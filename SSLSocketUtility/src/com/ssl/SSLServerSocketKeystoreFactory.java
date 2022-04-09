package com.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

/**
 * Util class to create SSLServerSocket using a KeyStore certificate to connect a server
 */
public class SSLServerSocketKeystoreFactory {

    private static String instance = "JKS";

    /**
     * A SSL algorithms types chooser enum
     *
     * @author gpotter2
     */
    public static enum ServerSecureType {
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

        private ServerSecureType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessource()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @return The SSLServerSocket or null if the connection was not possible
     * @throws IOException               If the socket couldn't be created
     * @throws KeyManagementException    If the KeyManager couldn't be loaded
     * @throws CertificateException      If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException  If the certificate is from an unknown type
     * @throws KeyStoreException         If your system is not compatible with JKS KeyStore certificates
     * @throws UnrecoverableKeyException Cannot get the keys of the KeyStore
     */
    public static SSLServerSocket getServerSocketWithCert(
            int port, String pathToCert, String passwordFromCert, ServerSecureType type
    ) throws
            IOException, KeyManagementException, NoSuchAlgorithmException,
            CertificateException, KeyStoreException, UnrecoverableKeyException
    {
        File f = new File(pathToCert);
        if (!f.exists()) {
            throw new NullPointerException("The specified path point to a non existing file !");
        }
        return getServerSocketWithCert(port, new FileInputStream(f), passwordFromCert, type);
    }

    /**
     * @param port             The port of the socket
     * @param pathToCert       The path to the KeyStore cert (can be with getClass().getRessourceAsStream()....)
     * @param passwordFromCert The password of the KeyStore cert
     * @return The SSLServerSocket or null if the connection was not possible
     * @throws IOException               If the socket couldn't be created
     * @throws KeyManagementException    If the KeyManager couldn't be loaded
     * @throws CertificateException      If the certificate is not correct (null or damaged) or the password is incorrect
     * @throws NoSuchAlgorithmException  If the certificate is from an unknown type
     * @throws KeyStoreException         If your system is not compatible with JKS KeyStore certificates
     * @throws UnrecoverableKeyException Cannot get the keys of the KeyStore
     */
    public static SSLServerSocket getServerSocketWithCert(
            int port, InputStream pathToCert,
            String passwordFromCert, ServerSecureType type
    ) throws IOException, KeyManagementException, NoSuchAlgorithmException,
            CertificateException, KeyStoreException, UnrecoverableKeyException
    {
        X509TrustManager[] tmm;
        X509KeyManager[] kmm;

        KeyStore ks = KeyStore.getInstance(instance);
        ks.load(pathToCert, passwordFromCert.toCharArray());
        tmm = getTrustManager(ks);
        kmm = getKeyManager(ks, passwordFromCert);

        SSLContext ctx = SSLContext.getInstance(type.getType());
        ctx.init(kmm, tmm, null);

        SSLServerSocketFactory socketFactory = ctx.getServerSocketFactory();
        return (SSLServerSocket) socketFactory.createServerSocket(port);
    }

    /**
     * Util class to get the X509TrustManager
     *
     * @param keystore
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    private static X509TrustManager[] getTrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
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

    /**
     * Util class to get the X509KeyManager
     *
     * @param keystore
     * @param password
     * @return
     */
    private static X509KeyManager[] getKeyManager(KeyStore keystore, String password)
            throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
    {
        KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyMgrFactory.init(keystore, password.toCharArray());

        KeyManager keyManagers[] = keyMgrFactory.getKeyManagers();

        for (int i = 0; i < keyManagers.length; i++) {
            if (keyManagers[i] instanceof X509KeyManager) {
                X509KeyManager[] kr = new X509KeyManager[1];
                kr[0] = (X509KeyManager) keyManagers[i];
                return kr;
            }
        }
        return null;
    }
}
