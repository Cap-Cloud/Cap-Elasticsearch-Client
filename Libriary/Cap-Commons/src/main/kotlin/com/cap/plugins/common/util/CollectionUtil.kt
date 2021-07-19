package com.cap.plugins.common.util

import com.google.common.collect.Iterators
import java.util.*

object CollectionUtil {

    /**
     * 判断两个map是否相等
     *
     * @param map1
     * @param map2
     * @return
     */
    fun isEquals(map1: Map<*, *>?, map2: Map<*, *>?): Boolean {
        if (map1 == null && map2 == null) {
            return true
        }
        if (map1 == null && map2 != null) {
            return false
        }
        if (map1 != null && map2 == null) {
            return false
        }
        return if (map1 != null && map2 != null) {
            // key判断
            if (!Iterators.elementsEqual(
                    map1.keys.stream().sorted().iterator(),
                    map2.keys.stream().sorted().iterator()
                )
            ) {
                false
            } else {
                map1.keys
                    .map { key: Any? -> Objects.equals(map1[key], map2[key]) }
                    .fold(true) { a, b -> a && b }
            }
            // value判断
        } else false
    }
}