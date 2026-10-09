package com.github.hugh.components.stepcontrol;

import com.github.hugh.components.stepcontrol.domain.StepConfig;
import com.github.hugh.components.stepcontrol.domain.StepResult;
import com.github.hugh.components.stepcontrol.enums.ActionDirection;
import com.github.hugh.components.stepcontrol.enums.StepReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StepMqReceiverTest {

    // 1. DTO 与状态存储模型 (模拟 MQ 消息与 Redis 缓存)

    /**
     * 模拟 MQ 消息体 (上行遥测数据包)
     */
    public static class SensorTelemetryMqMessage {
        private final String deviceId;
        private final double reportedLux; // 每次上报的照度 (每次不同)
        private final long timestamp;

        public SensorTelemetryMqMessage(String deviceId, double reportedLux, long timestamp) {
            this.deviceId = deviceId;
            this.reportedLux = reportedLux;
            this.timestamp = timestamp;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public double getReportedLux() {
            return reportedLux;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    /**
     * 模拟外部集中式存储 (如 Redis/DB)，维护设备当前持久化状态
     */
    public static class DeviceStateRepository {
        // key: deviceId, value: 当前保存的设备实际开度(CV)
        private final Map<String, Double> deviceOutputStore = new ConcurrentHashMap<>();

        public double loadCurrentOutput(String deviceId) {
            return deviceOutputStore.getOrDefault(deviceId, 0.0); // 初始开度 0.0%
        }

        public void saveCurrentOutput(String deviceId, double output) {
            deviceOutputStore.put(deviceId, output);
        }
    }

    // 模拟无状态 MQ 消费者服务
    public static class MqTelemetryConsumer {
        private final DeviceStateRepository repository;
        private final StepConfig config; // 目标一致的通用控制策略 (如目标 1435 lux)

        public MqTelemetryConsumer(DeviceStateRepository repository, StepConfig config) {
            this.repository = repository;
            this.config = config;
        }

        /**
         * 模拟 MQ 消息监听器: 每次接收一条独立消息触发一次处理
         */
        public StepResult onMessageReceived(SensorTelemetryMqMessage message) {
            String deviceId = message.getDeviceId();
            double reportedLux = message.getReportedLux();
            long timestamp = message.getTimestamp();
            // 1. [核心点] 不依赖长期持有的单例对象，而是从缓存/数据库恢复上次保存的开度状态
            double lastSavedOutput = repository.loadCurrentOutput(deviceId);
            // 2. [按需创建/还原控制器] 每次基于历史状态快照重新构建控制器实例
            StepControlLoop loop = new StepControlLoop(config, lastSavedOutput);
            // 3. 执行单次闭环算法计算
            StepResult result = loop.compute(reportedLux, timestamp);
            // 4. 状态持久化: 将计算后的最新开度写回 Redis/DB，供下一次消费使用
            repository.saveCurrentOutput(deviceId, loop.getCurrentOutput());
            // 5. 打印消费及控制演化日志
            double sp = config.getSetPoint();
            double error = sp - reportedLux;
            String actionDesc;
            if (result.isNeedAction()) {
                actionDesc = String.format("下发新开度 -> %.2f%% (步进 %+6.2f%%)", loop.getCurrentOutput(), result.getDelta());
            } else {
                actionDesc = String.format("无需下发 [%s]", result.getReason());
            }
            System.out.printf("[MQ消费] 设备: %s | 读数: %6.1f lux | 目标SP: %6.1f | 偏差: %+7.1f | 开度: %5.2f%% -> %5.2f%% | %s\n",
                    deviceId, reportedLux, sp, error, lastSavedOutput, loop.getCurrentOutput(), actionDesc);
            return result;
        }
    }

    // 3. 单元测试: 模拟不同 MQ 消息驱动下的闭环演化
    @Test
    @DisplayName("MQ驱动模式验证: 每次消息恢复状态重构控制器，不同数据逐步收敛至一致目标")
    void testMqDrivenDiscreteEvolution() {
        // 1. 目标一致的业务配置 (目标 1435 lux)
        final double targetSP = 1435.0;
        final double deadBand = 12.5;
        StepConfig config = new StepConfig(
                targetSP,
                deadBand,
                0.04,                  // Kp
                30.0,                   // 单次最大允许变动步长限制
                0.0,
                100.0,
                1000L,
                ActionDirection.REVERSE
        );
        DeviceStateRepository stateRepo = new DeviceStateRepository();
        MqTelemetryConsumer consumer = new MqTelemetryConsumer(stateRepo, config);
        // 模拟物理环境: 自然底光 150 lux, 满载贡献 3000 lux
        final double ambientBaseLux = 150.0;
        final double maxBulbContribution = 3000.0;
        final String deviceId = "LIGHT_ZONE_EAST_01";

        System.out.println("======================================================================================================================");
        System.out.printf("          [MQ消息队列驱动控制验证] 一致目标: %.1f lux | 死区: ±%.1f lux | 底光: %.1f lux\n",
                targetSP, deadBand, ambientBaseLux);
        System.out.println("======================================================================================================================");
        long currentTime = 10000L;
        int stableCount = 0;
        int messageCount = 12; // 模拟 12 条独立的 MQ 消息到达
        for (int i = 1; i <= messageCount; i++) {
            // A. [物理世界变化] 每次上报前，根据存储中当前灯具开度计算本次不同的照度数据
            double currentOutput = stateRepo.loadCurrentOutput(deviceId);
            double currentLux = ambientBaseLux + (currentOutput / 100.0) * maxBulbContribution;
            // B. [生成独立的 MQ 消息] 模拟传感器通过 TCP 上报后由网关投递到 MQ 的独立数据包
            SensorTelemetryMqMessage mqMessage = new SensorTelemetryMqMessage(deviceId, currentLux, currentTime);
            // C. [MQ 消费者消费] 传入单条消息，内部现场重构 UniversalControlLoop 进行计算与持久化
            StepResult result = consumer.onMessageReceived(mqMessage);
            if (!result.isNeedAction() && result.getReason() == StepReason.WITHIN_DEAD_BAND) {
                stableCount++;
            }
            // 模拟下一次消息间隔 2 秒
            currentTime += 2000L;
        }
        System.out.println("======================================================================================================================");
        // 验证系统最终收敛状态
        double finalOutput = stateRepo.loadCurrentOutput(deviceId);
        double finalLux = ambientBaseLux + (finalOutput / 100.0) * maxBulbContribution;
        System.out.printf("【验证结论】目标照度: %.1f lux | 最终照度: %.1f lux | 最终持久化开度: %.2f%%\n",
                targetSP, finalLux, finalOutput);

        assertTrue(stableCount >= 4, "经过多条 MQ 消息演化后未能持续稳定在死区内");
        assertTrue(Math.abs(finalLux - targetSP) <= deadBand,
                String.format("最终照度未落在死区内！实际: %.1f, 目标: %.1f", finalLux, targetSP));
    }
}
