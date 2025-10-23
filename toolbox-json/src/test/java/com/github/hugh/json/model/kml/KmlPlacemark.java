package com.github.hugh.json.model.kml;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "Placemark")
public class KmlPlacemark {
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;
    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "description")
    private String description;
    @JacksonXmlProperty(localName = "Point")
    private Point point;

    // Getters and Setters...

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        private double longitude;
        private double latitude;
        private double altitude;
        public Point() {
        }
        public Point(double longitude, double latitude, double altitude) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.altitude = altitude;
        }
        // ... Getters and Setters for Point
        @JacksonXmlProperty(localName = "coordinates")
        private void unpackCoordinates(String coords) {
            if (coords == null || coords.trim().isEmpty()) {
                return;
            }

            // 3. 在方法内部，我们手动进行分割和赋值
            String[] parts = coords.split(",");
            try {
                if (parts.length >= 1) {
                    this.longitude = Double.parseDouble(parts[0].trim());
                }
                if (parts.length >= 2) {
                    this.latitude = Double.parseDouble(parts[1].trim());
                }
                if (parts.length >= 3) {
                    this.altitude = Double.parseDouble(parts[2].trim());
                }
            } catch (NumberFormatException e) {
                System.err.println("警告：无法解析坐标字符串: " + coords);
                // 在实际项目中，这里应该使用日志框架
            }
        }
    }
}


