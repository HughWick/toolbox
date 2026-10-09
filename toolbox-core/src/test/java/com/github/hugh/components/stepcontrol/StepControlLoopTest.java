package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("通用闭环反馈控制引擎统一测试套件 (JUnit 5)")
class StepControlLoopTest {

    private StepConfig lightingConfig;
    private StepControlLoop lightingLoop;

    private StepConfig acCoolingConfig;
    private StepControlLoop acCoolingLoop;
    // 专为验证基于 System.currentTimeMillis() 的单参方法准备的短冷却时间配置
    private StepConfig fastCooldownConfig;
    @BeforeEach
    void setUp() {
        // 场景 A: 恒照度联动 (反作用: 光越弱输出越大)
        // SP=450 lux, 死区=±20 lux, 增益=0.05, 最大步长=10%, 输出=0~100%, 冷却=2000ms
        lightingConfig = new StepConfig(
                450.0, 20.0, 0.05, 10.0, 0.0, 100.0, 2000L, ActionDirection.REVERSE
        );
        lightingLoop = new StepControlLoop(lightingConfig, 50.0); // 初始亮度 50%

        // 场景 B: 空调制冷联动 (正作用: 温度越高输出越大)
        // SP=24.0 ℃, 死区=±0.5 ℃, 增益=5.0, 最大步长=15%, 输出=0~100%, 冷却=3000ms
        acCoolingConfig = new StepConfig(
                24.0, 0.5, 5.0, 15.0, 0.0, 100.0, 3000L, ActionDirection.DIRECT
        );
        acCoolingLoop = new StepControlLoop(acCoolingConfig, 20.0); // 初始开度 20%
        // 场景 C: 单参快捷测试场景 (短冷却时间 50ms，用于验证 System.currentTimeMillis())
        fastCooldownConfig = new StepConfig(
                50.0, 1.0, 0.5, 10.0, 0.0, 100.0, 50L, ActionDirection.DIRECT
        );

    }

    @Test
    @DisplayName("验证1: 反作用模式（照明）计算准确性及步长限幅")
    void testReverseAction_LightingScenario() {
        long now = 10000L;
        // 传感器上报 300 lux (低于目标 450)
        // 误差 e = 450 - 300 = +150
        // 理论调节量 delta = 150 * 0.05 = 7.5 (未超最大步长 10.0)
        StepResult result = lightingLoop.compute(300.0, now);

        assertTrue(result.isNeedAction(), "光照过低，必须触发调光");
        assertEquals(57.5, result.getNewOutput(), 1e-4, "新输出应为 50 + 7.5 = 57.5%");
        assertEquals(7.5, result.getDelta(), 1e-4);
        assertEquals(StepReason.ADJUSTED_OK, result.getReason());
        assertEquals(57.5, lightingLoop.getCurrentOutput(), 1e-4);
    }

    @Test
    @DisplayName("验证2: 正作用模式（空调制冷）计算及单次最大步长截断 (MaxStep)")
    void testDirectAction_CoolingWithStepClamping() {
        long now = 10000L;
        // 室内温度 28.0 ℃ (高于目标 24.0)
        // 误差 e = 28.0 - 24.0 = +4.0
        // 理论调节量 delta = 4.0 * 5.0 = 20.0 -> 超出最大限制 maxStep=15.0，应被截断
        StepResult result = acCoolingLoop.compute(28.0, now);
        assertTrue(result.isNeedAction(), "温度过高，必须加大制冷输出");
        assertEquals(15.0, result.getDelta(), 1e-4, "应触发 maxStep 限制，单次最多加大 15.0%");
        assertEquals(35.0, result.getNewOutput(), 1e-4, "初始 20% + 15% = 35%");
        assertEquals(StepReason.ADJUSTED_OK, result.getReason());
    }

    @Test
    @DisplayName("验证3: 死区容差判定 (双向测试：落在死区内时不产生指令)")
    void testDeadbandSuppression() {
        long now = 10000L;

        // 3.1 测试照明场景死区 (450 ± 20 -> 430~470)
        StepResult lightResult = lightingLoop.compute(440.0, now);
        assertFalse(lightResult.isNeedAction(), "440 lux 落在容差死区内，不得动作");
        assertEquals(StepReason.WITHIN_DEAD_BAND, lightResult.getReason());

        // 3.2 测试空调场景死区 (24.0 ± 0.5 -> 23.5~24.5)
        StepResult coolingResult = acCoolingLoop.compute(24.3, now);
        assertFalse(coolingResult.isNeedAction(), "24.3 ℃ 落在容差死区内，不得动作");
        assertEquals(StepReason.WITHIN_DEAD_BAND, coolingResult.getReason());
    }

    @Test
    @DisplayName("验证4: 时间节流冷却保护 (高频数据不重复下发指令)")
    void testCooldownThrottling() {
        long t0 = 10000L;
        // 第一次成功触发调节
        StepResult res1 = lightingLoop.compute(200.0, t0);
        assertTrue(res1.isNeedAction());

        // 1000ms 后再次上报数据 (冷却时间为 2000ms)
        StepResult res2 = lightingLoop.compute(200.0, t0 + 1000L);
        assertFalse(res2.isNeedAction(), "处于冷却期，必须拦截");
        assertEquals(StepReason.COOLDOWN_ACTIVE, res2.getReason());

        // 2001ms 后上报数据 (已跨过冷却期)
        StepResult res3 = lightingLoop.compute(200.0, t0 + 2001L);
        assertTrue(res3.isNeedAction(), "冷却期结束，允许下一轮调节");
    }

    @Test
    @DisplayName("验证5: 抗饱和及物理极限拦截 (上限 100% / 下限 0%)")
    void testOutputSaturation() {
        long now = 10000L;

        // 5.1 上限饱和测试：当前照明已满 100%，外界依然一片漆黑 (50 lux)
        lightingLoop.syncFeedback(100.0);
        StepResult highResult = lightingLoop.compute(50.0, now);
        assertFalse(highResult.isNeedAction(), "输出已达 100% 极限，禁止继续下发指令");
        assertEquals(StepReason.OUTPUT_SATURATED, highResult.getReason());

        // 5.2 下限饱和测试：当前照明已关停 0%，室外烈日当空 (1000 lux)
        lightingLoop.syncFeedback(0.0);
        StepResult lowResult = lightingLoop.compute(1000.0, now);
        assertFalse(lowResult.isNeedAction(), "输出已归 0%，不能再降低");
        assertEquals(StepReason.OUTPUT_SATURATED, lowResult.getReason());
    }

    @Test
    @DisplayName("验证6: 运行时动态参数热更新 (无须停机无感生效)")
    void testRuntimeDynamicConfigUpdate() {
        long now = 10000L;

        // 当前光照 450 lux，原本刚好达标
        assertFalse(lightingLoop.compute(450.0, now).isNeedAction());

        // 用户在手机 App 上将目标设定值由 450 调高至 600 lux
        lightingLoop.getConfig().setSetPoint(600.0);

        // 冷却期后再次上报 450 lux，此时相对于新的目标 600 已偏暗，应立即触发补光
        StepResult result = lightingLoop.compute(450.0, now + 3000L);
        assertTrue(result.isNeedAction(), "目标热更生效，应立即计算新的补光动作");
        assertTrue(result.getNewOutput() > 50.0);
    }

    @Test
    @DisplayName("验证7: 外部人工操作状态同步 (物理旋钮与内部基准同步)")
    void testSyncFeedback() {
        // 用户通过墙壁物理面板将灯光调暗至 15%
        lightingLoop.syncFeedback(15.0);
        assertEquals(15.0, lightingLoop.getCurrentOutput(), 1e-4);

        long now = 10000L;
        // 环境过暗需要增加 10% 亮度，算法必须以用户最新的 15% 为基准计算，而不是遗留的 50%
        StepResult result = lightingLoop.compute(200.0, now);
        assertTrue(result.isNeedAction());
        assertEquals(25.0, result.getNewOutput(), 1e-4, "应在外部纠偏值 15% 的基础上累加至 25%");
    }

    @Test
    @DisplayName("验证8: 复杂多轮闭环渐进收敛验证 (物理环境模拟迭代)")
    void testClosedLoopConvergenceSimulation() {
        // 模拟物理房间环境：
        // 初始基础温度 32.0 ℃
        // 制冷效果模型：空调每开启 1% 功率，大约降温 0.1 ℃
        // 目标：将房间通过多轮节流调节稳定在 24.0 ± 0.5 ℃

        double currentTemp = 32.0;
        long simulatedTime = 10000L;
        int loopCount = 0;

        for (int i = 0; i < 20; i++) {
            StepResult action = acCoolingLoop.compute(currentTemp, simulatedTime);

            // 物理环境根据制冷量实时产生降温响应
            currentTemp = 32.0 - (acCoolingLoop.getCurrentOutput() * 0.1);

            if (!action.isNeedAction() && StepReason.WITHIN_DEAD_BAND.equals(action.getReason())) {
                break; // 算法已经平滑稳定在设定温度内，停止多余动作
            }

            loopCount++;
            simulatedTime += 3500L; // 模拟时间推移 3.5 秒
        }

        // 断言最终室内温度落在设定死区 [23.5, 24.5] 范围内
        assertTrue(currentTemp >= 23.5 && currentTemp <= 24.5, "经过反馈收敛，温度必须落入死区");
        assertTrue(loopCount <= 6, "收敛速度合理，应在 6 轮步进内达成平衡，实际步数: " + loopCount);
    }
    @Test
    @DisplayName("测试单参 compute：首次调用正常执行调整并更新状态")
    void testComputeSingleArg_Success() {
        // 使用 fastCooldownConfig (SP=50.0, deadBand=1.0, gain=0.5, maxStep=10.0, DIRECT模式)
        StepControlLoop loop = new StepControlLoop(fastCooldownConfig, 20.0);

        // PV = 70.0 -> error = 70.0 - 50.0 = 20.0
        // delta = min(10.0, 20.0 * 0.5) = 10.0
        // nextOutput = 20.0 + 10.0 = 30.0
        StepResult result = loop.compute(70.0);

        assertNotNull(result);
        assertTrue(result.isNeedAction());
        assertEquals(StepReason.ADJUSTED_OK, result.getReason());
        assertEquals(30.0, result.getNewOutput(), 1e-6);
        assertEquals(10.0, result.getDelta(), 1e-6);

        // 验证内部状态被正确更新
        assertEquals(30.0, loop.getCurrentOutput(), 1e-6);
        assertTrue(loop.getLastAdjustTimestamp().get() > 0L, "执行后时间戳应被记录为当前系统时间");
    }

    @Test
    @DisplayName("测试单参 compute：连续调用时受冷却时间节流限制，休眠后恢复调整")
    void testComputeSingleArg_CooldownThrottleAndRecovery() throws InterruptedException {
        // 使用 fastCooldownConfig (cooldownMs = 50ms)
        StepControlLoop loop = new StepControlLoop(fastCooldownConfig, 20.0);

        // 1. 首次触发计算，正常执行
        StepResult firstResult = loop.compute(70.0);
        assertEquals(StepReason.ADJUSTED_OK, firstResult.getReason());
        assertEquals(30.0, loop.getCurrentOutput(), 1e-6);

        // 2. 紧接着立即二次调用（耗时 < 50ms cooldown），预期被节流拦截
        StepResult throttledResult = loop.compute(80.0);
        assertEquals(StepReason.COOLDOWN_ACTIVE, throttledResult.getReason());
        assertFalse(throttledResult.isNeedAction());
        assertEquals(30.0, throttledResult.getNewOutput(), "被节流跳过时应保持原输出不变");
        assertEquals(30.0, loop.getCurrentOutput(), "内部当前输出不应发生变化");

        // 3. 等待超过冷却时间（休眠 60ms > fastCooldownConfig 的 50ms）
        Thread.sleep(60L);

        // 4. 再次调用，冷却时间结束，应能再次执行调节
        // PV = 80.0 -> error = 80.0 - 50.0 = 30.0, delta = maxStep = 10.0, nextOutput = 30.0 + 10.0 = 40.0
        StepResult recoveredResult = loop.compute(80.0);
        assertEquals(StepReason.ADJUSTED_OK, recoveredResult.getReason());
        assertTrue(recoveredResult.isNeedAction());
        assertEquals(40.0, recoveredResult.getNewOutput(), 1e-6);
        assertEquals(40.0, loop.getCurrentOutput(), 1e-6);
    }

    @Test
    @DisplayName("测试单参 compute：误差在死区以内时正确跳过")
    void testComputeSingleArg_DeadBandSkip() {
        // 使用 fastCooldownConfig (SP=50.0, deadBand=1.0)
        StepControlLoop loop = new StepControlLoop(fastCooldownConfig, 20.0);

        // PV = 50.5 -> error = 50.5 - 50.0 = 0.5 <= deadBand(1.0)
        StepResult result = loop.compute(50.5);

        assertFalse(result.isNeedAction());
        assertEquals(StepReason.WITHIN_DEAD_BAND, result.getReason());
        assertEquals(20.0, result.getNewOutput());
        assertEquals(20.0, loop.getCurrentOutput());
        assertEquals(0L, loop.getLastAdjustTimestamp().get(), "因未实际执行动作，时间戳不应被更新");
    }
}