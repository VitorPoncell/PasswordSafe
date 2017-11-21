package com.poturno.vitor.passwordsafe.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vitor on 21/11/2017.
 */

public class Hash {

    private static MessageDigest algorithm;
    private static byte[] mensage;

    public static String getHash(String value) throws NoSuchAlgorithmException {
        algorithm = MessageDigest.getInstance("SHA-256");
        mensage = algorithm.digest(value.getBytes());
        return stringHexa(mensage);
    }

    private static String stringHexa(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
            int parteBaixa = bytes[i] & 0xf;
            if (parteAlta == 0)
                s.append('0');
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }
}
