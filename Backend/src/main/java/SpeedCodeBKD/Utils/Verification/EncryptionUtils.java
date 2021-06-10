package SpeedCodeBKD.Utils.Verification;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {
    public static String SHA256(String orgString) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(orgString.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

    public static String SHA512(String orgString) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hash = messageDigest.digest(orgString.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

    public static String MD5(String orgString) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hash = messageDigest.digest(orgString.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hash);
    }

    public static String passwordEncryption(String orgString) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String resultString = orgString;
        for(int i = 0; i < 10; i++) { resultString = MD5(orgString); }
        for(int i = 0; i < 10; i++) { resultString = SHA256(orgString); }
        for(int i = 0; i < 10; i++) { resultString = SHA512(orgString); }
        return resultString;
    }
}
