package com.hhx.chat;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.*;

public class RedoThread extends Thread {
	Hook hook;
	String singal = "";
	String singal_d = "";
	String own = "";
	String conetnt ="";
	int count = 0;


	public RedoThread(int tag, Hook hook) {
		this.hook = hook;
		singal = (tag == 1) ? "R54321T12345\n" : "R12345T54321\n";
		singal_d = (tag==1)?"D54321T12345" : "D12345T54321";
		own = (tag == 1) ? "12345" : "54321";
	}

	public void run() {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		//ExecutorService executorService = Executors.newFixedThreadPool(1);
		while (true) {
			try {

				Socket client = new Socket("127.0.0.1", 9999);
				//System.out.println(client.getLocalPort()+"===="+System.currentTimeMillis());
				InputStream in = client.getInputStream();
				InputStreamReader buffin = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(buffin);
				OutputStream out = client.getOutputStream();
				out.write(singal.getBytes());
				out.flush();

				byte[] buff = new byte[128];
				conetnt = "";

//				executorService.submit(new Runnable() {
//					@Override
//					public void run() {
//						System.out.println(System.currentTimeMillis());
//								while (sc.hasNext()) {
//									System.out.println("有数据");
//									conetnt += sc.next();
//								}
//							}
//				});
//				executorService.shutdown();

				FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
					@Override
					public String call() throws Exception {
						//System.out.println(System.currentTimeMillis());

							while(reader.ready())
							{
								conetnt = reader.readLine();
								//System.out.println("还没有准备哈");

							}
						//System.out.println("实际转发流读取"+conetnt);
						return conetnt;
					}
				});
				//System.out.println(count);
				//executorService.execute(task);
				executorService.schedule(task,2,TimeUnit.SECONDS);
				try {
					conetnt = task.get(4,TimeUnit.SECONDS);
					//System.out.println("得到？"+conetnt);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					System.out.println("断了");
					out.flush();
					out.close();
					in.close();
					buffin.close();
					reader.close();
					client.close();
					continue;
				} catch (TimeoutException e) {
					System.out.println("超时了？");
					out.flush();
					out.close();
					in.close();
					buffin.close();
					reader.close();
					client.close();
					continue;
				}
				boolean result = true;

				if(!conetnt.equals(""))
				{
					hook.doreceive(own, conetnt);
					//out.write(singal_d.getBytes());
				}
				//count++;
				out.flush();
				out.close();
				in.close();
				buffin.close();
				reader.close();
				client.close();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
