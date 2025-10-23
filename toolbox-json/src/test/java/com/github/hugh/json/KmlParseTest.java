package com.github.hugh.json;

import cn.idev.excel.FastExcel;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.hugh.bean.dto.coordinates.GpsDTO;
import com.github.hugh.json.model.kml.KmlPlacemark;
import com.github.hugh.json.model.kml.KmlRoot;
import com.github.hugh.json.model.kml.PlacemarkExcelDto;
import com.github.hugh.util.CoordinatesUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KmlParseTest {

    @Test
    void testFileKml() {
        String image1 = "/kml/test_01.kml";
        File fileDir1 = new File(getPath(image1));
        List<KmlPlacemark> placemarkList = parse2(fileDir1);
        for (KmlPlacemark p : placemarkList) {
            System.out.println(p);
        }
        System.out.println("--- 步骤1：解析完成，共 " + placemarkList.size() + " 条数据 ---");
        // 2. 转换 -> 将 XML 实体列表 转换为 Excel DTO 列表
        List<PlacemarkExcelDto> excelDtoList = placemarkList.stream()
                .map(p -> {
                    // 安全地获取坐标
                    Double lon = (p.getPoint() != null) ? p.getPoint().getLongitude() : null;
                    Double lat = (p.getPoint() != null) ? p.getPoint().getLatitude() : null;
                    GpsDTO gpsDTO = CoordinatesUtils.wgs84ToGcj02(lon, lat);
                    return new PlacemarkExcelDto(p.getName(), p.getDescription(), lon, lat, gpsDTO.getLongitude(), gpsDTO.getLatitude());
                }).collect(Collectors.toList());
        System.out.println("--- 步骤2：转换完成，准备导出 ---");
        // 3. 导出 -> 将 Excel DTO 列表写入文件
        String outputPath = "target/kml_export_final.xlsx";
        exportWithIdevexcel(excelDtoList, outputPath);
    }

    /**
     * 最终的、正确的 KML 解析方法
     */
    public List<KmlPlacemark> parse2(File kmlFile) {
        // 1. 创建一个 XmlMapper 实例
        XmlMapper xmlMapper = new XmlMapper();
        try {
            KmlRoot kmlRoot = xmlMapper.readValue(kmlFile, KmlRoot.class);
            if (kmlRoot != null && kmlRoot.getDocument() != null) {
                return kmlRoot.getDocument().getPlacemarks();
            } else {
                System.out.println("警告：解析成功，但未能找到 Placemark 列表。请检查 KML 文件内的 Document > Folder 结构是否正确。");
            }
        } catch (IOException e) {
            // 如果 Jackson 在这里抛出异常，说明 XML 文件本身存在严重的语法错误
            System.err.println("【严重错误】Jackson 解析 XML 文件失败！请检查文件格式。");
            e.printStackTrace();
        }
        // 如果发生错误或未找到数据，返回空列表
        return Collections.emptyList();
    }

    public static String getPath(String fileName) {
        return KmlParseTest.class.getResource(fileName).getPath();
    }

    /**
     * 使用 cn.idev.excel:fastexcel 将 DTO 列表导出到 Excel。
     *
     * @param excelDtoList 包含 Excel 数据的 DTO 列表
     * @param outputPath   Excel 文件的输出路径
     * @throws IOException 如果文件写入失败
     */
    public void exportWithIdevexcel(List<PlacemarkExcelDto> excelDtoList, String outputPath) {
        FastExcel.write("D:\\demo1.xlsx", PlacemarkExcelDto.class)
                .sheet("MySheetName") // Optionally specify sheet name
                .doWrite(excelDtoList);
        System.out.println("成功使用 [cn.idev.excel] 导出 " + excelDtoList.size() + " 条数据到: " + outputPath);
    }
}
