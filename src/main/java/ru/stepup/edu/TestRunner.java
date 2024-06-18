package ru.stepup.edu;

import ru.stepup.edu.annotation.AfterSuite;
import ru.stepup.edu.annotation.BeforeSuite;
import ru.stepup.edu.annotation.CsvSource;
import ru.stepup.edu.annotation.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class TestRunner {

    public static void runTests(Class clazz) throws InvocationTargetException, IllegalAccessException {

        Demo demo = new Demo();
        Method[] methods = clazz.getDeclaredMethods();

        // ТЗ: Происходит проверка что методов с аннотациями @BeforeSuite не больше одного
        long cnt = Arrays.stream(methods).filter(x->x.isAnnotationPresent(BeforeSuite.class)).count();
        if (1 < cnt) {
            throw new RuntimeException("Количество методов класса " + clazz.getName() + " с аннотацией 'BeforeSuite' ольше лдного");
        }

        // ТЗ: Происходит проверка что методов с аннотациями @AfterSuite не больше одного
        cnt = Arrays.stream(methods).filter(x->x.isAnnotationPresent(AfterSuite.class)).count();
        if (1 < cnt) {
            throw new RuntimeException("Количество методов класса " + clazz.getName() + " с аннотацией 'AfterSuite' ольше лдного");
        }
        Method afterMethod = Arrays.stream(methods).filter(x->x.isAnnotationPresent(AfterSuite.class))
                .findFirst()
                .orElse(null);

        // ТЗ: Выполняется метод с аннотацией @BeforeSuite, если такой есть
        Method beforeMethod = Arrays.stream(methods).filter(x->x.isAnnotationPresent(BeforeSuite.class))
                .findFirst()
                .orElse(null);
        if (beforeMethod != null) {
            Object [] args = new Object[]{(int)0,new String("")};
            beforeMethod.invoke(demo, args);
        }

        // ТЗ: Выполняются методы с аннотациями @Test в соответствии с их приоритетом.
        Map<Integer, Method> mapTest = new HashMap<>();
        for (Method method : methods) {

            // сохраняем в Map все метода, где ключ - приоритет вызова метода @Test.priority
            if (method.isAnnotationPresent(Test.class)) {
                Test test = method.getAnnotation(Test.class);
                int priority = test.priority() * 100;   // формируем уникальный ключ с учетом приоритета запуска
                while (mapTest.containsKey(priority)) {
                    priority++;
                }
                mapTest.put(priority, method);
            }
        }

        // ТЗ: Вначале выполняются те методы, у которых приоритет выше.
        SortedSet<Integer> keys = new TreeSet<Integer>(Collections.reverseOrder());
        keys.addAll(mapTest.keySet());

        for (Integer key : keys) {
            // запускаем по приоритетам метода с аннотацией @Test.priopity
            Method method = mapTest.get(key);
            System.out.println("Method name : " + method.getName() + "; " + key);

            // ТЗ: Можете добавить аннотации @BeforeTest и @AfterTest, методы с такими аннотациями должны выполняться перед каждым и после каждого теста соответственно
            if (beforeMethod != null) {
                // запуск иетода с аннотацией @BeforeTest перед каждым вызовом метода с аннотацией @Test
                Object [] args1 = new Object[]{key, new String(method.getName())};
                beforeMethod.invoke(demo, args1);
            }

            method.invoke(demo, null); // запуск метода с аннотацией @Test.priopity

            // ТЗ: Можете добавить аннотации @BeforeTest и @AfterTest, методы с такими аннотациями должны выполняться перед каждым и после каждого теста соответственно
            if (afterMethod != null) {
                // запуск иетода с аннотацией @AfterTest после каждого вызова метода с аннотацией @Test
                afterMethod.invoke(demo, null);
            }
        }

        // ТЗ: Добавьте аннотацию @CsvSource параметром которой является строка. При запуске метода-теста эта строка должна распарситься,
        // каждый элемент строки приведен к типу соответствующего аргумента метода и метод выполнен с данными из указанной строки.
        for (Method md : methods) {
            if (md.isAnnotationPresent(CsvSource.class)) {
                CsvSource csvSource = md.getAnnotation(CsvSource.class);
                String str = csvSource.text().replace(" ", "");
                List<String> list = Arrays.asList(str.split(","));  // переведем строку аннотации @CsvSource.text в список

                Class<?>[] params = md.getParameterTypes();
                Object [] args = new Object[params.length];

                // заполним параметры вызова данными аннотации, взятыми из списка
                int kk = 0;
                for (Class<?> paramType : params) {
                    System.out.println(" " + paramType.getName() + "; list.get(" + kk + ") = " + list.get(kk));
                    if (paramType.toGenericString().contains("int")) {
                        int i = Integer.parseInt(list.get(kk));
                        args[kk] = i;
                    } else if ((paramType.toGenericString().contains("String"))) {
                        args[kk] = new String(list.get(kk));
                    }
                    else if ((paramType.toGenericString().contains("boolean"))) {
                        boolean b = Boolean.parseBoolean(list.get(kk));
                        args[kk] = b;
                    }
                    kk++;
                }
                md.invoke(demo, args);
            }   // CsvSource.class
        }   // Method md : methods
    }
}
