package com.github.hugh.json.model.kml;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class KmlVo {

    private KmlDTO kml;

    @NoArgsConstructor
    @Data
    public static class KmlDTO {
        @JSONField(name = "xmlns")
        private String xmlns;
        @JSONField(name = "xmlns:atom")
        private String xmlnsAtom;
        @JSONField(name = "Document")
        private DocumentDTO document;
        @JSONField(name = "xmlns:gx")
        private String xmlnsGx;

        @NoArgsConstructor
        @Data
        public static class DocumentDTO {
            @JSONField(name = "name")
            private String name;
            @JSONField(name = "Folder")
            private FolderDTO folder;

            @NoArgsConstructor
            @Data
            public static class FolderDTO {
                @JSONField(name ="name")
                private String name;
                @JSONField(name ="Folder")
                private List<FolderDTO2> folder;

                @NoArgsConstructor
                @Data
                public static class FolderDTO2 {
                    @JSONField(name ="Placemark")
                    private List<PlacemarkDTO> placemark;
                    @JSONField(name ="name")
                    private String name;
                    @JSONField(name ="Folder")
                    private List<FolderDTO3> folder;

                    @NoArgsConstructor
                    @Data
                    public static class PlacemarkDTO {
                        @JSONField(name ="OvCoordType")
                        private String ovCoordType;
                        @JSONField(name ="name")
                        private String name;
                        @JSONField(name ="Style")
                        private StyleDTO style;
                        @JSONField(name ="MultiGeometry")
                        private MultiGeometryDTO multiGeometry;
                        @JSONField(name ="Point")
                        private PointDTO point;

                        @NoArgsConstructor
                        @Data
                        public static class StyleDTO {
                            @JSONField(name ="LineStyle")
                            private LineStyleDTO lineStyle;

                            @NoArgsConstructor
                            @Data
                            public static class LineStyleDTO {
                                @JSONField(name ="color")
                                private String color;
                                @JSONField(name ="width")
                                private Integer width;
                            }
                        }

                        @NoArgsConstructor
                        @Data
                        public static class MultiGeometryDTO {
                            @JSONField(name ="LineString")
                            private List<LineStringDTO> lineString;

                            @NoArgsConstructor
                            @Data
                            public static class LineStringDTO {
                                @JSONField(name ="coordinates")
                                private String coordinates;
                            }
                        }

                        @NoArgsConstructor
                        @Data
                        public static class PointDTO {
                            @JSONField(name ="coordinates")
                            private String coordinates;
                        }
                    }

                    @NoArgsConstructor
                    @Data
                    public static class FolderDTO3 {
                        @JSONField(name ="Placemark")
                        private List<PlacemarkDTO2> placemark;
                        @JSONField(name ="name")
                        private String name;

                        @NoArgsConstructor
                        @Data
                        public static class PlacemarkDTO2 {
                            @JSONField(name ="OvCoordType")
                            private String ovCoordType;
                            @JSONField(name ="name")
                            private String name;
                            @JSONField(name ="Style")
                            private StyleDTO style;
                            @JSONField(name ="Polygon")
                            private PolygonDTO polygon;
                            @JSONField(name ="LineString")
                            private LineStringDTO lineString;
                            @JSONField(name ="OvStyle")
                            private OvStyleDTO ovStyle;

                            @NoArgsConstructor
                            @Data
                            public static class StyleDTO {
                                @JSONField(name ="LineStyle")
                                private LineStyleDTO lineStyle;
                                @JSONField(name ="PolyStyle")
                                private PolyStyleDTO polyStyle;

                                @NoArgsConstructor
                                @Data
                                public static class LineStyleDTO {
                                    @JSONField(name ="color")
                                    private String color;
                                    @JSONField(name ="width")
                                    private Integer width;
                                }

                                @NoArgsConstructor
                                @Data
                                public static class PolyStyleDTO {
                                    @JSONField(name ="color")
                                    private String color;
                                }
                            }

                            @NoArgsConstructor
                            @Data
                            public static class PolygonDTO {
                                @JSONField(name ="tessellate")
                                private Integer tessellate;
                                @JSONField(name ="outerBoundaryIs")
                                private OuterBoundaryIsDTO outerBoundaryIs;

                                @NoArgsConstructor
                                @Data
                                public static class OuterBoundaryIsDTO {
                                    @JSONField(name ="LinearRing")
                                    private LinearRingDTO linearRing;

                                    @NoArgsConstructor
                                    @Data
                                    public static class LinearRingDTO {
                                        @JSONField(name ="coordinates")
                                        private String coordinates;
                                    }
                                }
                            }

                            @NoArgsConstructor
                            @Data
                            public static class LineStringDTO {
                                @JSONField(name ="coordinates")
                                private String coordinates;
                            }

                            @NoArgsConstructor
                            @Data
                            public static class OvStyleDTO {
                                @JSONField(name ="TrackStyle")
                                private TrackStyleDTO trackStyle;

                                @NoArgsConstructor
                                @Data
                                public static class TrackStyleDTO {
                                    @JSONField(name ="width")
                                    private Integer width;
                                    @JSONField(name ="type")
                                    private Integer type;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
