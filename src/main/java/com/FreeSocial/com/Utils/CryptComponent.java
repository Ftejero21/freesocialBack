package com.FreeSocial.com.Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Component
@Slf4j
public class CryptComponent {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "sV)NvO35)L>1%aS7"; // Debe ser de 16 caracteres

    public String encrypt(String valueToEnc) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedByteValue = cipher.doFinal(valueToEnc.getBytes("utf-8"));
            return Base64.encodeBase64String(encryptedByteValue);
        } catch (Exception e) {
            log.error("Error al encriptar", e);
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue64 = Base64.decodeBase64(encryptedValue);
            byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
            return new String(decryptedByteValue, "utf-8");
        } catch (Exception e) {
            log.error("Error al desencriptar", e);
            throw new RuntimeException(e);
        }
    }

    private Key generateKey() {
        return new SecretKeySpec(KEY.getBytes(), ALGORITHM);
    }
}
