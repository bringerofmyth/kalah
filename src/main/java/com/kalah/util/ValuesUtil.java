package com.kalah.util;

/**
 * Created by melihgurgah on 15/10/2017.
 */
public class ValuesUtil {

    public static String encode(int[] array) {
        return String.join(";", toStringArray(array));
    }

    public static int[] decode(String string) {
        if (string == null || string.isEmpty()) return null;
        String[] split = string.split(";");

        return toIntArray(split);
    }

    public static String initialValues() {
        int[] values = {6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
        return encode(values);
    }

    private static int[] toIntArray(String[] strArray) {
        int[] array = new int[strArray.length];

        int i = 0;
        for (String item : strArray) {
            array[i++] = Integer.parseInt(item);
        }
        return array;
    }

    private static String[] toStringArray(int[] array) {
        String[] strArray = new String[array.length];
        int i = 0;
        for (int item : array) {
            strArray[i++] = String.valueOf(item);
        }
        return strArray;
    }
}
