package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
        // 场景 A: 恒照度联动 (反作用 REVERSE: 光越弱输出越大)
        // SP=450 lux, 死区=±20 lux, 增益=0.05, 最大步长=10%, 输出=0~100%, 冷却=2000ms
        lightingConfig = new StepConfig(
                450.0, 20.0, 0.05, 10.0, 0.0, 100.0, 2000L, 0.0, ActionDirection.REVERSE
        );
        lightingLoop = new StepControlLoop(lightingConfig, 50.0); // 初始亮度 50%

        // 场景 B: 空调制冷联动 (正作用 DIRECT: 温度越高输出越大)
        // SP=24.0 ℃, 死区=±0.5 ℃, 增益=5.0, 最大步长=15%, 输出=0~100%, 冷却=3000ms
        acCoolingConfig = new StepConfig(
                24.0, 0.5, 5.0, 15.0, 0.0, 100.0, 3000L, 0.0, ActionDirection.DIRECT
        );
        acCoolingLoop = new StepControlLoop(acCoolingConfig, 20.0); // 初始开度 20%

        // 场景 C: 单参快捷测试场景 (短冷却时间 50ms，用于验证 System.currentTimeMillis())
        fastCooldownConfig = new StepConfig(
                50.0, 1.0, 0.5, 10.0, 0.0, 100.0, 50L, 0.0, ActionDirection.DIRECT
        );
    }

    // =========================================================================
    // 模块 1: StepResult 状态判断方法针对性覆盖
    // =========================================================================
    @Nested
    @DisplayName("StepResult 快捷状态辅助方法测试")
    class StepResultHelperMethodsTest {

        @Test
        @DisplayName("isSkipped(): 验证执行动作与跳过调节时的状态取反逻辑")
        void testIsSkipped() {
            StepResult actionResult = StepResult.execute(60.0, 10.0, StepReason.ADJUSTED_OK);
            assertTrue(actionResult.isNeedAction());
            assertFalse(actionResult.isSkipped(), "需要动作时 isSkipped() 必须为 false");

            StepResult skipResult = StepResult.skip(50.0, StepReason.COOLDOWN_ACTIVE);
            assertFalse(skipResult.isNeedAction());
            assertTrue(skipResult.isSkipped(), "无需动作时 isSkipped() 必须为 true");
        }

        @Test
        @DisplayName("isWithinDeadBand(): 正向命中与原因不符时的状态判断")
        void testIsWithinDeadBand() {
            StepResult deadBandResult = StepResult.skip(50.0, StepReason.WITHIN_DEAD_BAND);
            assertTrue(deadBandResult.isWithinDeadBand(), "原因符合时必须返回 true");
            assertTrue(deadBandResult.isSkipped());

            // 其它跳过原因（例如冷却阻断）不应被误判为处于死区
            StepResult cooldownResult = StepResult.skip(50.0, StepReason.COOLDOWN_ACTIVE);
            assertFalse(cooldownResult.isWithinDeadBand(), "冷却中不可被误判为死区稳态");

            // 正常执行时不可为 true
            StepResult executeResult = StepResult.execute(55.0, 5.0, StepReason.ADJUSTED_OK);
            assertFalse(executeResult.isWithinDeadBand());
        }

        @Test
        @DisplayName("isSaturated(): 物理限幅抗饱和判断的正向与反向覆盖")
        void testIsSaturated() {
            StepResult satResult = StepResult.skip(100.0, StepReason.OUTPUT_SATURATED);
            assertTrue(satResult.isSaturated(), "输出饱和时必须返回 true");
            assertTrue(satResult.isSkipped());

            // 其它原因跳过时不应被判断为饱和
            StepResult deadBandResult = StepResult.skip(50.0, StepReason.WITHIN_DEAD_BAND);
            assertFalse(deadBandResult.isSaturated());

            // 正常动作时不应被判断为饱和
            StepResult executeResult = StepResult.execute(80.0, 10.0, StepReason.ADJUSTED_OK);
            assertFalse(executeResult.isSaturated());
        }
    }

    // =========================================================================
    // 模块 2: 正反向控制与步长计算测试
    // =========================================================================
    @Nested
    @DisplayName("正/反向模式计算及双向调节与限幅测试")
    class ActionDirectionAndStepTest {

        @Test
        @DisplayName("验证1.1: 反作用模式（照明）计算准确性及步长限幅（光弱调亮）")
        void testReverseAction_LightingScenario_IncreaseOutput() {
            long now = 10000L;
            // 传感器上报 300 lux (低于目标 450)
            // 误差 e = 450 - 300 = +150, delta = 150 * 0.05 = 7.5
            StepResult result = lightingLoop.compute(300.0, now);

            assertTrue(result.isNeedAction(), "光照过低，必须触发调光");
            assertFalse(result.isSkipped());
            assertEquals(57.5, result.getNewOutput(), 1e-4, "新输出应为 50 + 7.5 = 57.5%");
            assertEquals(7.5, result.getDelta(), 1e-4);
            assertEquals(StepReason.ADJUSTED_OK, result.getReason());
            assertEquals(57.5, lightingLoop.getCurrentOutput(), 1e-4);
        }

        @Test
        @DisplayName("验证1.2 [补充]: 反作用模式（照明）反向调小输出（外界太亮需降光）")
        void testReverseAction_LightingScenario_DecreaseOutput() {
            long now = 10000L;
            // 传感器上报 700 lux (远高于目标 450 lux)
            // 误差 e = 450 - 700 = -250
            // 理论调节量 delta = -250 * 0.05 = -12.5 -> 负向超出最大限制 maxStep(10.0)，截断为 -10.0
            StepResult result = lightingLoop.compute(700.0, now);

            assertTrue(result.isNeedAction(), "外界强光，必须触发调暗");
            assertEquals(-10.0, result.getDelta(), 1e-4, "单次最多降低 maxStep 10.0%");
            assertEquals(40.0, result.getNewOutput(), 1e-4, "初始 50% - 10% = 40%");
            assertEquals(StepReason.ADJUSTED_OK, result.getReason());
            assertEquals(40.0, lightingLoop.getCurrentOutput(), 1e-4);
        }

        @Test
        @DisplayName("验证2.1: 正作用模式（空调制冷）正向单次最大步长截断 (MaxStep)")
        void testDirectAction_CoolingWithStepClamping_IncreaseOutput() {
            long now = 10000L;
            // 室内温度 28.0 ℃ (高于目标 24.0)
            // e = 28.0 - 24.0 = +4.0 -> delta = 4.0 * 5.0 = 20.0 -> 截断为 15.0
            StepResult result = acCoolingLoop.compute(28.0, now);
            assertTrue(result.isNeedAction());
            assertEquals(15.0, result.getDelta(), 1e-4, "应触发 maxStep 限制，单次最多加大 15.0%");
            assertEquals(35.0, result.getNewOutput(), 1e-4, "初始 20% + 15% = 35%");
            assertEquals(StepReason.ADJUSTED_OK, result.getReason());
        }

        @Test
        @DisplayName("验证2.2 [补充]: 正作用模式（空调制冷）反向调低输出及负向步长截断 (温度过低关小制冷)")
        void testDirectAction_CoolingWithStepClamping_DecreaseOutput() {
            long now = 10000L;
            // 提高基准当前开度到 50%
            acCoolingLoop.syncFeedback(50.0);

            // 室内温度 20.0 ℃ (低于目标 24.0)
            // e = 20.0 - 24.0 = -4.0 -> delta = -4.0 * 5.0 = -20.0 -> 负向截断为 -15.0
            StepResult result = acCoolingLoop.compute(20.0, now);
            assertTrue(result.isNeedAction(), "室温过低，必须减小制冷量");
            assertEquals(-15.0, result.getDelta(), 1e-4, "负向步长亦应受到 maxStep=15.0% 的限制截断");
            assertEquals(35.0, result.getNewOutput(), 1e-4, "当前 50% - 15% = 35%");
            assertEquals(StepReason.ADJUSTED_OK, result.getReason());
        }
    }

    // =========================================================================
    // 模块 3: 死区容差判定及严格边界测试
    // =========================================================================
    @Nested
    @DisplayName("死区容差判定与边界临界测试")
    class DeadBandTest {

        @Test
        @DisplayName("验证3.1: 死区内部抑制动作")
        void testDeadbandSuppression() {
            long now = 10000L;

            // 照明场景 (450 ± 20 -> 430~470)
            StepResult lightResult = lightingLoop.compute(440.0, now);
            assertTrue(lightResult.isSkipped());
            assertTrue(lightResult.isWithinDeadBand(), "440 lux 落在容差死区内，必须判定为 isWithinDeadBand");
            assertEquals(StepReason.WITHIN_DEAD_BAND, lightResult.getReason());

            // 空调场景 (24.0 ± 0.5 -> 23.5~24.5)
            StepResult coolingResult = acCoolingLoop.compute(24.3, now);
            assertTrue(coolingResult.isSkipped());
            assertTrue(coolingResult.isWithinDeadBand());
            assertEquals(StepReason.WITHIN_DEAD_BAND, coolingResult.getReason());
        }

        @Test
        @DisplayName("验证3.2 [补充]: 死区严格边界点行为验证 (|PV - SP| == deadBand 仍属死区)")
        void testDeadbandStrictBoundaries() {
            long now = 10000L;

            // 刚好处于上限死区边界 450 + 20 = 470 lux
            StepResult upperResult = lightingLoop.compute(470.0, now);
            assertTrue(upperResult.isSkipped(), "死区边界值 [470 lux] 应包含在死区内或维持抑制");
            assertTrue(upperResult.isWithinDeadBand());

            // 刚好处于下限死区边界 450 - 20 = 430 lux
            StepResult lowerResult = lightingLoop.compute(430.0, now);
            assertTrue(lowerResult.isSkipped(), "死区边界值 [430 lux] 应包含在死区内或维持抑制");
            assertTrue(lowerResult.isWithinDeadBand());
        }
    }

    // =========================================================================
    // 模块 4: 时间节流与冷却机制测试
    // =========================================================================
    @Nested
    @DisplayName("时间节流与冷却期测试")
    class CooldownThrottleTest {

        @Test
        @DisplayName("验证4.1: 时间节流冷却保护 (高频数据不重复下发指令)")
        void testCooldownThrottling() {
            long t0 = 10000L;
            StepResult res1 = lightingLoop.compute(200.0, t0);
            assertTrue(res1.isNeedAction());

            // 1000ms 后再次上报数据 (冷却时间为 2000ms)
            StepResult res2 = lightingLoop.compute(200.0, t0 + 1000L);
            assertTrue(res2.isSkipped(), "处于冷却期，必须拦截");
            assertEquals(StepReason.COOLDOWN_ACTIVE, res2.getReason());
            assertFalse(res2.isWithinDeadBand());
            assertFalse(res2.isSaturated());

            // 2001ms 后上报数据 (已跨过冷却期)
            StepResult res3 = lightingLoop.compute(200.0, t0 + 2001L);
            assertTrue(res3.isNeedAction(), "冷却期结束，允许下一轮调节");
        }

        @Test
        @DisplayName("验证4.2 [补充]: 冷却时间临界点验证 (严格冷却区间边界判定)")
        void testCooldownExactBoundary() {
            long t0 = 10000L;
            lightingLoop.compute(200.0, t0);

            // 正好等于 cooldownMs (2000ms) 处的时间戳
            // 当 now - lastTime == cooldownMs 时，通常已允许调节（视为冷却结束）
            StepResult resBoundary = lightingLoop.compute(200.0, t0 + 2000L);
            assertTrue(resBoundary.isNeedAction(), "到达冷却时间临界点 (t0 + 2000ms) 应解除节流并允许调节");
        }
    }

    // =========================================================================
    // 模块 5: 输出限幅与抗饱和自愈测试
    // =========================================================================
    @Nested
    @DisplayName("抗饱和机制与极限边界自愈测试")
    class SaturationTest {

        @Test
        @DisplayName("验证5.1: 抗饱和及物理极限拦截 (上限 100% / 下限 0%)")
        void testOutputSaturation() {
            long now = 10000L;

            // 5.1 上限饱和测试：当前照明已满 100%，外界依然漆黑 (50 lux)，要求继续调大
            lightingLoop.syncFeedback(100.0);
            StepResult highResult = lightingLoop.compute(50.0, now);
            assertTrue(highResult.isSkipped());
            assertTrue(highResult.isSaturated(), "输出已达 100% 极限且请求增大，必须判定为 isSaturated");
            assertEquals(StepReason.OUTPUT_SATURATED, highResult.getReason());

            // 5.2 下限饱和测试：当前照明已关停 0%，室外烈日 (1000 lux)，要求继续调小
            lightingLoop.syncFeedback(0.0);
            StepResult lowResult = lightingLoop.compute(1000.0, now);
            assertTrue(lowResult.isSkipped());
            assertTrue(lowResult.isSaturated(), "输出已达 0% 极限且请求减小，必须判定为 isSaturated");
            assertEquals(StepReason.OUTPUT_SATURATED, lowResult.getReason());
        }

        @Test
        @DisplayName("验证5.2 [补充]: 极值状态下的反向自愈解饱和（在 100% 能够回调，在 0% 能够上调）")
        void testSaturationRecovery() {
            long now = 10000L;

            // 1. 当前处于 100% 极限状态，但环境亮起 (800 lux)，需要调暗照明
            lightingLoop.syncFeedback(100.0);
            StepResult decreaseResult = lightingLoop.compute(800.0, now);
            assertTrue(decreaseResult.isNeedAction(), "虽然当前处于 100% 饱和位，但调节方向为减小，必须允许动作");
            assertFalse(decreaseResult.isSaturated(), "有效反向回调不得误判为饱和拦截");
            assertTrue(decreaseResult.getNewOutput() < 100.0, "新输出必须从 100% 向下降低");

            // 2. 当前处于 0% 关断状态，但环境变黑 (100 lux)，需要调亮照明
            lightingLoop.syncFeedback(0.0);
            StepResult increaseResult = lightingLoop.compute(100.0, now + 3000L);
            assertTrue(increaseResult.isNeedAction(), "虽然当前处于 0% 关停位，但调节方向为增大，必须允许动作");
            assertFalse(increaseResult.isSaturated(), "有效反向回调不得误判为饱和拦截");
            assertTrue(increaseResult.getNewOutput() > 0.0, "新输出必须从 0% 向上增大");
        }
    }

    // =========================================================================
    // 模块 6: 动态配置热更、状态同步与多轮闭环渐进收敛
    // =========================================================================
    @Nested
    @DisplayName("动态热更、外部同步与闭环渐进仿真")
    class LifecycleAndSimulationTest {

        @Test
        @DisplayName("验证6: 运行时动态参数热更新 (无须停机无感生效)")
        void testRuntimeDynamicConfigUpdate() {
            long now = 10000L;

            // 当前光照 450 lux，原本刚好达标
            StepResult initialResult = lightingLoop.compute(450.0, now);
            assertTrue(initialResult.isSkipped());
            assertTrue(initialResult.isWithinDeadBand());

            // 动态热更：用户提高设定值至 600 lux
            lightingLoop.getConfig().setSetPoint(600.0);

            // 冷却期后再次上报 450 lux，相对于 600 已偏暗，应触发补光
            StepResult result = lightingLoop.compute(450.0, now + 3000L);
            assertTrue(result.isNeedAction(), "目标热更生效，应立即计算新的补光动作");
            assertFalse(result.isSkipped());
            assertTrue(result.getNewOutput() > 50.0);
        }

        @Test
        @DisplayName("验证7: 外部人工操作状态同步 (物理面板外部纠偏与内部基准同步)")
        void testSyncFeedback() {
            // 用户通过物理面板强行将灯光调暗至 15%
            lightingLoop.syncFeedback(15.0);
            assertEquals(15.0, lightingLoop.getCurrentOutput(), 1e-4);

            long now = 10000L;
            // 此时环境暗，计算出需要 +10% 亮度，必须以 15% 为基准累加至 25%
            StepResult result = lightingLoop.compute(200.0, now);
            assertTrue(result.isNeedAction());
            assertEquals(25.0, result.getNewOutput(), 1e-4, "应在外部纠偏值 15% 的基础上累加至 25%");
        }

        @Test
        @DisplayName("验证8: 复杂多轮闭环渐进收敛验证 (物理环境模拟迭代)")
        void testClosedLoopConvergenceSimulation() {
            double currentTemp = 32.0;
            long simulatedTime = 10000L;
            int loopCount = 0;

            for (int i = 0; i < 20; i++) {
                StepResult action = acCoolingLoop.compute(currentTemp, simulatedTime);

                // 物理环境根据制冷量实时产生降温响应（开度 1% 降低 0.1 ℃）
                currentTemp = 32.0 - (acCoolingLoop.getCurrentOutput() * 0.1);

                if (action.isSkipped() && action.isWithinDeadBand()) {
                    break; // 算法已经平滑稳定在死区范围内，停止调节
                }

                loopCount++;
                simulatedTime += 3500L; // 模拟时间推移 3.5 秒
            }

            assertTrue(currentTemp >= 23.5 && currentTemp <= 24.5, "经过反馈收敛，温度必须落入死区");
            assertTrue(loopCount <= 6, "收敛速度合理，应在 6 轮步进内达成平衡，实际步数: " + loopCount);
        }
    }

    // =========================================================================
    // 模块 7: 单参重载 compute(double pv) 基于系统时钟的测试
    // =========================================================================
    @Nested
    @DisplayName("单参重载 compute(double pv) 测试")
    class SingleArgComputeTest {

        @Test
        @DisplayName("测试单参 compute：首次调用正常执行调整并更新状态")
        void testComputeSingleArg_Success() {
            StepControlLoop loop = new StepControlLoop(fastCooldownConfig, 20.0);

            // PV = 70.0 -> error = 70.0 - 50.0 = 20.0
            // delta = min(10.0, 20.0 * 0.5) = 10.0
            // nextOutput = 20.0 + 10.0 = 30.0
            StepResult result = loop.compute(70.0);

            assertNotNull(result);
            assertTrue(result.isNeedAction());
            assertFalse(result.isSkipped());
            assertEquals(StepReason.ADJUSTED_OK, result.getReason());
            assertEquals(30.0, result.getNewOutput(), 1e-6);
            assertEquals(10.0, result.getDelta(), 1e-6);
            assertEquals(30.0, loop.getCurrentOutput(), 1e-6);
        }

        @Test
        @DisplayName("测试单参 compute：连续调用受冷却时间节流限制，休眠后恢复调整")
        void testComputeSingleArg_CooldownThrottleAndRecovery() throws InterruptedException {
            StepControlLoop loop = new StepControlLoop(fastCooldownConfig, 20.0);

            // 1. 首次触发计算，正常执行
            StepResult firstResult = loop.compute(70.0);
            assertEquals(StepReason.ADJUSTED_OK, firstResult.getReason());
            assertEquals(30.0, loop.getCurrentOutput(), 1e-6);

            // 2. 紧接着立即二次调用（耗时 < 50ms cooldown），预期被节流拦截
            StepResult throttledResult = loop.compute(80.0);
            assertEquals(StepReason.COOLDOWN_ACTIVE, throttledResult.getReason());
            assertTrue(throttledResult.isSkipped());
            assertEquals(30.0, throttledResult.getNewOutput(), "被节流跳过时应保持原输出不变");
            assertEquals(30.0, loop.getCurrentOutput(), "内部当前输出不应发生变化");

            // 3. 等待超过冷却时间（休眠 60ms > fastCooldownConfig 的 50ms）
            Thread.sleep(60L);

            // 4. 再次调用，冷却时间结束，应能再次执行调节
            StepResult recoveredResult = loop.compute(80.0);
            assertEquals(StepReason.ADJUSTED_OK, recoveredResult.getReason());
            assertTrue(recoveredResult.isNeedAction());
            assertFalse(recoveredResult.isSkipped());
            assertEquals(40.0, recoveredResult.getNewOutput(), 1e-6);
            assertEquals(40.0, loop.getCurrentOutput(), 1e-6);
        }

        @Test
        @DisplayName("测试单参 compute：误差在死区以内时正确跳过")
        void testComputeSingleArg_DeadBandSkip() {
            StepControlLoop loop = new StepControlLoop(fastCooldownConfig, 20.0);

            // PV = 50.5 -> error = 50.5 - 50.0 = 0.5 <= deadBand(1.0)
            StepResult result = loop.compute(50.5);

            assertTrue(result.isSkipped());
            assertTrue(result.isWithinDeadBand());
            assertEquals(StepReason.WITHIN_DEAD_BAND, result.getReason());
            assertEquals(20.0, result.getNewOutput());
            assertEquals(20.0, loop.getCurrentOutput());
        }
    }
}