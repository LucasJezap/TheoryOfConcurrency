package com.company;

class Counter {
    public static int counter = 0;

    public synchronized static void inc() {
        counter += 1;
    }

    public synchronized static void dec() {
        counter -= 1;
    }
}

public class Main {

    public static void main(String[] args) {

        Thread threadOne = new Thread(() -> {
            for (int i = 0; i < 1e6; i++) {
                Counter.inc();
            }
        });
        Thread threadTwo = new Thread(() -> {
            for (int i = 0; i < 1e6; i++) {
                Counter.dec();
            }
        });

        System.out.println("Counter start value = " + Counter.counter);

        threadOne.start();
        threadTwo.start();

        try {
            threadOne.join();
        } catch (Exception ignored) {
        }

        try {
            threadTwo.join();
        } catch (Exception ignored) {
        }

        System.out.println("Counter end value = " + Counter.counter);
    }
}
