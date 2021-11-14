package com.hhx.chat;

import java.util.Scanner;

public class A {

	public static void main(String[] args) {

Client client = new Client(1);//a
		
		//client.redo();//轮询接受
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			client.send(sc.next());
			
		}
	}

}
