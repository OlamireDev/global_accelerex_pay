package com.olamiredev.accelepay.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Slf4j
public class EncryptDecrypt {

    public final byte[] KEY = {118, 106, 107, 122, 76, 99, 69, 83, 101, 103, 82, 101, 116, 75, 101, 127};

    @Value("(${accelePay.salts}).split(\",\")")
    private String[] salts;

    public String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            System.out.println("No data to encrypt!");
            return plainText;
        }
        String encryptedString = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedText = cipher.doFinal(salts[0].concat(plainText).getBytes());
            encryptedString = Base64.getEncoder().encodeToString(encryptedText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException ex) {
           log.error("Exception caught while encrypting : " + ex);
        }
        return encryptedString;
    }

    public String decrypt(String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            System.out.println("No data to decrypt!");
            return cipherText;
        }
        String decryptedString = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(KEY, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] encryptedText = Base64.getDecoder().decode(cipherText.getBytes());
            decryptedString = new String(cipher.doFinal(encryptedText));

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            log.error("Exception caught while decrypting : " + ex);
        }
        for(String salt: salts){
            if(decryptedString.startsWith(salt)){
                return decryptedString.replace(salt, "");
            }
        }
        return decryptedString;
    }


}
