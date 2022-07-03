package com.sooft_sales.model;

import java.io.Serializable;
import java.util.List;

public class UserModel extends StatusResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private User user;
        private String access_token;
        private String token_type;
        private String expires_in;
        private Setting setting;
        private List<String> permissions;

        public User getUser() {
            return user;
        }

        public Setting getSetting() {
            return setting;
        }

        public String getAccess_token() {
            return access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public class User implements Serializable {
            private int id;
            private String name;
            private String user_name;
            private String email_verified_at;
            private String photo;
            private String lang;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getUser_name() {
                return user_name;
            }

            public String getEmail_verified_at() {
                return email_verified_at;
            }

            public String getPhoto() {
                return photo;
            }

            public String getLang() {
                return lang;
            }
        }

        public class Setting implements Serializable {
            private String id;
            private String vat;
            private String name_ar;
            private String name_en;
            private String address_ar;
            private String address_en;
            private String commercial_number;
            private String logo;

            public String getId() {
                return id;
            }

            public String getVat() {
                return vat;
            }

            public String getName_ar() {
                return name_ar;
            }

            public String getName_en() {
                return name_en;
            }

            public String getAddress_ar() {
                return address_ar;
            }

            public String getAddress_en() {
                return address_en;
            }

            public String getCommercial_number() {
                return commercial_number;
            }

            public String getLogo() {
                return logo;
            }
        }
    }

}
