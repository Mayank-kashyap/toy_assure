package channel.util;

public class StringUtil {
    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }
}
