package top.gytf.family.server.utils;

import top.gytf.family.server.exceptions.DecryptedException;
import top.gytf.family.server.exceptions.rsa.EncryptedException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * Project:     IntelliJ IDEA<br>
 * Description: 非对称加密工具<br>
 * CreateDate:  2021/12/18 14:16 <br>
 * ------------------------------------------------------------------------------------------
 *
 * @author user
 * @version V1.0
 */
public class RsaUtil {
    public static final String ALGORITHM = "RSA";
    public static final Integer SIZE_OF_KEY = 1024;
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static RsaUtil instance;

    private final RSAPublicKey publicKey;
    private String stringPublicKey;
    private final Cipher rsaEncryptedCipher;
    private final Cipher rsaDecryptedCipher;
    private final Encoder base64Encoder;
    private final Decoder base64Decoder;

    /**
     * 获取实例
     * @return Rsa工具实例
     */
    public synchronized static RsaUtil getInstance() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        if (instance == null) {
            instance = new RsaUtil();
        }
        return instance;
    }

    private RsaUtil() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        // 生成器
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(SIZE_OF_KEY);
        // 生成密钥对
        KeyPair keyPair = generator.generateKeyPair();
        publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // Cipher
        rsaEncryptedCipher = Cipher.getInstance(ALGORITHM);
        rsaDecryptedCipher = Cipher.getInstance(ALGORITHM);
        rsaEncryptedCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        rsaDecryptedCipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Base64
        base64Encoder = Base64.getEncoder();
        base64Decoder = Base64.getDecoder();
    }

    /**
     * 获取公钥
     * @return 公钥
     */
    public String getPublicKey() {
        if (stringPublicKey == null) {
            stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        }
        return stringPublicKey;
    }

    /**
     * 加密
     * @param data 明文
     * @return 密文
     */
    public String encrypted(String data) {
        try {
            int inputLen = data.getBytes().length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = rsaEncryptedCipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = rsaEncryptedCipher.doFinal(data.getBytes(), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            return base64Encoder.encodeToString(encryptedData);
        } catch (Exception e) {
            // 转换异常
            throw new EncryptedException(e.getMessage());
        }
    }

    /**
     * 解密
     * @param data 密文
     * @return 明文
     */
    public String decrypted(String data) {
        try {
            byte[] dataBytes = base64Decoder.decode(data);
            int inputLen = dataBytes.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = rsaDecryptedCipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = rsaDecryptedCipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            out.close();

            // 解密后的内容
            return out.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            // 转换错误
            throw new DecryptedException(e.getMessage());
        }
    }
}
