package com.svlada.interview.thread.secenes.one;

import java.util.LinkedList;
import java.util.Random;

public class ProduceThread  extends Thread{

    public static final int max = 50;
    private LinkedList<String> linkedList;

    public ProduceThread(LinkedList<String> linkedList) {
        this.linkedList = linkedList;
    }

    //生产者
    @Override
    public void run() {
        while (true) {
            synchronized (linkedList) {
                try {
                    if (linkedList.size() == max) {
                        System.out.println(this.getName()+"linkedList 内元素的数量已经满了");
                        linkedList.wait();
                    }
                    int nextInt = new Random().nextInt();
                    linkedList.add(nextInt+"");
                    System.out.println(this.getName()+":linkedList 入队元素 = " + nextInt+",linkedList 内元素的数量："+linkedList.size());
                    linkedList.notify();
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
