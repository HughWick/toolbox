package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepLightLoopTest {

    @Test
    @DisplayName("核心验证: 环境照度多工况闭环调光控制验证 (含不可控强光抑制)")
    void testLightSensorContinuousRegulation_Optimized() {
        // 1. 物理环境模型构建:
        // 假设未受控的自然底色环境光 = 150 lux (若外界光 >= SP，则超出可控物理域)
        final double ambientBaseLux = 150.0;
        final double maxBulbContribution = 3000;
        final double targetSP = 435; // 目标设定值 SP 改为 850.0 lux
        // 物理可行性先验检查: 自然光不可大于目标值
        assertTrue(ambientBaseLux <= targetSP, "自然环境底光超出目标SP，灯具无法产生负向光强，系统物理不可达！");
        // 2. 控制器参数配置:
        // 目标 SP = 850.0 lux
        // 执行器灵敏度: 4850 lux / 100% = 48.5 lux/1%。
        // 推荐死区: 设置为 25.0 lux (允许在 825 ~ 875 lux 之间稳定，相当于约 ±0.5% 灯光开度容差)
        StepConfig config = new StepConfig(
                targetSP,
                52.5,             // 死区放宽至合理的物理容差 (±25 lux)
                0.045,            // 增益增量 Kp
                30.0,             // 单次最大步长限制 20%
                0.0,   // 最小输出 0% (关闭制冷)
                100.0,         // 最大输出 100% (全速制冷)
                1000L,         // 最小动作间隔 (1秒)
                // 发光设备天然是 REVERSE（越暗越要加力）
                ActionDirection.REVERSE // 照度越高越需要调暗
        );
        // 初始开度 100% (初始总照度 = 150 + 4850 = 5000 lux)
        StepControlLoop loop = new StepControlLoop(config, 0);
        System.out.println("\n=========================================================================================================");
        System.out.printf("               [IoT 闭环控制] 强光调至目标照度 %.1f lux 仿真演化日志 (底光: %.1f lux)\n", targetSP, ambientBaseLux);
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
            if (result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                stableCount++;
            } else if (result.getReason() == StepReason.OUTPUT_SATURATED) {
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

    private String formatActionDescription(StepResult result) {
        if (!result.isNeedAction()) {
            return "[维持] " + result.getReason().getDescription();
        }
        return String.format("[调节] Δ=%.2f%% -> 下发设备", result.getDelta());
    }


    @Test
    @DisplayName("核心验证: 智能遮阳帘正作用闭环调光控制验证 (DIRECT模式)")
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
                // 遮光设备天然是 DIRECT（越亮越要加力挡光）
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

    @Test
    @DisplayName("测试一: 极简调光闭环 (开发者零算法配置，自学习无超调逼近)")
    void testSimpleDimming_AutoTuning() {
        // 1. 物理环境真实特征 (模拟实际场景，算法自身完全无感知)
        final double ambientBaseLux = 150.0;       // 环境自然底光 150 lux
        final double maxBulbContribution = 3000.0; // 灯具最大光强 3000 lux (真实增益 30 lux/%)
        final double targetSP = 535.0;             // 期望维持的目标 435 lux
        // =========================================================================
        // 2. 开发者极简调用 (只需关注业务规则：目标 435 lux，容差 ±15 lux，天黑加力)
        // =========================================================================
        StepConfig config = StepConfig.of(targetSP, 15.0, ActionDirection.REVERSE);
        // 初始化闭环控制器，初始输出开度 0.0%
        StepControlLoop loop = new StepControlLoop(config, 0.0);
        System.out.println("\n=========================================================================================================");
        System.out.printf("   [极简闭环控制] 目标: %.1f lux | 底光: %.1f lux | 物理增益: 30.0 lux/%% | 业务死区: ±%.1f lux\n",
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
            // 开发者日常调用只有这一行：把当前值喂给控制器
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

    @Test
    @DisplayName("测试二: 不可控外界强光直射抑制与执行器下限饱和阻断 (Saturation)")
    void testDirectStrongSunlight_SaturatedOutput() {
        // 场景：室外强阳光透入，底光暴涨到 600 lux，期望维持 435 lux
        // 灯具即使完全关闭到 0.0% 也无法消除外界超标光线
        final double strongSunlight = 600.0;
        final double targetSP = 435.0;
        // 1. 一行配置
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

        // 断言二: 当开度已在 0.0%，外界强光依旧时，验证抗饱和阻断 (停止发送无效下发指令)
        StepResult saturatedResult = loop.compute(strongSunlight, simulatedTime);
        assertFalse(saturatedResult.isNeedAction(), "已达最低物理极限，严禁向下游重复下发多余动作！");
        assertEquals(0.0, saturatedResult.getNewOutput(), 1e-4);
        assertEquals(0.0, saturatedResult.getDelta(), 1e-4);
        assertEquals(StepReason.OUTPUT_SATURATED, saturatedResult.getReason(), "必须返回 OUTPUT_SATURATED 饱和原因");
    }

    @Test
    @DisplayName("测试三: 高频连续上报的动作冷却防护 (Cooldown 防抖拦截)")
    void testHighFrequencyReporting_CooldownThrottle() {
        // 1. 一行配置：目标 500 lux，容差 ±10 lux，冷却时间默认 1000ms
        StepConfig config = StepConfig.of(500.0, 10.0, ActionDirection.REVERSE);
        StepControlLoop loop = new StepControlLoop(config, 0.0);

        long baseTime = 10000L;

        // 首次上报：照度 200 lux，正常执行动作
        StepResult res1 = loop.compute(200.0, baseTime);
        assertTrue(res1.isNeedAction(), "首次上报必须正常触发下发");
        assertEquals(StepReason.ADJUSTED_OK, res1.getReason());

        // 仅间隔 300ms 传感器再次上报数据 (在 1000ms 冷却保护期内)
        StepResult res2 = loop.compute(210.0, baseTime + 300L);
        assertFalse(res2.isNeedAction(), "冷却期未到，必须拒绝下发动作指令");
        assertEquals(res1.getNewOutput(), res2.getNewOutput(), 1e-4, "跳过时输出必须维持原值");
        assertEquals(0.0, res2.getDelta(), 1e-4, "跳过时步进调节量必须为 0.0");
        assertEquals(StepReason.COOLDOWN_ACTIVE, res2.getReason(), "必须返回 COOLDOWN_ACTIVE 冷却原因");

        // 跨过 1000ms 冷却期后再次上报：恢复正常执行动作
        StepResult res3 = loop.compute(210.0, baseTime + 1200L);
        assertTrue(res3.isNeedAction(), "冷却期过后必须恢复正常执行");
    }
}
