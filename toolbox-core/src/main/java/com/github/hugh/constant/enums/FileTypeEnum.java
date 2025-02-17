package com.github.hugh.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 文件类型枚举
 * <p>
 * 注意：txt文档没有文件头
 * </p>
 * <ul>
 *  <li>
 * 该枚举命名方式都为长度为3的后缀字母，如：
 * </li>
 * <li>
 * jpg=jpg; jpe; jpeg
 * </li>
 *  <li>
 * tif=tiff,tif
 * </li>
 * </ul>
 *
 * @author hugh
 * @since 2.4.6
 */
@ToString
@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    /**
     * JPG
     * <p>
     * jpg,jpeg,gpe都为统一个文件十六进制开头
     * </p>
     */
    JPG("FFD8FF"),

    /**
     * PNG.
     */
    PNG("89504E47"),

    /**
     * 高效率图像文件格式
     *
     * @since 2.4.7
     */
    HEIF("0000001C66747970"),

    /**
     * WEBP
     */
    WEBP("52494646"),

    /**
     * GIF.
     */
    GIF("47494638"),

    /**
     * TIFF.
     */
    TIF("49492A00"),

    /**
     * Windows Bitmap.
     */
    BMP("424D"),

    /**
     * CAD.
     */
    DWG("41433130"),

    /**
     * Adobe Photoshop.
     */
    PSD("38425053"),

    /**
     * Rich Text Format.
     */
    RTF("7B5C727466"),

    /**
     * XML.
     */
    XML("3C3F786D6C"),

    /**
     * HTML.
     */
    HTML("68746D6C3E"),

    /**
     * CSS.
     */
    CSS("48544D4C207B0D0A0942"),

    /**
     * JS.
     */
    JS("696B2E71623D696B2E71"),

    /**
     * Email [thorough only].
     */
    EML("44656C69766572792D646174653A"),

    /**
     * Outlook Express.
     */
    DBX("CFAD12FEC5FD746F"),

    /**
     * Outlook (pst).
     */
    PST("2142444E"),

    /**
     * MS Word/Excel. 2003之前前缀都是一样的
     */
    XLS_DOC("D0CF11E0"),

    /**
     * 2007 之后excel后缀
     */
    XLSX("504B03041400000008"),

    /**
     * 2007 之后word文档后缀
     */
    DOCX("504B0304140006"),

    /**
     * Visio
     */
    VSD("d0cf11e0a1b11ae10000"),

    /**
     * MS Access.
     */
    MDB("5374616E64617264204A"),

    /**
     * WPS文字wps、表格et、演示dps都是一样的
     */
    WPS("d0cf11e0a1b11ae10000"),

    /**
     * torrent
     */
    TORRENT("6431303A637265617465"),

    /**
     * WordPerfect.
     */
    WPD("FF575043"),

    /**
     * Postscript.
     */
    EPS("252150532D41646F6265"),

    /**
     * Adobe Acrobat.
     */
    PDF("255044462D312E"),

    /**
     * Quicken.
     */
    QDF("AC9EBD8F"),

    /**
     * Windows Password.
     */
    PWL("E3828596"),

    /**
     * ZIP Archive.
     */
    ZIP("504B030414"),

    /**
     * RAR Archive.
     */
    RAR("52617221"),

    /**
     * JSP Archive.
     */
    JSP("3C2540207061676520"),

    /**
     * JAVA Archive.
     */
    JAVA("7061636B61676520"),

    /**
     * CLASS Archive.
     */
    CLASS("CAFEBABE0000002E00"),

    /**
     * JAR Archive.
     */
    JAR("504B03040A000000"),

    /**
     * MF Archive.
     */
    MF("4D616E69666573742D56"),

    /**
     * EXE Archive.
     */
    EXE("4D5A9000030000000400"),

    /**
     * CHM Archive.
     */
    CHM("49545346030000006000"),
//    INI("235468697320636F6E66"),
//    SQL("494E5345525420494E54"),
//    BAT("406563686F206f66660D"),
//    GZ("1F8B0800000000000000"),
//    PROPERTIES("6C6F67346A2E726F6F74"),
//    MXP("04000000010000001300"),

    /**
     * Wave.
     */
    WAV("57415645"),

    /**
     * AVI.
     */
    AVI("41564920"),

    /**
     * Real Audio.
     */
    RAM("2E7261FD"),

    /**
     * Real Media.
     */
    RM("2E524D46"),

    /**
     * MPEG (mpg).
     */
    MPG("000001BA"),

    /**
     * Quicktime.
     */
    MOV("6D6F6F76"),

    /**
     * Windows Media.
     */
    ASF("3026B2758E66CF11"),

    /**
     * MIDI.
     */
    MID("4D546864"),

    /**
     * m4a（Apple Lossless Audio Codec file）、m4v（QuickTime M4A/M4V file）
     */
    M4A("0000002066747970"),

    /**
     * MP4  MPEG-4 video files
     */
    MP4("0000001866747970"),

    /**
     * MP3.
     */
    MP3("4944330300"),

    /**
     * FLV.
     */
    FLV("464C5601050000000900");

    private final String value;
}
