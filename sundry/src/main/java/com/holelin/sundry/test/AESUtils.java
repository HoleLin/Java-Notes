package com.holelin.sundry.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AES的加密和解密
 * 
 * @author libo
 */
public class AESUtils {
    // 密钥 (需要前端和后端保持一致)
    public static final String KEY = "YW#309YW#309YW#3";
    // 算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * aes解密
     * 
     * @param encrypt 内容
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String encrypt) {
        try {
            return aesDecrypt(encrypt, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static byte[] aesDecryptBytesToBytes(byte[] content, String encryptKey) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, encryptKey);

        return cipher.doFinal(content);
    }
    /**
     * aes加密
     * 
     * @param content
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String content) {
        try {
            return aesEncrypt(content, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将byte[]转为各种进制的字符串
     * 
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     * 
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     * 
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return StringUtils.isEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
    }

    /**
     * AES加密
     * 
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, encryptKey);

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES加密
     * 
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptBytesToBytes(byte[] content, String encryptKey) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, encryptKey);

        return cipher.doFinal(content);
    }

    private static Cipher getCipher(int encryptMode, String encryptKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(encryptMode, new SecretKeySpec(encryptKey.getBytes(), "AES"));
        return cipher;
    }

    /**
     * AES加密为base 64 code
     * 
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     * 
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, decryptKey);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * AES解密
     * 
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的byte
     * @throws Exception
     */
    public static byte[] aesDecryptBytesByBytes(byte[] encryptBytes) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, KEY);
        return cipher.doFinal(encryptBytes);
    }

    /**
     * 将base 64 code AES解密
     * 
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * 将文件byte AES加密
     *
     */
    public static byte[] encryptFile(byte[] file) throws Exception {

        byte[] file_last = new byte[1024];
        System.arraycopy(file, file.length - 1024, file_last, 0, 1024);

        byte[] file_salt = AESUtils.aesEncryptBytesToBytes(file_last, KEY);

        byte[] result = new byte[file.length - 1024 + file_salt.length];
        System.arraycopy(file, 0, result, 0, file.length - 1024);
        System.arraycopy(file_salt, 0, result, file.length - 1024, file_salt.length);
        
        return result;

    }

    /**
     * com.yw.ct.util.AESUtils#encryptFile(byte[]) 方法对应的解密方法
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] decryptFile(byte[] bytes) throws Exception {
        int lastBlockSize = 1040;
        byte[] file_salt = new byte[lastBlockSize];
        System.arraycopy(bytes, bytes.length - lastBlockSize, file_salt, 0, lastBlockSize);

        byte[] file_last = AESUtils.aesDecryptBytesByBytes(file_salt);

        byte[] result = new byte[bytes.length - lastBlockSize + file_last.length];
        System.arraycopy(bytes, 0, result, 0, bytes.length - lastBlockSize);
        System.arraycopy(file_last, 0, result, bytes.length - lastBlockSize, file_last.length);

        return result;
    }
}