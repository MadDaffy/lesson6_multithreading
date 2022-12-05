package com.geekbrains.lesson6;

import java.util.Arrays;

//        1. Необходимо написать два метода, которые делают следующее:
//        1) Создают одномерный длинный массив, например:
//        static final int SIZE = 10 000 000 ;
//        static final int HALF = size / 2 ;
//        float [] arr = new float [ size ].
//        2) Заполняют этот массив единицами.
//        3) Засекают время выполнения: long a = System.currentTimeMillis().
//        4) Проходят по всему массиву и для каждой ячейки считают новое значение по формуле:
//        arr[ i ] = (f loat) (a rr[ i ] * Math. s in( 0 .2f + i / 5) * Math. c os( 0 .2f + i / 5) *
//        Math . cos ( 0.4f + i / 2 )).
//        5) Проверяется время окончания метода System.currentTimeMillis().
//        6) В консоль выводится время работы: System.out.println(System.currentTimeMillis() - a).
//        Отличие первого метода от второго:
//        ● Первый просто бежит по массиву и вычисляет значения.
//        ● Второй разбивает массив на два массива, в двух потоках высчитывает новые значения и
//        потом склеивает эти массивы обратно в один.
//        Пример деления одного массива на два:
//        ● System.arraycopy(arr, 0, a1, 0, h);
//        ● System.arraycopy(arr, h, a2, 0, h).
//        Пример обратной склейки:
//        ● System.arraycopy(a1, 0, arr, 0, h);
//        ● System.arraycopy(a2, 0, arr, h, h).
//        Примечание:
//        System.arraycopy() — копирует данные из одного массива в другой:
//        System.arraycopy(массив-источник, откуда начинаем брать данные из массива-источника,
//        массив-назначение, откуда начинаем записывать данные в массив-назначение, сколько ячеек
//        копируем)
//        По замерам времени:
//        Для первого метода надо считать время только на цикл расчета:
//        for ( int i = 0 ; i < size ; i ++) {
//        arr [ i ] = ( float )( arr [ i ] * Math . sin ( 0.2f + i / 5 ) * Math . cos ( 0.2f + i / 5 ) *
//        Math . cos ( 0.4f + i / 2 ));
//        }
//        Для второго метода замеряете время разбивки массива на 2, просчета каждого из двух массивов и
//        склейки.

public class MainApp {
    public static void main(String[] args) {
        calculation();
        calculationWithThread();
    }


    static final int SIZE = 10_000_000;
    static final int HALF = SIZE / 2;

    public static void calculation() {

        float[] arr = new float[SIZE];
        Arrays.fill(arr, 1.0f);
        long a = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                    Math.cos(0.4f + i / 2));
        }
//      System.currentTimeMillis(); - не понял зачем это. Пунк 5.
        System.out.println(System.currentTimeMillis() - a);
    }

    public static void calculationWithThread() {
        float[] arr = new float[SIZE];
        Arrays.fill(arr, 1.0f);
        long a = System.currentTimeMillis();
        float[] a1 = new float[HALF];
        float[] a2 = new float[HALF];
        System.arraycopy(arr, 0, a1, 0, HALF);
        System.arraycopy(arr, HALF, a2, 0, HALF);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < a1.length; i++) {
                a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) *
                        Math.cos(0.4f + i / 2));
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < a2.length; i++) {
                a2[i] = (float) (a2[i] * Math.sin(0.2f + (i + HALF) / 5) * Math.cos(0.2f + (i + HALF) / 5) *
                        Math.cos(0.4f + (i + HALF) / 2));
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.arraycopy(a1, 0, arr, 0, HALF);
        System.arraycopy(a2, 0, arr, HALF, HALF);
//      System.currentTimeMillis(); - не понял зачем это. Пунк 5.
        System.out.println(System.currentTimeMillis() - a);
    }
}