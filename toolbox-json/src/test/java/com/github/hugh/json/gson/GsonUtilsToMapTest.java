package com.github.hugh.json.gson;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GsonUtilsToMapTest {
    private static final int NUM_THREADS = 10; // 并发线程数
    private static final int NUM_ITERATIONS_PER_THREAD = 5000; // 每个线程执行次数
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
    }

    @AfterEach
    void tearDown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testToMapPerformanceWithJsonObject() throws InterruptedException {
        // 准备一个复杂的 JsonObject 作为输入
        JsonObject complexJsonObject = createComplexJsonObject(5, 5); // 5层嵌套，每层5个键值对
        System.out.println("--- Starting performance test with JsonObject ---");
        long startTime = System.nanoTime();
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicLong successCount = new AtomicLong(0);
        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < NUM_ITERATIONS_PER_THREAD; j++) {
                        Map<String, String> result = GsonUtils.toMap(complexJsonObject, String.class, String.class);
                        assertNotNull(result);
                        assertTrue(result.containsKey("key_0_0")); // 简单断言，确保结果正确
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.err.println("Error in thread: " + Thread.currentThread().getName() + " - " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 等待所有线程完成
        long endTime = System.nanoTime();
        long totalExecutionTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

        System.out.println("Total threads: " + NUM_THREADS);
        System.out.println("Iterations per thread: " + NUM_ITERATIONS_PER_THREAD);
        System.out.println("Total operations: " + (NUM_THREADS * NUM_ITERATIONS_PER_THREAD));
        System.out.println("Successful operations: " + successCount.get());
        System.out.println("Total execution time: " + totalExecutionTimeMillis + " ms");
        System.out.println("Average time per operation: " + (double) totalExecutionTimeMillis / (NUM_THREADS * NUM_ITERATIONS_PER_THREAD) + " ms");
        System.out.println("Throughput: " + (double) (NUM_THREADS * NUM_ITERATIONS_PER_THREAD) * 1000 / totalExecutionTimeMillis + " operations/sec");
        System.out.println("--- Performance test finished ---");
    }

    @Test
    void testToMapPerformanceWithPojo() throws InterruptedException {
        // 准备一个复杂的 POJO 作为输入
        TestPojo complexPojo = createComplexPojo(5, 5);

        System.out.println("--- Starting performance test with POJO ---");
        long startTime = System.nanoTime();
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicLong successCount = new AtomicLong(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < NUM_ITERATIONS_PER_THREAD; j++) {
                        Map<String, String> result = GsonUtils.toMap(complexPojo, String.class, String.class);
                        assertNotNull(result);
                        assertTrue(result.containsKey("field_0_0")); // 简单断言
                        successCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    System.err.println("Error in thread: " + Thread.currentThread().getName() + " - " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 等待所有线程完成
        long endTime = System.nanoTime();
        long totalExecutionTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

        System.out.println("Total threads: " + NUM_THREADS);
        System.out.println("Iterations per thread: " + NUM_ITERATIONS_PER_THREAD);
        System.out.println("Total operations: " + (NUM_THREADS * NUM_ITERATIONS_PER_THREAD));
        System.out.println("Successful operations: " + successCount.get());
        System.out.println("Total execution time: " + totalExecutionTimeMillis + " ms");
        System.out.println("Average time per operation: " + (double) totalExecutionTimeMillis / (NUM_THREADS * NUM_ITERATIONS_PER_THREAD) + " ms");
        System.out.println("Throughput: " + (double) (NUM_THREADS * NUM_ITERATIONS_PER_THREAD) * 1000 / totalExecutionTimeMillis + " operations/sec");
        System.out.println("--- Performance test finished ---");
    }

    // 辅助方法：创建复杂的 JsonObject
    private JsonObject createComplexJsonObject(int depth, int numKeysPerLevel) {
        JsonObject root = new JsonObject();
        createNestedJsonObject(root, depth, numKeysPerLevel, 0);
        return root;
    }

    private void createNestedJsonObject(JsonObject current, int maxDepth, int numKeysPerLevel, int currentDepth) {
        if (currentDepth >= maxDepth) {
            return;
        }
        for (int i = 0; i < numKeysPerLevel; i++) {
            current.addProperty("key_" + currentDepth + "_" + i, "value_" + currentDepth + "_" + i);
            if (currentDepth + 1 < maxDepth) {
                JsonObject nested = new JsonObject();
                current.add("nested_" + currentDepth + "_" + i, nested);
                createNestedJsonObject(nested, maxDepth, numKeysPerLevel, currentDepth + 1);
            }
        }
    }

    // 辅助方法：创建复杂的 POJO
    private TestPojo createComplexPojo(int depth, int numFieldsPerLevel) {
        TestPojo root = new TestPojo();
        createNestedPojo(root, depth, numFieldsPerLevel, 0);
        return root;
    }

    private void createNestedPojo(TestPojo current, int maxDepth, int numFieldsPerLevel, int currentDepth) {
        if (currentDepth >= maxDepth) {
            return;
        }

        for (int i = 0; i < numFieldsPerLevel; i++) {
            // 尝试设置 String 类型的字段
            String stringFieldName = "field_" + currentDepth + "_" + i;
            try {
                java.lang.reflect.Field stringField = current.getClass().getDeclaredField(stringFieldName);
                if (stringField.getType() == String.class) { // 检查字段类型
                    stringField.set(current, "value_" + currentDepth + "_" + i);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 如果是其他类型（如嵌套POJO），则继续尝试设置
                // 或者在这里捕获到没有对应 String 字段的情况
            }

            // 尝试设置嵌套 POJO 类型的字段
            if (currentDepth + 1 < maxDepth) {
                String nestedFieldName = "nested_" + currentDepth + "_" + i;
                try {
                    java.lang.reflect.Field nestedField = current.getClass().getDeclaredField(nestedFieldName);
                    if (nestedField.getType() == TestPojo.class) { // 检查字段类型
                        TestPojo nested = new TestPojo();
                        nestedField.set(current, nested);
                        createNestedPojo(nested, maxDepth, numFieldsPerLevel, currentDepth + 1);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // 如果没有对应嵌套 POJO 字段，则忽略
                }
            }
        }
    }

    // 用于测试的简单 POJO
    public static class TestPojo {
        public String field_0_0;
        public String field_0_1;
        public String field_0_2;
        public String field_0_3;
        public String field_0_4;
        public TestPojo nested_0_0;
        public TestPojo nested_0_1;
        public TestPojo nested_0_2;
        public TestPojo nested_0_3;
        public TestPojo nested_0_4;

        // 可以在这里添加更多字段和嵌套 POJO
        // 实际生成时需要动态生成字段
        public TestPojo field_1_0;
        public TestPojo field_1_1;
        // ...以此类推
    }
}
