package com.github.hugh.util.io;

import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.file.FileUtils;
import com.github.hugh.util.system.OsUtils;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * stream 流处理工具类
 *
 * @author AS
 * @date 2020/10/26 15:11
 */
class StreamTest {

    @Test
    void testToFile() throws IOException {
        String image1 = "/file/image/69956256_p1.jpg";
        String path = StreamTest.class.getResource(image1).getPath();
//        String path2 = StreamTest.class.getResource("/").getPath();
        InputStream inputStream = StreamUtils.getInputStream(path);
        String outFilePath;
        if (OsUtils.isWindows()) {
            outFilePath = "D:\\";
        } else {
            outFilePath = "./";
        }
        outFilePath += "test.jpg";
        StreamUtils.toFile(inputStream, outFilePath);
        assertTrue(new File(outFilePath).exists());
        FileUtils.delFile(outFilePath);
        assertFalse(new File(outFilePath).exists());
    }

    //  测试文件转字节
    @Test
    void testToByteArray() throws IOException {
//        String path = "D:\\private\\toolbox-2.4.X\\toolbox-core\\src\\test\\resources\\file\\image\\Teresa.png";
        String image1 = "/file/image/Teresa.png";
        String path = StreamTest.class.getResource(image1).getPath();
        File file = new File(path);
        ByteSource byteSource = Files.asByteSource(file);
        byte[] read = byteSource.read();
        byte[] bytes = Files.toByteArray(file);
        assertEquals(read.length, bytes.length);
        byte[] bytes1 = StreamUtils.resourceToByteArray(path);
        assertEquals(bytes.length, bytes1.length);
        assertArrayEquals(bytes, bytes1);
    }


    @Test
    void testCloneInputStream() throws IOException {
        String image1 = "/file/image/Teresa.png";
        String path = StreamTest.class.getResource(image1).getPath();
//        File file = new File(path);
        InputStream inputStream = StreamUtils.getInputStream(path);
        InputStream inputStream2 = StreamUtils.getInputStream(path);
        assertArrayEquals(inputStream.readAllBytes(), inputStream2.readAllBytes());
        final InputStream cloneInputStream1 = StreamUtils.cloneInputStream(inputStream);
        assertNotEquals(inputStream2.hashCode(), inputStream.hashCode());
//        assertEquals(inputStream2.hashCode(), inputStream3.hashCode());
        assertArrayEquals(inputStream.readAllBytes(), cloneInputStream1.readAllBytes());
    }

    @Test
    void testToString_withValidInput_returnsCorrectString() {
        String inputString = "Hello, World!";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        String result = StreamUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        assertEquals(inputString, result);
    }

    @Test
    void testToString_withEmptyInput_returnsEmptyString() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String result = StreamUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        assertEquals("", result);
    }

    @Test
    void testToString_withDifferentCharset_returnsCorrectString() {
        String inputString = "你好，世界！";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_16));
        String result = StreamUtils.toString(inputStream, StandardCharsets.UTF_16.name());
        assertEquals(inputString, result);
    }

    @Test
    void testToString_withPartialRead() {
        String inputString = "This is a longer string to test partial reads.";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        String result = StreamUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        assertEquals(inputString, result);
    }

    @Test
    void testToString_withExceptionInInputStream_throwsToolboxException() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated IOException");
            }
        };
        ToolboxException thrown = assertThrows(ToolboxException.class, () -> {
            StreamUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        });
        assertInstanceOf(IOException.class, thrown.getCause());
        assertEquals("Simulated IOException", thrown.getCause().getMessage());
    }

    @Test
    void testToString_withNullCharset_throwsIllegalArgumentException() {
        String inputString = "Test string";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        assertThrows(NullPointerException.class, () -> StreamUtils.toString(inputStream, null));
    }

    @Test
    void testToString_inputStreamCloses() {
        String inputString = "Test string";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        StreamUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        assertEquals(-1, inputStream.read()); // Reading after close should return -1
    }


    @Test
    void testToString_inputStreamOnly_withValidInput_returnsCorrectString() {
        String inputString = "Hello, Default UTF-8!";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(StandardCharsets.UTF_8));
        String result = StreamUtils.toString(inputStream);
        assertEquals(inputString, result);
    }

    @Test
    void toInputStream_withNonEmptyString_returnsCorrectInputStream() throws IOException {
        String inputString = "Convert this string";
        InputStream inputStream = StreamUtils.toInputStream(inputString);
        assertNotNull(inputStream);
        byte[] expectedBytes = inputString.getBytes();
        byte[] actualBytes = inputStream.readAllBytes();
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    void toInputStream_withEmptyString_returnsCorrectInputStream() throws IOException {
        String inputString = "";
        InputStream inputStream = StreamUtils.toInputStream(inputString);
        assertNotNull(inputStream);
        assertEquals(0, inputStream.available()); // Empty stream should have 0 available bytes
        assertEquals(-1, inputStream.read()); // Reading from an empty stream should return -1
    }

    @Test
    void testCloseWithIOException() throws IOException {
        // 创建一个模拟的 Closeable 对象，其 close() 方法抛出 IOException
        Closeable mockCloseable = mock(Closeable.class);
        IOException expectedException = new IOException("Simulated IOException");
        doThrow(expectedException).when(mockCloseable).close();

        // 调用 close 方法并期望抛出 ToolboxException
        ToolboxException thrownException = assertThrows(ToolboxException.class, () -> StreamUtils.close(mockCloseable));

        // 验证 mockCloseable 的 close() 方法被调用了一次
        verify(mockCloseable, times(1)).close();

        // 验证 ToolboxException 的 cause 是预期的 IOException
        assertEquals(expectedException, thrownException.getCause());
    }

    @Test
    void testCloseWithNullCloseable() {
        // 调用 close 方法并传入 null
        StreamUtils.close(null);
        // 对于 null 的 Closeable，try-with-resources 不会抛出异常，也不会调用 close() 方法
        // 因此这里不需要验证任何方法调用，只需要确保没有异常抛出
    }

    @Test
    void testRead_SuccessAndClose() throws IOException {
        String expected = "Hello, World!";
        StringReader reader = new StringReader(expected);
        String actual = StreamUtils.read(reader, true);
        assertEquals(expected, actual);
        // Verify that the reader was closed
        assertThrows(IOException.class, reader::read, "Reader should be closed");
    }

//    @Test
//    void testRead_SuccessNoClose() throws IOException {
//        String expected = "Another test string";
//        StringReader reader = new StringReader(expected);
//        String actual = StreamUtils.read(reader, false);
//        assertEquals(expected, actual);
//        // Verify that the reader is still open
//        assertDoesNotThrow(reader::read, "Reader should not be closed");
//        reader.close(); // Close it manually for cleanup
//    }

    @Test
    void testRead_EmptyInput() throws IOException {
        String expected = "";
        StringReader reader = new StringReader(expected);
        String actual = StreamUtils.read(reader, true);
        assertEquals(expected, actual);
        assertThrows(IOException.class, reader::read, "Reader should be closed");
    }

    @Test
    void testRead_IOException() throws IOException {
        Reader mockReader = Mockito.mock(Reader.class);
        when(mockReader.read(any(CharBuffer.class))).thenThrow(new IOException("Simulated IOException"));

        IOException thrownException = assertThrows(IOException.class, () -> StreamUtils.read(mockReader, true));
        assertEquals("Simulated IOException", thrownException.getMessage());
        // Verify that close was called even after the exception
        try {
            verify(mockReader, times(1)).close();
        } catch (Throwable t) {
            fail("Close method should have been called", t);
        }
    }

    @Test
    void testRead_IOException_NoClose() throws IOException {
        Reader mockReader = Mockito.mock(Reader.class);
        when(mockReader.read(any(CharBuffer.class))).thenThrow(new IOException("Simulated IOException"));

        IOException thrownException = assertThrows(IOException.class, () -> StreamUtils.read(mockReader, false));
        assertEquals("Simulated IOException", thrownException.getMessage());
        // Verify that close was NOT called because isClose is false
        verify(mockReader, never()).close();
    }

    @Test
    void testRead_ReaderReturnsMinusOneImmediately() throws IOException {
        Reader mockReader = Mockito.mock(Reader.class);
        when(mockReader.read(any(CharBuffer.class))).thenReturn(-1);

        String actual = StreamUtils.read(mockReader, true);
        assertEquals("", actual);
        // Verify that close was called
        try {
            verify(mockReader, times(1)).close();
        } catch (Throwable t) {
            fail("Close method should have been called", t);
        }
    }

    @Test
    void testRead_ReaderReturnsMinusOneImmediately_NoClose() throws IOException {
        Reader mockReader = Mockito.mock(Reader.class);
        when(mockReader.read(any(CharBuffer.class))).thenReturn(-1);

        String actual = StreamUtils.read(mockReader, false);
        assertEquals("", actual);
        // Verify that close was not called
        verify(mockReader, never()).close();
    }


    private static final String TEST_FILE_PATH = "/file/img.gitconfig";

    @Test
    void testGetInputStream_ValidURL() throws IOException {
        String path = StreamTest.class.getResource(TEST_FILE_PATH).getPath();
        String url = new File(path).toURI().toURL().toString();
        InputStream inputStream = StreamUtils.getInputStream(url);
        assertNotNull(inputStream);
        inputStream.close();
    }

    @Test
    void testGetInputStream_MalformedURLException_FileNotFound() {
        String nonExistentFile = "non_existent_file.txt";
        assertThrows(ToolboxException.class, () -> StreamUtils.getInputStream(nonExistentFile));
    }
}
