package com.github.hugh.mongodb;

import com.mongodb.client.MongoClient;
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
    protected static MongoClient client;
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
        client = MongoClients.create(connectionString);
        mongoTemplate = new MongoTemplate(client, TEST_DB_NAME);
        System.out.println("嵌入式 MongoDB (v7.x) 已启动: " + connectionString);
    }

    @AfterAll
    static void stopEmbeddedMongo() {
        // 第一步：先关闭 Java 客户端连接
        // 这样驱动的后台线程就会停止工作，不会再去 ping 数据库了
        if (client != null) {
            client.close();
            System.out.println("MongoDB Client 已关闭");
        }
        // 第二步：再关闭嵌入式数据库进程
        if (running != null) {
            running.close();
            System.out.println("嵌入式 MongoDB 进程已关闭");
        }
    }
}
