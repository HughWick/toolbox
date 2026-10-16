package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.domain.StepState;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * 环境照度步进式闭环控制回路 (Step Control Loop) 测试套件
 *
 * 【开发者阅读指南】：
 * 1. 业务接入请直接参考: [测试 01] testProductionApi_ComputeWithoutInitialOutput (生产极简静态接入)
 * 2. 算法自适应收敛原理参考: [测试 02] testSimpleDimming_AutoTuning (在线增益辨识与自整定)
 * 3. 遮阳/百叶窗正作用控制参考: [测试 03] testDirectActionShadingContinuousRegulation (DIRECT 模式)
 * 4. 边界抗饱和与冷却防抖参考: [测试 04] 与 [测试 05]
 * 5. 手动调参专家模式参考: [测试 06]
 */
class StepLightLoopTest {

    // =========================================================================
    // 第一部分：业务开发者必读 —— 极简接入与自适应收敛
    // =========================================================================

    @Test
    @DisplayName("【必读 01·生产接入】真实业务入口验证 (3参免传开度，静态方法与上下文自托管)")
    void testProductionApi_ComputeWithoutInitialOutput() {
        // 0. 准备设备标识并清理状态池（保证单测独立性）
        final String deviceId = "smart_dimmer_room_101";
        StepControlLoop.evictState(deviceId);

        // 1. 物理环境真实特征 (模拟现场环境)
        final double ambientBaseLux = 150.0;       // 自然底光 150 lux
        final double maxBulbContribution = 3000.0; // 灯具满开光强 3000 lux (真实物理增益: 30 lux/%)
        final double targetSP = 535.0;             // 期望维持的目标照度 535 lux

        // 2. 业务规则配置 (目标 535 lux，容差 ±15 lux，反作用调节：天黑加力)
        StepConfig config = StepConfig.of(targetSP, 15.0, ActionDirection.REVERSE);
//        config.setMaxStep(40.0);
        // 【关键技巧】：单测环境将冷却节流置 0，允许连续计算，完全无需暴露 nowMs 也不用 Thread.sleep
        config.setCooldownMs(0L);

        System.out.println("\n=========================================================================================================");
        System.out.printf("   [生产方法极简闭环] 设备ID: %s | 目标: %.1f lux | 物理增益: 30.0 lux/%% | 死区: ±%.1f lux\n",
                deviceId, config.getSetPoint(), config.getDeadBand());
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-10s | %-8s | %-10s | %-10s | %-18s | %-20s\n",
                "轮次", "当前照度(PV)", "目标(SP)", "执行器开度(CV)", "步进(Δ)", "在线辨识增益", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        int stableCount = 0;
        int maxRounds = 8;
        double previousLux = ambientBaseLux;

        // 模拟物理灯具的真实硬件当前开度 (初始为 0.0%)
        // 开发者完全不需要把这个开度传给算法，算法从 0 基准自适应启动
        double simulatedBulbOutput = 0.0;

        for (int round = 1; round <= maxRounds; round++) {
            // 物理环境演化: 当前照度 = 自然底光 + 灯光实际贡献
            double currentLux = ambientBaseLux + (simulatedBulbOutput / 100.0) * maxBulbContribution;

            // =====================================================================
            // 开发者生产中唯一调用的三参数方法：完全使用系统真实时间，无额外参数！
            // =====================================================================
            StepResult result = StepControlLoop.compute(deviceId, currentLux, config);

            // 业务处理逻辑：当下发有调节动作时，硬件更新开度
            if (result.isNeedAction()) {
                simulatedBulbOutput = result.getNewOutput();
            }

            // 获取内部透明托管的设备状态（仅用于控制台观测与断言）
            StepState monitorState = StepControlLoop.getInternalStateForMonitor(deviceId);
            assertNotNull(monitorState, "首次调用后，底层必须已自动初始化该设备的状态上下文");

            Double estimatedGain = monitorState.getEstimatedPlantGain();
            String gainDisplay = (estimatedGain != null) ? String.format("%.2f lux/%%", estimatedGain) : "冷启动微步探测";
            String actionDesc = formatActionDescription(result);

            System.out.printf("#%-5d | %8.1f lux | %8.1f | %9.2f %% | %+8.2f %% | %-16s | %s\n",
                    round, currentLux, config.getSetPoint(),
                    simulatedBulbOutput, result.getDelta(), gainDisplay, actionDesc
            );

            // 物理舒适度断言: 调光应单调递增逼近，杜绝晃眼反向跳动
            if (round > 1 && result.getReason() != StepReason.WITHIN_DEAD_BAND) {
                assertTrue(currentLux >= previousLux - 1e-4,
                        String.format("第 %d 轮出现反向超调跳动！前次: %.1f, 本次: %.1f", round, previousLux, currentLux));
            }
            previousLux = currentLux;

            if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                stableCount++;
            }
        }
        System.out.println("=========================================================================================================\n");

        // 1. 最终控制精度断言
        double finalLux = ambientBaseLux + (simulatedBulbOutput / 100.0) * maxBulbContribution;
        assertTrue(stableCount >= 4, "系统未能在后半程持续保持在稳态死区内！");
        assertTrue(Math.abs(finalLux - targetSP) <= config.getDeadBand(),
                String.format("最终照度未落在死区范围内！当前照度: %.1f, 目标: %.1f", finalLux, targetSP));

        // 2. 自适应物理增益在线学习断言 (真实物理增益为 30.0 lux/%)
        StepState finalState = StepControlLoop.getInternalStateForMonitor(deviceId);
        assertNotNull(finalState.getEstimatedPlantGain(), "回路收敛后必须成功辨识出对象的物理增益！");
        assertEquals(30.0, finalState.getEstimatedPlantGain(), 5.0,
                "在线辨识出的增益值与真实物理增益偏差过大！");

        // =====================================================================
        // 3. 顺便验证：真实系统时钟下的冷却防抖（Cooldown）是否生效
        // =====================================================================
        // 将冷却时间改为 5000 毫秒
        config.setCooldownMs(5000L);
        // 偏离死区（制造误差），紧接着立即调用
        double disturbedLux = 300.0;
        StepResult throttledResult = StepControlLoop.compute(deviceId, disturbedLux, config);
        // 刚调完不久，必定触发冷却节流
        assertEquals(StepReason.COOLDOWN_ACTIVE, throttledResult.getReason(),
                "生产环境下的 Cooldown 节流保护机制未生效！");
    }

    @Test
    @DisplayName("【原理 02·自学习逼近】面向对象极简闭环 (零算法参数配置，增益在线辨识与无超调验证)")
    void testSimpleDimming_AutoTuning() {
        // 1. 物理环境真实特征 (模拟实际场景，算法自身完全无感知)
        final double ambientBaseLux = 150.0;       // 环境自然底光 150 lux
        final double maxBulbContribution = 3000.0; // 灯具最大光强 3000 lux (真实增益 30 lux/%)
        final double targetSP = 535.0;             // 期望维持的目标照度 535.0 lux

        // =========================================================================
        // 2. 开发者极简调用 (只需关注业务规则：目标 535 lux，容差 ±15 lux，天黑加力)
        // =========================================================================
        StepConfig config = StepConfig.of(targetSP, 15.0, ActionDirection.REVERSE);

        // 初始化闭环控制器，初始输出开度 0.0%
        StepControlLoop loop = new StepControlLoop(config, 0.0);

        System.out.println("\n=========================================================================================================");
        System.out.printf("   [极简自学习闭环] 目标: %.1f lux | 底光: %.1f lux | 物理增益: 30.0 lux/%% | 业务死区: ±%.1f lux\n",
                config.getSetPoint(), ambientBaseLux, config.getDeadBand());
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-9s | %-10s | %-8s | %-10s | %-10s | %-18s | %-20s\n",
                "轮次", "仿真时间", "当前照度(PV)", "目标(SP)", "输出开度(CV)", "步进(Δ)", "在线辨识增益", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        long simulatedTime = 10000L;
        int stableCount = 0;
        int maxRounds = 8;
        double previousLux = ambientBaseLux;

        for (int round = 1; round <= maxRounds; round++) {
            // 物理环境演化: 当前实际照度 = 自然底光 + 灯光当前实际贡献
            double currentLux = ambientBaseLux + (loop.getCurrentOutput() / 100.0) * maxBulbContribution;

            // =====================================================================
            // 开发者日常调用只有这一行：把当前测量值喂给控制器
            // =====================================================================
            StepResult result = loop.compute(currentLux, simulatedTime);

            // 业务处理逻辑：若需要动作，则向下游硬件下发开度指令
            if (result.isNeedAction()) {
                // mockHardwareDevice.setBrightness(result.getNewOutput());
            }

            // 获取内部自适应辨识到的物理增益（仅供控制台观察）
            Double estimatedGain = loop.getInternalState().getEstimatedPlantGain();
            String gainDisplay = (estimatedGain != null) ? String.format("%.2f lux/%%", estimatedGain) : "冷启动微步探测";

            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);

            System.out.printf("#%-5d | %-10s | %8.1f lux | %8.1f | %9.2f %% | %+8.2f %% | %-16s | %s\n",
                    round, timeStr, currentLux, config.getSetPoint(),
                    loop.getCurrentOutput(), result.getDelta(), gainDisplay, actionDesc
            );

            // 物理舒适度断言: 调光应单调平滑递增，绝对不能出现“先暴亮再调暗”的晃眼超调
            if (round > 1 && result.getReason() != StepReason.WITHIN_DEAD_BAND) {
                assertTrue(currentLux >= previousLux - 1e-4,
                        String.format("第 %d 轮出现反向超调跳动！前次照度: %.1f, 本次照度: %.1f", round, previousLux, currentLux));
            }
            previousLux = currentLux;

            if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                stableCount++;
            }
            simulatedTime += 2000L; // 模拟每 2 秒一个周期 (超过 1 秒冷却时间)
        }
        System.out.println("=========================================================================================================\n");

        // 最终控制品质断言
        double finalLux = ambientBaseLux + (loop.getCurrentOutput() / 100.0) * maxBulbContribution;
        assertTrue(stableCount >= 4, "系统未能在后半程保持持续稳态！");
        assertTrue(Math.abs(finalLux - targetSP) <= config.getDeadBand(),
                String.format("最终照度未落在死区范围内！最终照度: %.1f lux, 目标: %.1f lux", finalLux, targetSP));
    }

    // =========================================================================
    // 第二部分：场景拓展 —— 正作用模式（遮阳/降温系统）
    // =========================================================================

    @Test
    @DisplayName("【模式 03·正作用遮阳】智能遮阳帘闭环调光控制验证 (DIRECT模式: 越亮越加力遮挡)")
    void testDirectActionShadingContinuousRegulation() {
        // 1. 物理环境模型构建 (电动遮阳帘 / 百叶窗调光系统):
        // 假设室外强光直射底色照度 = 3500.0 lux
        // 百叶帘完全关闭(开度 100%)时最大遮挡光强 = 2500.0 lux
        // 室内实际照度物理公式: PV = ambientBaseLux - (CV / 100.0) * maxShadingContribution
        final double ambientBaseLux = 3500.0;
        final double maxShadingContribution = 2500.0;
        final double targetSP = 1500.0; // 目标舒适照度 1500 lux

        // 物理可行性先验检查:
        // 遮阳帘全开(CV=0)时照度为 3500 lux; 全闭(CV=100%)时照度为 1000 lux (3500 - 2500)
        // 目标 1500 lux 必须落在系统物理可达域 [1000, 3500] 之内
        assertTrue(ambientBaseLux >= targetSP, "自然环境底光低于目标SP，遮阳机构无须介入且无法增光！");
        assertTrue((ambientBaseLux - maxShadingContribution) <= targetSP, "遮阳机构最大遮蔽能力不足，无法压制到目标SP！");

        // 2. 控制器参数配置:
        // 执行器灵敏度: 2500 lux / 100% = 25.0 lux/1% 开度
        // 死区容差: ±25 lux (相当于允许 ±1.0% 的遮阳帘机械定位容差)
        StepConfig config = new StepConfig(
                targetSP,
                25.0,                  // 死区 (±25 lux)
                0.040,                 // 比例增益 Kp
                20.0,                  // 单次最大动作步长 20%
                0.0,                   // 遮蔽开度下限 0% (全收起，无遮挡)
                100.0,                 // 遮蔽开度上限 100% (全放下，最大遮挡)
                1000L,                 // 动作最小时间间隔 (1秒)
                0.0,
                // 遮光设备天然是 DIRECT（越亮越要加力挡光；同理制冷空调也是越热越加力制冷）
                ActionDirection.DIRECT // 正作用: 当前照度(PV)高于目标(SP)时，加大遮挡输出(CV 增加)
        );

        // 初始百叶帘完全开启 (CV = 0%, 室内照度 3500 lux，处于极度过亮工况)
        StepControlLoop loop = new StepControlLoop(config, 0.0);

        System.out.println("\n=========================================================================================================");
        System.out.printf("          [IoT 闭环控制-正作用] 智能遮阳帘压制强光至目标 %.1f lux 仿真日志 (室外光: %.1f lux)\n", targetSP, ambientBaseLux);
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-9s | %-10s | %-8s | %-11s | %-12s | %-10s | %-18s\n",
                "轮次", "仿真时间", "当前照度(PV)", "目标(SP)", "控制误差(e)", "遮阳开度(CV)", "步进(Δ)", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        long simulatedTime = 10000L;
        int stableCount = 0;
        int maxRounds = 12;

        for (int round = 1; round <= maxRounds; round++) {
            // 物理环境模型计算: 遮阳开度越大，照度越低 (负相关关系)
            double currentLux = ambientBaseLux - (loop.getCurrentOutput() / 100.0) * maxShadingContribution;

            // 闭环控制核心决策计算
            StepResult result = loop.compute(currentLux, simulatedTime);

            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);

            System.out.printf("#%-5d | %-10s | %8.1f lux | %8.1f | %+10.1f | %10.2f %% | %+9.2f %% | %s\n",
                    round, timeStr, currentLux, config.getSetPoint(),
                    (config.getSetPoint() - currentLux),
                    loop.getCurrentOutput(),
                    result.getDelta(),
                    actionDesc
            );

            // 判定恒定状态: 命中死区 或 达到输出物理限幅饱和
            if (result.getReason() == StepReason.WITHIN_DEAD_BAND || result.getReason() == StepReason.OUTPUT_SATURATED) {
                stableCount++;
            }

            simulatedTime += 2000L; // 模拟每 2 秒一个控制周期
        }
        System.out.println("=========================================================================================================\n");

        // 3. 稳态与控制收敛断言:
        // 理论稳态遮挡开度计算: 3500 - (CV/100)*2500 = 1500  => CV 理论值应收敛在 80% 附近
        double finalLux = ambientBaseLux - (loop.getCurrentOutput() / 100.0) * maxShadingContribution;
        assertTrue(stableCount >= 5, String.format("正作用控制未能在后半程保持稳定收敛，稳定周期数: %d/5", stableCount));
        assertTrue(Math.abs(finalLux - targetSP) <= config.getDeadBand(),
                String.format("遮阳正作用调控失败，最终照度未落在死区范围内！最终照度: %.1f lux, 目标: %.1f lux", finalLux, targetSP));
        assertTrue(Math.abs(loop.getCurrentOutput() - 80.0) <= 2.0,
                String.format("稳态遮阳帘开度偏离理论值(80%%)过大，当前最终开度: %.2f %%", loop.getCurrentOutput()));
    }

    // =========================================================================
    // 第三部分：工程鲁棒性防线 —— 抗饱和与频控节流
    // =========================================================================

    @Test
    @DisplayName("【防护 04·抗饱和阻断】不可控强光直射与执行器极限饱和阻断 (OUTPUT_SATURATED)")
    void testDirectStrongSunlight_SaturatedOutput() {
        // 场景：室外强阳光透入，底光暴涨到 600 lux，期望维持 435 lux
        // 灯具即使完全关闭到 0.0% 也无法消除外界超标光线（超出物理执行器可调范围）
        final double strongSunlight = 600.0;
        final double targetSP = 435.0;

        // 1. 业务配置：目标 435 lux，容差 ±15 lux，反作用调节
        StepConfig config = StepConfig.of(targetSP, 15.0, ActionDirection.REVERSE);

        // 2. 初始化控制器（假设初始灯光开在 20.0%）
        StepControlLoop loop = new StepControlLoop(config, 20.0);

        long simulatedTime = 10000L;
        StepResult result;
        int maxRounds = 10;
        int round = 0;

        // 3. 业务周期性下调逼近循环
        do {
            result = loop.compute(strongSunlight, simulatedTime);
            simulatedTime += 2000L;
            round++;
        } while (result.isNeedAction() && round < maxRounds);

        // 断言一: 在强光压制下，执行器开度已被安全步进压低至物理下限 0.0%
        assertEquals(0.0, loop.getCurrentOutput(), 1e-4, "强光压制下开度应最终被压低至 0.0%！");

        // 断言二: 当开度已在 0.0%，外界强光依旧时，验证抗饱和阻断 (停止发送无效下发指令，节约网络与硬件寿命)
        StepResult saturatedResult = loop.compute(strongSunlight, simulatedTime);
        assertFalse(saturatedResult.isNeedAction(), "已达最低物理极限，严禁向下游重复下发多余动作！");
        assertEquals(0.0, saturatedResult.getNewOutput(), 1e-4);
        assertEquals(0.0, saturatedResult.getDelta(), 1e-4);
        assertEquals(StepReason.OUTPUT_SATURATED, saturatedResult.getReason(), "必须返回 OUTPUT_SATURATED 饱和原因");
    }

    @Test
    @DisplayName("【防护 05·冷却防抖】高频上报与短时波动冷却拦截 (COOLDOWN_ACTIVE)")
    void testHighFrequencyReporting_CooldownThrottle() {
        // 1. 一行配置：目标 500 lux，容差 ±10 lux，冷却时间默认 1000ms
        StepConfig config = StepConfig.of(500.0, 10.0, ActionDirection.REVERSE);
        StepControlLoop loop = new StepControlLoop(config, 0.0);

        long baseTime = 10000L;

        // 动作一：首次上报照度 200 lux，正常执行动作
        StepResult res1 = loop.compute(200.0, baseTime);
        assertTrue(res1.isNeedAction(), "首次上报必须正常触发下发");
        assertEquals(StepReason.ADJUSTED_OK, res1.getReason());

        // 动作二：仅间隔 300ms 传感器再次上报数据 (处于 1000ms 冷却保护期内)
        StepResult res2 = loop.compute(210.0, baseTime + 300L);
        assertFalse(res2.isNeedAction(), "冷却期未到，必须拒绝下发动作指令");
        assertEquals(res1.getNewOutput(), res2.getNewOutput(), 1e-4, "跳过时输出必须维持原值");
        assertEquals(0.0, res2.getDelta(), 1e-4, "跳过时步进调节量必须为 0.0");
        assertEquals(StepReason.COOLDOWN_ACTIVE, res2.getReason(), "必须返回 COOLDOWN_ACTIVE 冷却原因");

        // 动作三：跨过 1000ms 冷却期后再次上报：恢复正常执行动作
        StepResult res3 = loop.compute(210.0, baseTime + 1200L);
        assertTrue(res3.isNeedAction(), "冷却期过后必须恢复正常执行");
    }

    // =========================================================================
    // 第四部分：高级调优 —— 专家级全参数手动整定模式
    // =========================================================================

    @Test
    @DisplayName("【专家 06·手动调参】环境照度多工况闭环调光控制验证 (全参数精准手动配置)")
    void testLightSensorContinuousRegulation_Optimized() {
        // 1. 物理环境模型构建:
        // 假设未受控的自然底色环境光 = 150 lux (若外界底光 >= SP，则超出可控物理域)
        final double ambientBaseLux = 150.0;
        final double maxBulbContribution = 3000.0;
        final double targetSP = 435.0; // 目标设定值 SP 为 435.0 lux

        // 物理可行性先验检查: 自然光不可大于目标值
        assertTrue(ambientBaseLux <= targetSP, "自然环境底光超出目标SP，灯具无法产生负向光强，系统物理不可达！");

        // 2. 控制器全参数专家配置:
        // 执行器理论增益: 3000 lux / 100% = 30.0 lux/1% 开度
        // 设置死区: 52.5 lux (容许约 ±1.75% 的灯光开度波动容差)
        StepConfig config = new StepConfig(
                targetSP,
                52.5,             // 死区放宽至合理的物理容差 (±52.5 lux)
                0.045,            // 增量比例增益 Kp
                30.0,             // 单次最大动作步长限制 30%
                0.0,              // 最小输出 0% (完全关闭)
                100.0,            // 最大输出 100% (全速运行)
                1000L,            // 最小动作冷却间隔 (1秒)
                0.0,
                // 发光设备天然是 REVERSE（环境越暗越要加力发光）
                ActionDirection.REVERSE
        );

        // 初始开度 0% (初始总照度 = 150 + 0 = 150 lux)
        StepControlLoop loop = new StepControlLoop(config, 0.0);

        System.out.println("\n=========================================================================================================");
        System.out.printf("          [IoT 闭环控制-专家手动] 强光调至目标照度 %.1f lux 仿真演化日志 (底光: %.1f lux)\n", targetSP, ambientBaseLux);
        System.out.println("=========================================================================================================");
        System.out.printf("%-4s | %-9s | %-10s | %-8s | %-11s | %-12s | %-10s | %-18s\n",
                "轮次", "仿真时间", "当前照度(PV)", "目标(SP)", "控制误差(e)", "输出亮度(CV)", "步进(Δ)", "决策动作 / 状态");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        long simulatedTime = 10000L;
        int stableCount = 0;
        int maxRounds = 10; // 扩展采样轮次以覆盖充分的稳态验证

        for (int round = 1; round <= maxRounds; round++) {
            // 物理环境模型计算当前实际照度
            double currentLux = ambientBaseLux + (loop.getCurrentOutput() / 100.0) * maxBulbContribution;

            // 闭环控制核心决策计算
            StepResult result = loop.compute(currentLux, simulatedTime);

            String timeStr = String.format("+%02ds", (round - 1) * 2);
            String actionDesc = formatActionDescription(result);

            System.out.printf("#%-5d | %-10s | %8.1f lux | %8.1f | %+10.1f | %10.2f %% | %+9.2f %% | %s\n",
                    round, timeStr, currentLux, config.getSetPoint(),
                    (config.getSetPoint() - currentLux),
                    loop.getCurrentOutput(),
                    result.getDelta(),
                    actionDesc
            );

            // 判定恒定状态: 命中死区 或 物理输出已达极限且误差已无法消除(抗饱和)
            if (result.getReason() == StepReason.WITHIN_DEAD_BAND || result.getReason() == StepReason.OUTPUT_SATURATED) {
                stableCount++;
            }

            simulatedTime += 2000L; // 模拟每 2 秒一个周期
        }
        System.out.println("=========================================================================================================\n");

        // 3. 算法精准性与稳态断言:
        double finalLux = ambientBaseLux + (loop.getCurrentOutput() / 100.0) * maxBulbContribution;
        assertTrue(stableCount >= 5, String.format("系统未能在后半程持续保持稳定，稳态周期数: %d/5", stableCount));
        assertTrue(Math.abs(finalLux - targetSP) <= config.getDeadBand(),
                String.format("最终照度未落在死区范围内！最终照度: %.1f lux, 目标: %.1f lux", finalLux, targetSP));
    }

    // =========================================================================
    // 辅助工具方法
    // =========================================================================

    private String formatActionDescription(StepResult result) {
        if (!result.isNeedAction()) {
            return "[维持] " + result.getReason().getDescription();
        }
        return String.format("[调节] Δ=%.2f%% -> 下发设备", result.getDelta());
    }
}