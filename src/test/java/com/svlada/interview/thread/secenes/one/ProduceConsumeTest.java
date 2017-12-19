package com.svlada.interview.thread.secenes.one;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ProduceConsumeTest {


    /**
      使用 wait和notify 来解决生产者消费者 问题
      解题思路：生产者生产物品，放入队列中，消费者从队列中消耗物品

     生产者和消费者是两个线程。不断轮询，查询共享区域也就是 共享对象的状态。
     当共享对象的队列为空的时候，则消费线程则在对共享区域的调用(消费)则将转为阻塞状态。
     当共享对象的队列为满队列的时候，则生产线程则在对共享区域的调用(生产)则将转为阻塞状态。

     */
    public static final int max = 10;
    //共享队列
    public static List<String> queue = new ArrayList<String>(max);


    public static void main(String[] args) {
        LinkedList<String> share  = new LinkedList<>();

        ConsumerThread consumerThreadA = new ConsumerThread(share);
        consumerThreadA.setName("consumerThreadA");
        ConsumerThread consumerThreadB = new ConsumerThread(share);
        consumerThreadB.setName("consumerThreadB");
        ProduceThread produceThreadA = new ProduceThread(share);
        produceThreadA.setName("produceThreadA");

        consumerThreadA.start();
        consumerThreadB.start();
        produceThreadA.start();

    }

    public void produce(){
        try {
            if (queue.size()==max){//容量已经满了，放不下了,就等待
                this.wait();
            }
            //否则,生产一个字符串，放入队列中
            queue.add(new Random().nextInt()+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void consume(){
        try {
            if (queue.size()==0){//队列为空，等待
                this.wait();
            }
            queue.remove(queue.size()-1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
