package fr.formation.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class HashingService {
    
   

/**
 * Fonction de hash SHA1
 * @param String toConvert
 */
public static String hashWithSHA1(String plaintext) {
    MessageDigest md = null;

    try {
        md = MessageDigest.getInstance("SHA-1"); // SHA-1 generator instance
    } catch(NoSuchAlgorithmException e) {
        return "";
    }

    try {
        //8859_1 ou UTF-8
        md.update(plaintext.getBytes("UTF-8")); // Message summary generation
    } catch(UnsupportedEncodingException e) {
        return "";
    }

    byte raw[] = md.digest(); // Message summary reception

    try{
        String hash = new String(org.apache.commons.codec.binary.Base64.encodeBase64(raw),"UTF-8");
        //String hash = new String(raw);
        return hash;
    }
    catch (UnsupportedEncodingException use){
        return "";
    }
}
}