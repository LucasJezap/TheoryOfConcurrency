package com.company;

// ŁUKASZ JEZAPKOWICZ LAB 2

public class Main {

    private static final Object l = new Object();
    private static int counter = -1;

    public static void main(String[] args) throws InterruptedException {

        Thread threadOne = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (l) {
                    f1();
                }
            }
        });
        Thread threadTwo = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (l) {
                    f2();
                }
            }
        });
        Thread threadThree = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (l) {
                    f3();
                }
            }
        });

        System.out.println("Order 1 = (1,2,3)");
        threadOne.start();
        threadTwo.start();
        threadThree.start();
        threadOne.join();
        threadTwo.join();
        threadThree.join();

//        System.out.println("Order 2 = (1,3,2)");
//        threadOne.start();
//        threadThree.start();
//        threadTwo.start();
//        threadOne.join();
//        threadTwo.join();
//        threadThree.join();
//
//        System.out.println("Order 3 = (2,3,1)");
//        threadTwo.start();
//        threadThree.start();
//        threadOne.start();
//        threadOne.join();
//        threadTwo.join();
//        threadThree.join();
//
//        System.out.println("Order 4 = (2,1,3)");
//        threadTwo.start();
//        threadOne.start();
//        threadThree.start();
//        threadOne.join();
//        threadTwo.join();
//        threadThree.join();
//
//        System.out.println("Order 5 = (3,1,2)");
//        threadThree.start();
//        threadOne.start();
//        threadTwo.start();
//        threadOne.join();
//        threadTwo.join();
//        threadThree.join();
//
//        System.out.println("Order 6 = (3,2,1)");
//        threadThree.start();
//        threadTwo.start();
//        threadOne.start();
//        threadOne.join();
//        threadTwo.join();
//        threadThree.join();
    }

    private static void f1() {
        while (counter != 3 && counter != -1) {
            try {
                l.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter = 1;
        System.out.println(counter);
        l.notifyAll();
    }

    private static void f2() {
        while (counter != 1) {
            try {
                l.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter = 2;
        System.out.println(counter);
        l.notifyAll();
    }

    private static void f3() {
        while (counter != 2) {
            try {
                l.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        counter = 3;
        System.out.println(counter + "\n");
        l.notifyAll();
    }
}
