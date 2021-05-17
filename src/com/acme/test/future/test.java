package com.acme.test.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class test {


    static class MyCallable implements Callable<String> {


        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return "1000000";
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final FutureTask futureTask = new FutureTask<>(new MyCallable());
        new Thread(futureTask).start();
        final Object o = futureTask.get();
        System.out.println("haha");

    }
}
