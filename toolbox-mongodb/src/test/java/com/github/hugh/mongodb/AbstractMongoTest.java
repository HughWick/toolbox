package com.github.hugh.mongodb;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.TransitionWalker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractMongoTest {

    // 4.x 版本使用 TransitionWalker 来控制进程
    private static TransitionWalker.ReachedState<RunningMongodProcess> running;

    protected static MongoTemplate mongoTemplate;
    protected static final String TEST_DB_NAME = "test_embedded_db";
    protected static final String COLLECTION_NAME = "collection_test";

    @BeforeAll
    static void startEmbeddedMongo() {
        // 1. 使用新的 API 启动 Mongo (会自动选择空闲端口)
        running = Mongod.instance().start(Version.Main.V7_0);
        // 2. 获取运行时的地址信息
        var serverAddress = running.current().getServerAddress();
        String ip = serverAddress.getHost();
        int port = serverAddress.getPort();
        // 3. 创建连接字符串
        String connectionString = String.format("mongodb://%s:%d/%s", ip, port, TEST_DB_NAME);
        // 4. 初始化 MongoTemplate
        mongoTemplate = new MongoTemplate(MongoClients.create(connectionString), TEST_DB_NAME);
        System.out.println("嵌入式 MongoDB (v4.x) 已启动: " + connectionString);
    }

    @AfterAll
    static void stopEmbeddedMongo() {
        if (running != null) {
            running.close(); // 关闭进程
            System.out.println("嵌入式 MongoDB 已关闭");
        }
    }
}
