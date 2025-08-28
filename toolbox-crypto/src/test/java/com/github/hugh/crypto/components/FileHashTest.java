package com.github.hugh.crypto.components;

import com.github.hugh.constant.EncryptCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileHashTest {
    private File testFile;
    private String testFilePath;
    private String bin_file_path;
    private File bin_file_01;
    @TempDir // JUnit 5 注解，提供一个临时目录，测试结束后自动删除
    Path tempDir;
    private static final String TEST_CONTENT = "Hello, world! This is a test file for hashing.";
    private static final String EMPTY_CONTENT = "";

    @BeforeEach
    void setUp() throws IOException {
        // 在每个测试方法执行前创建测试文件
        testFile = tempDir.resolve("test_file.txt").toFile();
        bin_file_path = FileHashTest.class.getResource("/bin/host_standrd-1.2.0_240724_RELEASE.bin").getFile();
        bin_file_01 = new File(bin_file_path);
        testFilePath = testFile.getAbsolutePath();
        Files.write(testFile.toPath(), TEST_CONTENT.getBytes());
    }

    @Test
    void testFileMd5() throws IOException, NoSuchAlgorithmException {
        String ip2DbPath = FileHashTest.class.getResource("/logo.png").getFile();
        Assertions.assertNotNull(ip2DbPath);
        File file = new File(ip2DbPath);
        String md5Result = "559e0c4bd56eb91202e60b398b8c556d";
        assertEquals(md5Result, FileHash.getMD5(file));
        assertEquals(md5Result, FileHash.getMD5(ip2DbPath));
        String md5Result2 = "bc934fee1449b524feaf3f688a26e301";
        assertEquals(md5Result2, FileHash.getMD5(bin_file_01));
    }

    @Test
    void testFileBin() throws IOException, NoSuchAlgorithmException {
        String ip2DbPath = FileHashTest.class.getResource("/host_display_gd32.bin").getFile();
        Assertions.assertNotNull(ip2DbPath);
        File file = new File(ip2DbPath);
        String md5Result = "018f36293f3c4ed46674e19e87f731f4";
        assertEquals(md5Result, FileHash.getMD5(file));
    }

    @Test
    @DisplayName("getHash: 文件存在且算法有效 - SHA-256")
    void testGetHash_ExistingFile_SHA256() throws IOException, NoSuchAlgorithmException {
        String expectedHash = FileHash.getSHA256(testFile);
        String actualHash = FileHash.getHash(testFile, EncryptCode.SHA_256);
        assertEquals(expectedHash, actualHash);
        String sha255Result = "e60cd4c196606c108836c248589aed286fcd163dddc4e45ff77db76c390b6841";
        assertEquals(sha255Result, FileHash.getSHA256(bin_file_01));
//        String sha255Result2 = "e60cd4c196606c108836c248589aed286fcd163dddc4e45ff77db76c390b6841";
        assertEquals(sha255Result, FileHash.getSHA256(bin_file_path));
    }

    @Test
    @DisplayName("getHash: 文件为null时抛出IllegalArgumentException")
    void testGetHash_NullFile_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            FileHash.getHash(null, EncryptCode.MD5);
        });
        assertEquals("文件不能为null。", thrown.getMessage());
        IllegalArgumentException thrown2 = assertThrows(IllegalArgumentException.class, () -> {
            FileHash.getMD5((String) null);
        });
        assertEquals("文件路径不能为null或空。", thrown2.getMessage());

        IllegalArgumentException thrown3 = assertThrows(IllegalArgumentException.class, () -> {
            FileHash.getSHA256((String) null);
        });
        assertEquals("文件路径不能为null或空。", thrown3.getMessage());
    }

    @Test
    @DisplayName("getHash: 文件不存在时抛出IllegalArgumentException")
    void testGetHash_NonExistentFile_ThrowsException() {
        File nonExistentFile = tempDir.resolve("non_existent.txt").toFile();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            FileHash.getHash(nonExistentFile, EncryptCode.MD5);
        });
        assertTrue(thrown.getMessage().startsWith("文件不存在:"));
    }

    @Test
    @DisplayName("getHash: 算法名称为null时抛出IllegalArgumentException")
    void testGetHash_NullAlgorithm_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            FileHash.getHash(testFile, null);
        });
        assertEquals("哈希算法名称不能为null或空。", thrown.getMessage());
    }

    @Test
    @DisplayName("getHash: 算法名称为空字符串时抛出IllegalArgumentException")
    void testGetHash_EmptyAlgorithm_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            FileHash.getHash(testFile, "");
        });
        assertEquals("哈希算法名称不能为null或空。", thrown.getMessage());
    }

    @Test
    @DisplayName("getHash: 算法名称为无效值时抛出NoSuchAlgorithmException")
    void testGetHash_InvalidAlgorithm_ThrowsException() {
        assertThrows(NoSuchAlgorithmException.class, () -> {
            FileHash.getHash(testFile, "INVALID_ALGORITHM");
        });
    }

//    @Test
//    @DisplayName("getHash: 对空文件进行哈希计算")
//    void testGetHash_EmptyFile() throws IOException, NoSuchAlgorithmException {
//        File emptyFile = tempDir.resolve("empty.txt").toFile();
//        Files.write(emptyFile.toPath(), EMPTY_CONTENT.getBytes()); // 确保文件是空的
//
//        String expectedMd5Hash = FileHash.getHash(EMPTY_CONTENT.getBytes(), EncryptCode.MD5);
//        String actualMd5Hash = FileHash.getHash(emptyFile, EncryptCode.MD5);
//        assertEquals(expectedMd5Hash, actualMd5Hash);
//
//        String expectedSha256Hash = FileHash.getHash(EMPTY_CONTENT.getBytes(), EncryptCode.SHA_256);
//        String actualSha256Hash = FileHash.getHash(emptyFile, EncryptCode.SHA_256);
//        assertEquals(expectedSha256Hash, actualSha256Hash);
//    }
}
