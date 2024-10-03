package me.ilinskiy.chess.impl.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 06.12.15.
 */
public class CollectionUtil {
    public static <T1, T2> List<Tuple2<T1, T2>> zip(T1[] a, T2[] b) {
        int size = Math.min(a.length, b.length);
        ArrayList<Tuple2<T1, T2>> res = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            res.add(new Tuple2<>(a[i], b[i]));
        }
        return res;
    }
}
