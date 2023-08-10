package monitor.core;

/**
 * @author WuQinglong
 * @since 2023/3/3 14:55
 */
public class StrUtil {

    /**
     * 去除字符串中所有的空格
     */
    public static String trimAllWhitespace(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        int index = 0;
        while (sb.length() > index) {
            if (Character.isWhitespace(sb.charAt(index))) {
                sb.deleteCharAt(index);
            }
            else {
                index++;
            }
        }
        return sb.toString();
    }

}
