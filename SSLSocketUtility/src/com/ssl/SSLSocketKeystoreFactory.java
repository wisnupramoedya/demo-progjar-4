package com.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Util class to create SSLSocket using a KeyStore certificate to connect a server
 */
public class SSLSocketKeystoreFactory {

    private static String instance = "JKS";

    /**
     * A SSL algorithms types chooser enum
     */
    public static enum SecureType {
        SSL("SSL"),
        @Deprecated
        SSLv2("SSLv2"),
        SSLv3("SSLv3"),
        @Deprecated
        TLS("TLS"),
        @Deprecated
        TLSv1("TLSv1"),
        TLSv1_1("TLSv1.1"),
        TLSv1_2("TLSv1.2");

        private String type;

        private SecureType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Instantiate sslsocket
     *
     * @param ip               The IP to connect the socket to
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessource()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @param type             The SSL algorithm to use
     * @return The SSLSocket or null if the connection was not possible
     * @throws IOException              If the socket couldn't be created
     * @throws KeyManagementException   If the KeyManager couldn't be loaded
     * @throws CertificateException     If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException If the certificate is from an unknown type
     * @throws KeyStoreException        If your system is not compatible with JKS KeyStore certificates
     */
    public static SSLSocket getSocketWithCert(
            String ip, int port, String pathToCert,
            String passwordFromCert, SecureType type
    ) throws IOException, KeyManagementException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException
    {
        InetAddress ip2 = InetAddress.getByName(ip);
        if (ip2 == null) {
            throw new NullPointerException("The ip must be a correct IP !");
        }

        File f = new File(pathToCert);
        if (!f.exists()) {
            throw new NullPointerException("The specified path point to a non existing file !");
        }
        return getSocketWithCert(ip2, port, new FileInputStream(f), passwordFromCert, type);
    }

    /**
     * Instantiate sslsocket
     *
     * @param ip               The IP to connect the socket to
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessourceAsStream()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @param type             The SSL algorithm to use
     * @return The SSLSocket or null if the connection was not possible
     * @throws IOException              If the socket couldn't be created
     * @throws KeyManagementException   If the KeyManager couldn't be loaded
     * @throws CertificateException     If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException If the certificate is from an unknown type
     * @throws KeyStoreException        If your system is not compatible with JKS KeyStore certificates
     */
    public static SSLSocket getSocketWithCert(
            String ip, int port, InputStream pathToCert,
            String passwordFromCert, SecureType type
    ) throws IOException, KeyManagementException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException
    {
        InetAddress ip2 = InetAddress.getByName(ip);
        if (ip2 == null) {
            throw new NullPointerException("The ip must be a correct IP !");
        }
        return getSocketWithCert(ip2, port, pathToCert, passwordFromCert, type);
    }

    /**
     * Instantiate sslsocket
     *
     * @param ip               The IP to connect the socket to
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessource()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @param type             The SSL algorithm to use
     * @return The SSLSocket or null if the connection was not possible
     * @throws IOException              If the socket couldn't be created
     * @throws KeyManagementException   If the KeyManager couldn't be loaded
     * @throws CertificateException     If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException If the certificate is from an unknown type
     * @throws KeyStoreException        If your system is not compatible with JKS KeyStore certificates
     */
    public static SSLSocket getSocketWithCert(
            InetAddress ip, int port, String pathToCert,
            String passwordFromCert, SecureType type
    ) throws IOException, KeyManagementException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException
    {
        File f = new File(pathToCert);
        if (!f.exists()) {
            throw new NullPointerException("The specified path point to a non existing file !");
        }
        return getSocketWithCert(ip, port, new FileInputStream(f), passwordFromCert, type);
    }

    /**
     * Instantiate sslsocket
     *
     * @param ip               The IP to connect the socket to
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessourceAsStream()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @param type             The SSL algorithm to use
     * @return The SSLSocket or null if the connection was not possible
     * @throws IOException              If the socket couldn't be created
     * @throws KeyManagementException   If the KeyManager couldn't be loaded
     * @throws CertificateException     If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException If the certificate is from an unknown type
     * @throws KeyStoreException        If your system is not compatible with JKS KeyStore certificates
     */
    public static SSLSocket getSocketWithCert(
            InetAddress ip, int port, InputStream pathToCert,
            String passwordFromCert, SecureType type
    ) throws IOException, KeyManagementException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException
    {
        X509TrustManager[] tmm;
        KeyStore ks = KeyStore.getInstance(instance);

        ks.load(pathToCert, passwordFromCert.toCharArray());
        tmm = tm(ks);

        SSLContext ctx = SSLContext.getInstance(type.getType());
        ctx.init(null, tmm, null);

        SSLSocketFactory SocketFactory = ctx.getSocketFactory();
        return (SSLSocket) SocketFactory.createSocket(ip, port);
    }

    /**
     * Instantiate sslsocket (beta proxy)
     *
     * @param ip               The IP to connect the socket to
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessourceAsStream()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @param type             The SSL algorithm to use
     * @return The SSLSocket or null if the connection was not possible
     * @throws IOException              If the socket couldn't be created
     * @throws KeyManagementException   If the KeyManager couldn't be loaded
     * @throws CertificateException     If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException If the certificate is from an unknown type
     * @throws KeyStoreException        If your system is not compatible with JKS KeyStore certificates
     */
    public static SSLSocket getSocketWithCert(
            InetAddress ip, int port, InputStream pathToCert,
            String passwordFromCert, SecureType type, Proxy proxy
    ) throws IOException, KeyManagementException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException
    {
        X509TrustManager[] tmm;
        KeyStore ks = KeyStore.getInstance(instance);

        ks.load(pathToCert, passwordFromCert.toCharArray());
        tmm = tm(ks);

        SSLContext ctx = SSLContext.getInstance(type.getType());
        ctx.init(null, tmm, null);

        SSLSocketFactory SocketFactory = ctx.getSocketFactory();
        Socket proxy_s = new Socket(proxy);

        return (SSLSocket) SocketFactory.createSocket(proxy_s, ip.getHostAddress(), port, true);
    }

    /**
     * Util class to get the X509TrustManager
     *
     * @param keystore
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    private static X509TrustManager[] tm(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustMgrFactory.init(keystore);

        TrustManager trustManagers[] = trustMgrFactory.getTrustManagers();
        for (int i = 0; i < trustManagers.length; i++) {
            if (trustManagers[i] instanceof X509TrustManager) {
                X509TrustManager[] tr = new X509TrustManager[1];
                tr[0] = (X509TrustManager) trustManagers[i];
                return tr;
            }
        }
        return null;
    }
}
