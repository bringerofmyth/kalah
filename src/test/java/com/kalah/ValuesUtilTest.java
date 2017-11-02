package com.kalah;

import com.kalah.util.ValuesUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;

/**
 * Created by melihgurgah on 15/10/2017.
 */

public class ValuesUtilTest {
    int[] values = null;

    @Before
    public void init() {
        values = new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    }

    @Test
    public void testEncode() {
        final String encoded = ValuesUtil.encode(values);
        assertEquals("6;6;6;6;6;6;0;6;6;6;6;6;6;0", encoded);
    }

    @Test
    public void testDecode() {
        final String encoded = "6;6;6;6;6;6;0;6;6;6;6;6;6;0";
        final int[] decoded = ValuesUtil.decode(encoded);
        List<Integer> actual = TestUtil.toIntegerList(decoded);
        assertThat(actual, contains(6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0));
        assertEquals(14, actual.size());
    }
}
