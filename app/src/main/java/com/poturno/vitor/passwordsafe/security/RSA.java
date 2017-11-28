package com.poturno.vitor.passwordsafe.security;

import com.poturno.vitor.passwordsafe.helper.Base64Custom;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by vitor on 28/11/2017.
 */

public class RSA {

    private static final int RSAKEYSIZE = 1024;
    private static final String CIPHER_TYPE = "RSA";

    public static KeyPair generateKeyPar() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(CIPHER_TYPE);
        kpg.initialize (new RSAKeyGenParameterSpec(RSAKEYSIZE, RSAKeyGenParameterSpec.F4));
        return kpg.generateKeyPair();
    }

    public static byte[] encode(PrivateKey pvtKey, byte[] value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher rsacf = Cipher.getInstance(CIPHER_TYPE);
        rsacf.init(Cipher.ENCRYPT_MODE,pvtKey);
        return rsacf.doFinal(value);
    }

    public static PrivateKey getPvtKey(String pvtKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(CIPHER_TYPE);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Custom.decodeBase64toBytes(pvtKey));
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    public static PublicKey getPubKey(String pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(CIPHER_TYPE);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64Custom.decodeBase64toBytes(pubKey));
        return keyFactory.generatePublic(pkcs8EncodedKeySpec);
    }
}
