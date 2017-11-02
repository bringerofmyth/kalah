package com.kalah;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by melihgurgah on 15/10/2017.
 */
public class TestUtil {
    public static List<Integer> toIntegerList(int[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

}
