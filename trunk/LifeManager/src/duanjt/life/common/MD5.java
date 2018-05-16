package duanjt.life.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * MD5加密类
 * @author Administrator
 *
 */
public class MD5 {

	/**
	 * MD5加密，该方法能保证和C#返回的值一致
	 * @param strObj
	 * @return
	 */
    public static String GetMD5Code(String strObj) {
    	MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = strObj.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            String result= hexValue.toString();
            result=result.substring(0,result.length()-2).toUpperCase();
            return result;
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        
    }


}
