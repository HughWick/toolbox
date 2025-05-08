package com.github.hugh.json.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UsersJsonBo {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private AddressDTO address;
    private String phone;
    private String website;
    private CompanyDTO company;

    @NoArgsConstructor
    @Data
    public static class AddressDTO {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private GeoDTO geo;

        @NoArgsConstructor
        @Data
        public static class GeoDTO {
            private String lat;
            private String lng;
        }
    }

    @NoArgsConstructor
    @Data
    public static class CompanyDTO {
        private String name;
        private String catchPhrase;
        @JsonProperty("bs")
        private String bs;
    }
}
