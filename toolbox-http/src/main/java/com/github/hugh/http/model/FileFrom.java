package com.github.hugh.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;

import java.io.File;

/**
 * 文件来源类，表示包含一组文件媒体信息。
 *
 * @author hugh
 * @since 2.5.1
 */
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileFrom {

    // 文件上传表单字段名称（对应请求参数名）
    private String key;

    // 文件在表单中的名称（对应 filename 属性）
    private String name;

    // 文件路径（本地文件系统中的路径）
    private String path;

    // 文件 与 文件路径必须传参一个
    private File file;

    // 文件类型（MIME 类型）
    private MediaType fileMediaType;
}
