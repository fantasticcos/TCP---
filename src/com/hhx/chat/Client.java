package com.hhx.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	Socket client;
	OutputStream out;
	InputStream in;
	Scanner sc ;
	String head ="";
	Client(int i){//设定用户
		head =(i==1)?"S12345T54321T":"S54321T12345T";
		redo(i);
	}
	
	public void send(String msg){
		//字符串格式  qqAmessage
		//我是12345 对面是 54321
		String ms = head+msg;
		try {
			client = new Socket("127.0.0.1",9999);
			out = client.getOutputStream();
			out.write(ms.getBytes("utf-8"));
			out.flush();
			out.close();
			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void recive(String qq,String content){
		System.out.println("收到消息:=================");
		System.out.println("发送者: "+qq+":       "+content);
	}
	public void redo(int i)//轮询线程，有消息收消息
	{
		new RedoThread(i,new Hook(){
			@Override
			public void doreceive(String qq,String content) {
				recive(qq, content);
			}}).start();;
	}
	public static void main(String[] args) {
		Client client = new Client(1);//a
		
		//client.redo();//轮询接受
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			client.send(sc.next());
			
		}

		
	}

}
