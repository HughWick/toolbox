package com.github.hugh.http.constant;

import okhttp3.MediaType;

/**
 * 定义常用的媒体类型和对应的值。
 */
public class MediaTypes {

    /**
     * Atom XML 类型。
     */
    public static final MediaType APPLICATION_ATOM_XML = MediaType.parse("application/atom+xml");

    /**
     * 表单 URL 编码类型。
     */
    public static final MediaType APPLICATION_FORM_URLENCODED = MediaType.parse("application/x-www-form-urlencoded");

    /**
     * 表单 URL 编码类型。字符集为utf-8。
     */
    public static final MediaType APPLICATION_FORM_URLENCODED_UTF8 = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");

    /**
     * JSON 类型，采用 UTF-8 字符集编码。
     */
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.parse("application/json;charset=UTF-8");

    /**
     * 二进制流类型。
     */
    public static final MediaType APPLICATION_OCTET_STREAM = MediaType.parse("application/octet-stream");

    /**
     * PDF 类型。
     */
    public static final MediaType APPLICATION_PDF = MediaType.parse("application/pdf");

    /**
     * RSS XML 类型。
     */
    public static final MediaType APPLICATION_RSS_XML = MediaType.parse("application/rss+xml");

    /**
     * XHTML XML 类型。
     */
    public static final MediaType APPLICATION_XHTML_XML = MediaType.parse("application/xhtml+xml");

    /**
     * XML 类型。
     */
    public static final MediaType APPLICATION_XML = MediaType.parse("application/xml");

    /**
     * GIF 图片类型。
     */
    public static final MediaType IMAGE_GIF = MediaType.parse("image/gif");

    /**
     * JPEG 图片类型。
     */
    public static final MediaType IMAGE_JPEG = MediaType.parse("image/jpeg");

    /**
     * PNG 图片类型。
     */
    public static final MediaType IMAGE_PNG = MediaType.parse("image/png");

    /**
     * multipart/form-data 类型，用于文件上传等场景。
     */
    public static final MediaType MULTIPART_FORM_DATA = MediaType.parse("multipart/form-data");

    /**
     * HTML 类型。
     */
    public static final MediaType TEXT_HTML = MediaType.parse("text/html");

    /**
     * Markdown 类型。
     */
    public static final MediaType TEXT_MARKDOWN = MediaType.parse("text/markdown");

    /**
     * 纯文本类型。
     */
    public static final MediaType TEXT_PLAIN = MediaType.parse("text/plain");

    /**
     * XML 类型。
     */
    public static final MediaType TEXT_XML = MediaType.parse("text/xml");

}