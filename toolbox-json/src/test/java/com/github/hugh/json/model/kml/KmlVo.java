package com.github.hugh.json.model.kml;

import com.google.gson.annotations.SerializedName;
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
        @SerializedName("xmlns")
        private String xmlns;
        @SerializedName("xmlns:atom")
        private String xmlnsAtom;
        @SerializedName("Document")
        private DocumentDTO document;
        @SerializedName("xmlns:gx")
        private String xmlnsGx;

        @NoArgsConstructor
        @Data
        public static class DocumentDTO {
            @SerializedName("name")
            private String name;
            @SerializedName("Folder")
            private FolderDTO folder;

            @NoArgsConstructor
            @Data
            public static class FolderDTO {
                @SerializedName("name")
                private String name;
                @SerializedName("Folder")
                private List<FolderDTO2> folder;

                @NoArgsConstructor
                @Data
                public static class FolderDTO2 {
                    @SerializedName("Placemark")
                    private List<PlacemarkDTO> placemark;
                    @SerializedName("name")
                    private String name;
                    @SerializedName("Folder")
                    private List<FolderDTO3> folder;

                    @NoArgsConstructor
                    @Data
                    public static class PlacemarkDTO {
                        @SerializedName("OvCoordType")
                        private String ovCoordType;
                        @SerializedName("name")
                        private String name;
                        @SerializedName("Style")
                        private StyleDTO style;
                        @SerializedName("MultiGeometry")
                        private MultiGeometryDTO multiGeometry;
                        @SerializedName("Point")
                        private PointDTO point;

                        @NoArgsConstructor
                        @Data
                        public static class StyleDTO {
                            @SerializedName("LineStyle")
                            private LineStyleDTO lineStyle;

                            @NoArgsConstructor
                            @Data
                            public static class LineStyleDTO {
                                @SerializedName("color")
                                private String color;
                                @SerializedName("width")
                                private Integer width;
                            }
                        }

                        @NoArgsConstructor
                        @Data
                        public static class MultiGeometryDTO {
                            @SerializedName("LineString")
                            private List<LineStringDTO> lineString;

                            @NoArgsConstructor
                            @Data
                            public static class LineStringDTO {
                                @SerializedName("coordinates")
                                private String coordinates;
                            }
                        }

                        @NoArgsConstructor
                        @Data
                        public static class PointDTO {
                            @SerializedName("coordinates")
                            private String coordinates;
                        }
                    }

                    @NoArgsConstructor
                    @Data
                    public static class FolderDTO3 {
                        @SerializedName("Placemark")
                        private List<PlacemarkDTO2> placemark;
                        @SerializedName("name")
                        private String name;

                        @NoArgsConstructor
                        @Data
                        public static class PlacemarkDTO2 {
                            @SerializedName("OvCoordType")
                            private String ovCoordType;
                            @SerializedName("name")
                            private String name;
                            @SerializedName("Style")
                            private StyleDTO style;
                            @SerializedName("Polygon")
                            private PolygonDTO polygon;
                            @SerializedName("LineString")
                            private LineStringDTO lineString;
                            @SerializedName("OvStyle")
                            private OvStyleDTO ovStyle;

                            @NoArgsConstructor
                            @Data
                            public static class StyleDTO {
                                @SerializedName("LineStyle")
                                private LineStyleDTO lineStyle;
                                @SerializedName("PolyStyle")
                                private PolyStyleDTO polyStyle;

                                @NoArgsConstructor
                                @Data
                                public static class LineStyleDTO {
                                    @SerializedName("color")
                                    private String color;
                                    @SerializedName("width")
                                    private Integer width;
                                }

                                @NoArgsConstructor
                                @Data
                                public static class PolyStyleDTO {
                                    @SerializedName("color")
                                    private String color;
                                }
                            }

                            @NoArgsConstructor
                            @Data
                            public static class PolygonDTO {
                                @SerializedName("tessellate")
                                private Integer tessellate;
                                @SerializedName("outerBoundaryIs")
                                private OuterBoundaryIsDTO outerBoundaryIs;

                                @NoArgsConstructor
                                @Data
                                public static class OuterBoundaryIsDTO {
                                    @SerializedName("LinearRing")
                                    private LinearRingDTO linearRing;

                                    @NoArgsConstructor
                                    @Data
                                    public static class LinearRingDTO {
                                        @SerializedName("coordinates")
                                        private String coordinates;
                                    }
                                }
                            }

                            @NoArgsConstructor
                            @Data
                            public static class LineStringDTO {
                                @SerializedName("coordinates")
                                private String coordinates;
                            }

                            @NoArgsConstructor
                            @Data
                            public static class OvStyleDTO {
                                @SerializedName("TrackStyle")
                                private TrackStyleDTO trackStyle;

                                @NoArgsConstructor
                                @Data
                                public static class TrackStyleDTO {
                                    @SerializedName("width")
                                    private Integer width;
                                    @SerializedName("type")
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
