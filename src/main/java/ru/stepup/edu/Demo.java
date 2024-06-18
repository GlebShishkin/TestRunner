package ru.stepup.edu;

import ru.stepup.edu.annotation.AfterSuite;
import ru.stepup.edu.annotation.BeforeSuite;
import ru.stepup.edu.annotation.CsvSource;
import ru.stepup.edu.annotation.Test;

public class Demo {

    @BeforeSuite
    public static void functionBeforeSuite(Integer integer, String string) {
        if (string != null) {
            System.out.println("Вызов functionBeforeSuite перед вызовом метода " + string + " с приоритетом " + integer.toString());
        }
        else {
            System.out.println("Вызов functionBeforeSuite");
        }
    }

    @AfterSuite
    public static void functionAfterSuite() {
        System.out.println("Вызов functionAfterSuite");
    }

    @Test(priority=1)
    public void functionTest1() {
        System.out.println("Вызов functionTest1");
    }

    @Test(priority=5)
    public void functionTest5() {
        System.out.println("Вызов functionTest5");
    }

    @Test(priority=5)
    public void functionTest50() {
        System.out.println("Вызов functionTest50");
    }

    @Test(priority=10)
    public void functionTest10() {
        System.out.println("Вызов functionTest10");
    }

    @CsvSource(text = "10, Java, 20, true")
    public void testMethod(int a, String b, int c, boolean d) {
    }
}
