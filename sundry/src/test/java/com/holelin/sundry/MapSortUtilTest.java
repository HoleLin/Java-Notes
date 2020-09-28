package com.holelin.sundry;

import com.holelin.sundry.utils.collection.MapSortUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;


/**
 * Map排序工具类
 *
 * @author liuwangyanghdu@163.com  明明如月
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapSortUtilTest {
    /**
     * 创建一个字符串为Key，数字为值的map
     */
    Map<String, Integer> budget = new HashMap<>();

    @BeforeAll
    public void init() {
        budget.put("clothes", 120);
        budget.put("grocery", 150);
        budget.put("transportation", 100);
        budget.put("utility", 130);
        budget.put("rent", 1150);
        budget.put("miscellneous", 90);
        System.out.println("排序前: " + budget);
    }

    @Test
    void sortByKeyAsc() {
        System.out.println("按键升序" + MapSortUtil.sortByKeyAsc(budget));
    }

    @Test
    void sortByKeyDesc() {
        System.out.println("按键降序" + MapSortUtil.sortByKeyDesc(budget));
    }

    @Test
    void sortByValueAsc() {
        System.out.println("按值升序" + MapSortUtil.sortByValueAsc(budget));
    }

    @Test
    void sortByValueDesc() {
        System.out.println("按值降序" + MapSortUtil.sortByValueDesc(budget));
    }

    @Test
    void sortByValueDescEmpty() {
        System.out.println("按值降序" + MapSortUtil.sortByValueDesc(null));
    }
}