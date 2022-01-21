package com.holelin.sundry.demo;

import com.holelin.sundry.domain.Employee;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2020/7/29 13:52
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/7/29 13:52
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

public class EasyRandomTest {
    public static void main(String[] args) {

        EasyRandomParameters parameters = new EasyRandomParameters();
        parameters.stringLengthRange(3, 3);
        parameters.collectionSizeRange(5, 5);
        parameters.excludeField(FieldPredicates.named("lastName").and(FieldPredicates.inClass(Employee.class)));
        EasyRandom generator =new EasyRandom(parameters);
        Employee employee = generator.nextObject(Employee.class);

        System.out.println(employee);
        System.out.println(Math.abs(-2147483648));
    }
}
