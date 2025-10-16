package org.example.util;

import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 通用 Bean 拷贝工具类
 * 功能：仅将 source 中非空字段的值拷贝到 target 对象中
 */
public class BeanCopyUtil {

    /**
     * 拷贝 source 中的非空属性到 target
     *
     * @param source 源对象（提供数据）
     * @param target 目标对象（被赋值）
     */
    public static void copyNonNullProperties(Object source, Object target) {
        if (source == null || target == null) return;
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 获取对象中属性值为 null 的字段名数组
     */
    private static String[] getNullPropertyNames(Object source) {
        final Set<String> emptyNames = new HashSet<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        for (PropertyDescriptor pd : propertyDescriptors) {
            try {
                Method getter = pd.getReadMethod();
                if (getter != null) {
                    Object value = getter.invoke(source);
                    if (value == null) {
                        emptyNames.add(pd.getName());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return emptyNames.toArray(new String[0]);
    }
}
