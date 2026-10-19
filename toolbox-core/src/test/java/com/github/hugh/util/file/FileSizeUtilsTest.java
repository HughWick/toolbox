package com.github.hugh.util.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FileSizeUtils 单元测试")
class FileSizeUtilsTest {

    @Nested
    @DisplayName("1. 基础大小与单位换算测试 (long fileSize)")
    class LongFileSizeTests {

        @ParameterizedTest(name = "字节数: {0} => 预期: {1}")
        @CsvSource({
                "-1, 0 B",
                "0, 0 B",
                "1, 1 B",
                "512, 512 B",
                "1023, 1023 B",
                "1024, 1 KB",
                "1536, 1.5 KB",
                "1048576, 1 MB",                     // 1024 * 1024
                "1073741824, 1 GB",                  // 1024 * 1024 * 1024
                "1099511627776, 1 TB",               // 1024^4
                "1125899906842624, 1 PB"             // 1024^5
        })
        @DisplayName("测试边界及各级别单位的基础格式化 (带单位)")
        void testFormatFileSizeWithLong(long bytes, String expected) {
            assertEquals(expected, FileSizeUtils.formatFileSize(bytes));
        }

        @Test
        @DisplayName("测试是否包含单位 includeUnit 参数")
        void testFormatFileSizeWithIncludeUnit() {
            // 包含单位
            assertEquals("1.5 KB", FileSizeUtils.formatFileSize(1536L, true));
            assertEquals("0 B", FileSizeUtils.formatFileSize(0L, true));

            // 不包含单位
            assertEquals("1.5", FileSizeUtils.formatFileSize(1536L, false));
            assertEquals("0", FileSizeUtils.formatFileSize(0L, false));
            assertEquals("0", FileSizeUtils.formatFileSize(-100L, false));
        }

        @Test
        @DisplayName("测试超出 PB 级别的超大文件（最大保留到 PB 级别）")
        void testFormatFileSizeExceedingPB() {
            // 2048 PB
            long largeBytes = 2048L * 1024L * 1024L * 1024L * 1024L * 1024L;
            assertEquals("2048 PB", FileSizeUtils.formatFileSize(largeBytes));
        }
    }

    @Nested
    @DisplayName("2. 带有源单位与浮点数的换算测试 (double size, SizeUnit unit)")
    class DoubleSizeWithUnitTests {

        @Test
        @DisplayName("测试浮点数及不同单位传入")
        void testFormatWithDifferentUnits() {
            // 1.5 MB 传进去直接换算
            assertEquals("1.5 MB", FileSizeUtils.formatFileSize(1.5, FileSizeUtils.SizeUnit.MB));
            // 2048 KB 自动进位到 2 MB
            assertEquals("2 MB", FileSizeUtils.formatFileSize(2048, FileSizeUtils.SizeUnit.KB));
            // 0.5 KB 换算为 512 B (四舍五入保留整数)
            assertEquals("512 B", FileSizeUtils.formatFileSize(0.5, FileSizeUtils.SizeUnit.KB));
        }

        @Test
        @DisplayName("测试浮点数小数位格式化 (DecimalFormat 最多2位小数)")
        void testDecimalRounding() {
            // 1024 + 100 字节 = 1.09765625 KB => 四舍五入保留两位为 1.1 KB
            assertEquals("1.1 KB", FileSizeUtils.formatFileSize(1124L));
            // 1024 + 250 字节 = 1.244140625 KB => 1.24 KB
            assertEquals("1.24 KB", FileSizeUtils.formatFileSize(1274L));
        }

        @Test
        @DisplayName("测试 unit 为 null 时的兜底处理（默认当作 B 处理）")
        void testNullUnitFallback() {
            assertEquals("1 KB", FileSizeUtils.formatFileSize(1024.0, null));
            assertEquals("500 B", FileSizeUtils.formatFileSize(500.0, null, true));
            assertEquals("500", FileSizeUtils.formatFileSize(500.0, null, false));
        }

        @Test
        @DisplayName("测试负数或0结合自定义单位")
        void testNegativeAndZeroWithUnit() {
            assertEquals("0 B", FileSizeUtils.formatFileSize(0, FileSizeUtils.SizeUnit.GB, true));
            assertEquals("0", FileSizeUtils.formatFileSize(-5.0, FileSizeUtils.SizeUnit.MB, false));
        }
    }

    @Nested
    @DisplayName("3. 基于 File 对象的测试 (File file)")
    class FileObjectTests {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("测试合法文件的大小格式化")
        void testValidFile() throws IOException {
            File testFile = tempDir.resolve("test-file.txt").toFile();
            // 写入 2048 字节 (2 KB)
            try (FileOutputStream fos = new FileOutputStream(testFile)) {
                fos.write(new byte[2048]);
            }

            assertEquals("2 KB", FileSizeUtils.formatFileSize(testFile));
        }

        @Test
        @DisplayName("测试空文件 (0 字节)")
        void testEmptyFile() throws IOException {
            File emptyFile = tempDir.resolve("empty.txt").toFile();
            boolean created = emptyFile.createNewFile();

            assertEquals("0 B", FileSizeUtils.formatFileSize(emptyFile));
        }

        @Test
        @DisplayName("测试传入目录、不存在的文件以及 null 对象，均应返回 -1")
        void testInvalidFiles() {
            // 1. null 对象
            assertEquals("-1", FileSizeUtils.formatFileSize((File) null));

            // 2. 不存在的文件
            File nonExistentFile = new File(tempDir.toFile(), "not_exists.txt");
            assertEquals("-1", FileSizeUtils.formatFileSize(nonExistentFile));

            // 3. 目标是目录而非文件
            File directory = tempDir.toFile();
            assertEquals("-1", FileSizeUtils.formatFileSize(directory));
        }
    }

    @Nested
    @DisplayName("4. 基于文件路径的测试 (String path)")
    class FilePathTests {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("测试合法文件路径")
        void testValidPath() throws IOException {
            File testFile = tempDir.resolve("path-test.txt").toFile();
            try (FileOutputStream fos = new FileOutputStream(testFile)) {
                fos.write(new byte[1024]); // 1 KB
            }

            assertEquals("1 KB", FileSizeUtils.formatFileSize(testFile.getAbsolutePath()));
        }

        @ParameterizedTest(name = "空路径测试用例: \"{0}\"")
        @NullAndEmptySource
        @ValueSource(strings = {"  ", "\t", "\n"})
        @DisplayName("测试 null、空字符串或纯空白路径，均应返回 -1")
        void testNullAndEmptyPath(String invalidPath) {
            assertEquals("-1", FileSizeUtils.formatFileSize(invalidPath));
        }

        @Test
        @DisplayName("测试不存在的文件路径，应返回 -1")
        void testNonExistentPath() {
            String nonExistentPath = tempDir.resolve("ghost_file.bin").toString();
            assertEquals("-1", FileSizeUtils.formatFileSize(nonExistentPath));
        }
    }
}
