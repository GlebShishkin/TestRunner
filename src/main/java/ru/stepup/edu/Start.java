package ru.stepup.edu;

import java.lang.reflect.InvocationTargetException;

public class Start {

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        Class clazz = Demo.class;
        TestRunner.runTests(clazz);
    }
}
