package com.holelin.redis.utils;

/**
 * @author jaco
 * @create 2019-03-13 下午6:36
 **/

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 */

/**
 * @Description:  反射工具类
 *  提供了一系列的获取某一个类的信息的方法<br />
 *  包括获取全类名，实现的接口，接口的泛型等<br />
 *  并且提供了根据Class类型获取对应的实例对象的方法，以及修改属性和调用对象的方法等
 * @Author: HoleLin
 * @CreateDate: 2020/11/18 15:46
 * @UpdateUser: HoleLin
 * @UpdateDate: 2020/11/18 15:46
 * @UpdateRemark: 修改内容

 * @Version: 1.0
 */

public class ReflectionUtils {


    /**
     * 获取类所有的属性(包括从父类继承的)
     * getFields()获得某个类的所有的公共（public）的字段，包括父类。
     * getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
     * 同样类似的还有getConstructors()和getDeclaredConstructors()，getMethods()和getDeclaredMethods()
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Field[] getAllDeclareFields(Class clazz) {
        //父类就是顶级类
        if (clazz.getSuperclass() == Object.class) {
            return clazz.getDeclaredFields();
        }

        Set<String> names = new HashSet<String>();
        List<Field> list = new ArrayList<Field>();
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if (names.contains(field.getName())) {
                    continue;
                }
                //字段属性
                list.add(field);
            }
            //父类
            searchType = searchType.getSuperclass();
        }
        //把list转化为数组形式返回
        Field[] fields = new Field[list.size()];
        return list.toArray(fields);

    }

    /**
     * 判断某字符串是否为空或长度为0或由空白符(whitespace) 构成
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 直接通过反射获取对象属性的值
     *
     * @param target
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object target, String fieldName) {
        if (target == null) {
            return null;
        }
        if (isBlank(fieldName)) {
            return null;
        }
        //Map直接返回
        if (target instanceof Map) {
            return ((Map<?, ?>) target).get(fieldName);
        }
        Class<?> clazz = null;
        if (target instanceof Class) {
            clazz = (Class<?>) target;
        } else {
            clazz = target.getClass();
        }

        Field field = org.springframework.util.ReflectionUtils.findField(clazz, fieldName);
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            String message = String.format("类 [%s] 反射访问属性 [%s] 异常!", clazz.getCanonicalName(), fieldName);
            throw new RuntimeException(message, e);
        }

    }

    /**
     * 直接通过反射设置对象属性的值
     *
     * @param target
     * @param fieldName
     * @param fieldValue
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setFieldValue(Object target, String fieldName, Object fieldValue) {

        if (target == null || isBlank(fieldName)) {
            return;
        }
        //map直接返回
        if (target instanceof Map) {
            ((Map) target).put(fieldName, fieldValue);
            return;
        }

        Class<?> clazz = null;
        if (target instanceof Class) {
            clazz = (Class<?>) target;
        } else {
            clazz = target.getClass();
        }

        Field field = org.springframework.util.ReflectionUtils.findField(clazz, fieldName);


        try {
            field.setAccessible(true);
            field.set(target, fieldValue);
        } catch (Exception e) {
            String message = String.format("类 [%s] 反射访问属性 [%s] 异常!", clazz.getCanonicalName(), fieldName);
            throw new RuntimeException(message, e);
        }

    }


}
