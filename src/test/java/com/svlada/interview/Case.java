package com.svlada.interview;

public class Case {
    public static void main(String args[]){
        final String lock = "abc";
        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                synchronized(lock){
                    System.out.println(Thread.currentThread().getName() + " starts...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() +"  ends...");
                }
            }
        });
        t1.setName("t1");

        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                synchronized(lock){
                    System.out.println(Thread.currentThread().getName() + " starts...");
                    lock.notify();
                    System.out.println(Thread.currentThread().getName() +"  ends...");
                }
            }
        });
        t2.setName("t2");

        Thread t3 = new Thread(new Runnable(){
            @Override
            public void run() {
                synchronized(lock){
                    System.out.println(Thread.currentThread().getName() + " starts...");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() +"  ends...");
                }
            }
        });
        t3.setName("t3");



        t1.start();
        t2.start();
        t3.start();
    }
}
