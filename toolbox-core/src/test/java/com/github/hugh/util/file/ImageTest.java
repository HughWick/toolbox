package com.github.hugh.util.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 图片工具测试类
 *
 * @author AS
 * @date 2020/9/17 15:53
 */
class ImageTest {

    @Test
    void testIsImage() {
        String image1 = "/file/image/69956256_p1.jpg";
        String path = ImageTest.class.getResource(image1).getPath();
        assertTrue(ImageUtils.isImage(path));
        String image2 = "/file/image/20200718234953_grmzy.jpeg";
        String path2 = ImageTest.class.getResource(image2).getPath();
        assertTrue(ImageUtils.isImage(path2));
        String image3 = "/file/image/Teresa.png";
        String path3 = ImageTest.class.getResource(image3).getPath();
        assertTrue(ImageUtils.isImage(path3));
        String image4 = "/file/image/webp/share_572031b53d646c2c8a8191bdd93a95b2.png";
        String path4 = ImageTest.class.getResource(image4).getPath();
        assertFalse(ImageUtils.isImage(path4));
    }
}
