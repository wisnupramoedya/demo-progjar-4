package com.ww;

public class Utils {
    public static String HOST = "localhost";
    public static int PORT = 6969;

    private static String PRIVATE_CERT_PATH    = "/CLE_PRIVATE.jks";
    public static String PRIVATE_CERT_PASSWORD = "PASSWORD1";

    private static String PUBLIC_CERT_PATH    = "/CLE_PUBLIC.jks";
    public static String PUBLIC_CERT_PASSWORD = "PASSWORD2";

    public static String getPrivateCertPath() {
        return System.getProperty("user.dir") + PRIVATE_CERT_PATH;
    }

    public static String getPublicCertPath() {
        return System.getProperty("user.dir") + PUBLIC_CERT_PATH;
    }
}
