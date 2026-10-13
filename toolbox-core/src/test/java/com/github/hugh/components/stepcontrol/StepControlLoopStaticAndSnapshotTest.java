package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.domain.StepState;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepControlLoopStaticAndSnapshotTest {

    private final String DEV_A = "device-sensor-001";
    private final String DEV_B = "device-sensor-002";

    @BeforeEach
    void setUp() {
        // 清理缓存，防止测试间干扰
        StepControlLoop.evictState(DEV_A);
        StepControlLoop.evictState(DEV_B);
    }

    // =========================================================================
    // 1. 外部状态快照还原测试 (StepControlLoop(config, state))
    // =========================================================================

    @Test
    @DisplayName("覆盖验证: 外部指定状态快照构造 (模拟从 Redis/DB 加载快照跳过冷启动)")
    void testExternalStateSnapshotConstructor() {
        StepConfig config = StepConfig.of(435.0, 15.0, ActionDirection.REVERSE);

        // 模拟从 Redis 中反序列化出来的持久化快照:
        // 该设备之前已经在线学习过，物理增益为 30.0 lux/%，当前开度在 8.0%
        StepState restoredState = new StepState();
        restoredState.setCurrentOutput(8.0);
        restoredState.setEstimatedPlantGain(30.0); // 已有增益记忆
        restoredState.setLastRecordedPV(390.0);
        restoredState.setLastActionOutput(8.0);
        restoredState.setLastAdjustTimestamp(10000L);

        // 使用快照构造控制器
        StepControlLoop loop = new StepControlLoop(config, restoredState);

        // 验证状态正确绑定
        assertSame(restoredState, loop.getInternalState(), "内部状态引用必须与外部快照一致");
        assertEquals(8.0, loop.getCurrentOutput(), 1e-4);

        // 当前照度 390 lux，离目标 435 lux 相差 45 lux (> 死区 15)
        // 因为快照中已经有 estimatedPlantGain = 30.0，因此它【不会】走 5% 的冷启动试探步，
        // 而是直接走黄金阻尼计算: delta = 45 * (0.7 / 30.0) = 1.05%
        long now = 12000L; // 满足冷却
        StepResult result = loop.compute(390.0, now);

        assertTrue(result.isNeedAction(), "应立即触发动作");
        assertEquals(1.05, result.getDelta(), 1e-2, "由于已有快照增益，应直接走黄金阻尼(1.05%)而不是冷启动(5%)！");
        assertEquals(9.05, result.getNewOutput(), 1e-2);
        assertEquals(9.05, loop.getCurrentOutput(), 1e-2, "快照对象的内部开度应被同步更新");
    }

    // =========================================================================
    // 2. 静态方法与状态监控测试 (compute(deviceId, ..., nowMs) & getInternalStateForMonitor)
    // =========================================================================

    @Test
    @DisplayName("覆盖验证: 静态极简计算与内部监控状态获取 (带时间戳注入)")
    void testStaticComputeWithTimestampAndMonitor() {
        StepConfig config = StepConfig.of(500.0, 10.0, ActionDirection.REVERSE);
        long baseTime = 10000L;

        // 1. 首次调用前，监控应该查不到该设备状态
        assertNull(StepControlLoop.getInternalStateForMonitor(DEV_A), "未调用前状态应为 null");

        // 2. 首次静态计算，初始开度给定 0.0%
        StepResult res1 = StepControlLoop.compute(DEV_A, 200.0, 0.0, config, baseTime);
        assertTrue(res1.isNeedAction());

        // 3. 校验 getInternalStateForMonitor 是否能正常获取内部缓存的状态
        StepState state = StepControlLoop.getInternalStateForMonitor(DEV_A);
        assertNotNull(state, "调用后必须自动创建并驻留状态");
        assertEquals(res1.getNewOutput(), state.getCurrentOutput(), 1e-4, "监控状态输出必须与计算结果对齐");
        assertEquals(baseTime, state.getLastAdjustTimestamp(), "时间戳必须被正确记录");
        assertEquals(200.0, state.getLastRecordedPV(), 1e-4, "PV 测量值必须被正确暂存");

        // 4. 第二次调用，利用内部状态池完成冷却拦截
        StepResult res2 = StepControlLoop.compute(DEV_A, 210.0, 0.0, config, baseTime + 200L); // 仅过 200ms
        assertFalse(res2.isNeedAction(), "未达冷却期，必须被拦截");
        assertEquals(StepReason.COOLDOWN_ACTIVE, res2.getReason());
    }

    // =========================================================================
    // 3. 静态无时间戳方法测试 (compute(deviceId, currentPV, currentOutput, config))
    // =========================================================================

    @Test
    @DisplayName("覆盖验证: 极简静态计算入口 (默认使用系统当前时间)")
    void testStaticComputeWithSystemCurrentTime() {
        StepConfig config = StepConfig.of(100.0, 5.0, ActionDirection.DIRECT);

        // 直接调用无需传时间戳的重载方法
        StepResult result = StepControlLoop.compute(DEV_A, 150.0, 10.0, config);

        assertNotNull(result);
        assertTrue(result.isNeedAction());

        // 验证内部记录的时间戳接近当前系统时间
        StepState state = StepControlLoop.getInternalStateForMonitor(DEV_A);
        assertNotNull(state);
        assertTrue(System.currentTimeMillis() - state.getLastAdjustTimestamp() < 1000L,
                "记录的时间戳必须与系统当前物理时间吻合");
    }

    // =========================================================================
    // 4. 状态淘汰与内存释放测试 (evictState(deviceId))
    // =========================================================================

    @Test
    @DisplayName("覆盖验证: 离线状态释放与内存清理 (evictState)")
    void testEvictState() {
        StepConfig config = StepConfig.of(100.0, 5.0, ActionDirection.REVERSE);

        // 触发一次调用，使其生成内存状态
        StepControlLoop.compute(DEV_A, 80.0, 0.0, config);
        assertNotNull(StepControlLoop.getInternalStateForMonitor(DEV_A), "执行后必须存在状态缓存");

        // 执行清理（如设备解绑或下线事件触发）
        StepControlLoop.evictState(DEV_A);

        // 校验已被完全移出，防止内存泄漏
        assertNull(StepControlLoop.getInternalStateForMonitor(DEV_A), "evictState 后状态池中必须被彻底移除");

        // 再次调用时应能正常重新初始化（从冷启动开始）
        StepResult reInitResult = StepControlLoop.compute(DEV_A, 80.0, 0.0, config);
        assertTrue(reInitResult.isNeedAction());
        assertNotNull(StepControlLoop.getInternalStateForMonitor(DEV_A));
    }

    // =========================================================================
    // 5. 多租户 / 多设备状态完全隔离验证
    // =========================================================================

    @Test
    @DisplayName("覆盖验证: 多设备状态隔离性 (验证不同 deviceId 互不干扰)")
    void testMultiDeviceStateIsolation() {
        StepConfig config = StepConfig.of(500.0, 10.0, ActionDirection.REVERSE);
        long now = 10000L;

        // 设备 A 初始开度 10.0%，当前照度 200 lux
        StepControlLoop.compute(DEV_A, 200.0, 10.0, config, now);

        // 设备 B 初始开度 80.0%，当前照度 495 lux（已在死区 ±10 内）
        StepResult resB = StepControlLoop.compute(DEV_B, 495.0, 80.0, config, now);

        StepState stateA = StepControlLoop.getInternalStateForMonitor(DEV_A);
        StepState stateB = StepControlLoop.getInternalStateForMonitor(DEV_B);

        // 验证两台设备独立计算、互不穿透
        assertNotEquals(stateA.getCurrentOutput(), stateB.getCurrentOutput(), "两设备输出开度必须独立");
        assertEquals(StepReason.WITHIN_DEAD_BAND, resB.getReason(), "设备 B 误差在死区内应判定为无需调节");

        // 仅清理设备 A，设备 B 必须不受任何影响
        StepControlLoop.evictState(DEV_A);
        assertNull(StepControlLoop.getInternalStateForMonitor(DEV_A));
        assertNotNull(StepControlLoop.getInternalStateForMonitor(DEV_B), "设备 B 状态必须依然完好保留");
    }
}
