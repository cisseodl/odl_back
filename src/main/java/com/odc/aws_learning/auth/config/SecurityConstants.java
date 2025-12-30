package com.odc.aws_learning.auth.config;

public class SecurityConstants {
    public static final String AUTH_LOGIN_URL = "/auth/**";
    public static final String DOWNLOAD_URL = "/downloads/**";
    public static final String CONTACT_URL = "/contacts/send/**";
    public static final String CHECK_USER_URL = "/users/check/**";
    public static final String CONFIG_URL = "/configurations/get-config/**";



    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
