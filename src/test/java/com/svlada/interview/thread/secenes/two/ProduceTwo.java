package com.svlada.interview.thread.secenes.two;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class ProduceTwo<String> implements Runnable{

    private BlockingQueue<String> blockingQueue;

    public ProduceTwo(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        while(true){
            try {
                blockingQueue.put((String) new Date().toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
