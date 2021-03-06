package com.holelin.sundry.test.common;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/29 17:21
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/29 17:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class CollectionTest {
    public static void main(String[] args) {
        ArrayListTest.addFromHeaderTest(100000);
        LinkedListTest.addFromHeaderTest(100000);


        ArrayListTest.addFromMidTest(10000);
        LinkedListTest.addFromMidTest(10000);

        ArrayListTest.addFromTailTest(1000000);
        LinkedListTest.addFromTailTest(1000000);

        ArrayListTest.deleteFromHeaderTest(100000);
        LinkedListTest.deleteFromHeaderTest(100000);

        ArrayListTest.deleteFromMidTest(100000);
        LinkedListTest.deleteFromMidTest(100000);

        ArrayListTest.deleteFromTailTest(1000000);
        LinkedListTest.deleteFromTailTest(1000000);

        ArrayListTest.getByForTest(10000);
        LinkedListTest.getByForTest(10000);

        ArrayListTest.getByIteratorTest(100000);
        LinkedListTest.getByIteratorTest(100000);
    }
}
