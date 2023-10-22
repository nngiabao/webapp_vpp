package Model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class cipher {

    public static String encode(String msg) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(msg.getBytes());
    }

    public static String decode(String msg) {
        Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(msg);
        return new String(bytes);
    }

}
