
/**
 * Created by daichao on 2017/11/3.
 * AES加密算法
 */
public class AESUtils {
    private final static Logger logger = LoggerFactory.getLogger(AESUtils.class);

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @return 密文
     */
    public static String encryptAES(String content,String rawPassword) {
        String password;
        try {
            //生成password
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            password  = base64en.encode(md5.digest(rawPassword.getBytes("utf-8")));

            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者

            kgen.init(128, new SecureRandom(password.getBytes()));// 利用用户密码作为随机数初始化出

            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥

            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回-1

            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥

            Cipher cipher = Cipher.getInstance("AES");// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器

            return parseByte2HexStr(cipher.doFinal(byteContent));

        } catch (Exception e) {
            logger.error("加密出错,内容={}，密码={},异常={}", content, LogExceptionStackTrace.erroStackTrace(e));
        }
        return "-1";
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

}
