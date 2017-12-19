package com.svlada.interview.thread.secenes.one;

import java.util.LinkedList;

public class ConsumerThread extends Thread{

    private LinkedList<String> linkedList;

    public ConsumerThread(LinkedList<String> linkedList) {
        this.linkedList = linkedList;
    }

    //消费
    @Override
    public void run() {
        while (true){
            synchronized (linkedList){
                try {
                    if (linkedList.size()==0){
                        linkedList.wait();
                    }
                    String pop = linkedList.pop();//出队
                    System.out.println(this.getName()+"linkedList 出队元素：= " + pop+",linkedList 内元素的数量："+linkedList.size());
                    linkedList.notify();
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
