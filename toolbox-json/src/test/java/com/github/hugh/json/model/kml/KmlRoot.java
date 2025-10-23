package com.github.hugh.json.model.kml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "kml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KmlRoot {

    // 映射 <kml> 标签下的 <Document> 标签
    @JacksonXmlProperty(localName = "Document")
    private KmlDocument document;

}
