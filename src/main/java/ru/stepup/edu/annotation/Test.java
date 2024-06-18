package ru.stepup.edu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

// ТЗ: Для аннотации @Test добавляете параметр priority (int в пределах от 1 до 10 включительно)
public @interface Test {
    int  priority ()  default  5;   // Если параметр не задан явно, то он равен 5
}
