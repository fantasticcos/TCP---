package com.hhx.chat;

import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) {



            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    System.out.println(new String("指向".getBytes("gbk")));
                    return "res";
                }
            });
            executorService.schedule(task,1,TimeUnit.SECONDS);
            try {
                System.out.println(task.get(2,TimeUnit.SECONDS));;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }



    }
}
