package com.hhx.chat;

import java.util.Scanner;

public class B {

	public static void main(String[] args) {

Client client = new Client(0);//a
		
		//client.redo();//ÂÖÑ¯½ÓÊÜ
		Scanner sc = new Scanner(System.in);
		while(sc.hasNext()){
			client.send(sc.next());
			
		}
	}

}
