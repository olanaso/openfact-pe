package org.openfact.ubl.data.xml.mappers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openfact.JSONObjectUtils;
import org.openfact.ubl.data.xml.annotations.ArrayKey;
import org.openfact.ubl.data.xml.annotations.SimpleKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectMapper {

    public static <T> List<T> objectArrayKey(Class<T> clazz, List<JSONObject> jsons) {
        return jsons.stream().map(json -> mapObjectKey(clazz, json)).collect(Collectors.toList());
    }

    public static <T> T mapObjectKey(Class<T> clazz, JSONObject json) {
        T instance = null;
        try {
            instance = clazz.newInstance();

            // Simple key
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                Annotation simpleAnnotation = field.getAnnotation(SimpleKey.class);
                if (simpleAnnotation instanceof SimpleKey) {
                    SimpleKey simpleKey = (SimpleKey) simpleAnnotation;
                    Object value = JSONObjectUtils.getObject(json, simpleKey.key());
                    field.set(instance, simpleKey.mapper().newInstance().apply(value));
                }

                Annotation arrayAnnotation = field.getAnnotation(ArrayKey.class);
                if (arrayAnnotation instanceof ArrayKey) {
                    ArrayKey arrayKey = (ArrayKey) arrayAnnotation;

                    List value = new ArrayList();
                    if (JSONObjectUtils.isJSONArray(json, arrayKey.arrayKey())) {
                        JSONArray array = JSONObjectUtils.getJSONArray(json, arrayKey.arrayKey());
                        Iterator it = array.iterator();
                        while (it.hasNext()) {
                            JSONObject current = (JSONObject) it.next();
                            value.add(JSONObjectUtils.getObject(current, arrayKey.fieldKey()));
                        }
                    } else {
                        String[] fullKey = (String[]) addAll(arrayKey.arrayKey(), arrayKey.fieldKey());
                        value.add(JSONObjectUtils.getObject(json, fullKey));
                    }

                    Function mapper = arrayKey.mapper().newInstance();
                    field.set(instance, value.stream().map(mapper).collect(Collectors.toList()));
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Error parsing object", e);
        }

        return instance;
    }

    public static Object[] addAll(Object[] array1, Object[] array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        Object[] joinedArray = (Object[]) Array.newInstance(array1.getClass().getComponentType(),
                array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (ArrayStoreException ase) {
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because:
             * - it would be a wasted check most of the time
             * - safer, in case check turns out to be too strict
             */
            final Class type1 = array1.getClass().getComponentType();
            final Class type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)){
                throw new IllegalArgumentException("Cannot store "+type2.getName()+" in an array of "+type1.getName());
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    public static Object[] clone(Object[] array) {
        if (array == null) {
            return null;
        }
        return (Object[]) array.clone();
    }

}
