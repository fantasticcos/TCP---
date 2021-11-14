package com.hhx.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket listen ;
	Socket client = null;
	List<Socket> container;
	Map<String,Map<String,String>> map;//value��key�����Ǹ�����Ϣ
	byte [] buff = new byte[1024];
	Server() throws IOException{//����������Ĳ����У�1���洢��Ϣ������������
		listen = new ServerSocket(9999);//����Ƿ���Ϣ
		//read
		container = new ArrayList<>(10);
		map = new ConcurrentHashMap<>();
	}//���彻����ʽ: ����Ϊ��ͷS(sb T sb)������Ϊ��ͷR
	
	public void listening()
	{
		ExecutorService pool = Executors.newFixedThreadPool(10);
		while(true)
		{

				try {
					client = listen.accept();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				pool.submit(()->{
					try {
						OutputStream out;
						InputStream in;
						InputStreamReader buffin;
						BufferedReader reader;
						OutputStreamWriter buffout;
						//Scanner sc ;
						
						BufferedWriter writer;
						in = client.getInputStream();
						buffin = new InputStreamReader(in);
						reader = new BufferedReader(buffin);

						//sc = new Scanner(in,"utf-8");
						out = client.getOutputStream();
						buffout = new OutputStreamWriter(out);
						writer = new BufferedWriter(buffout);

						int i = 1;
						StringBuilder sb = new StringBuilder();
						String temp = null;
						System.out.println(client.getRemoteSocketAddress()+"==�������������=="+ System.currentTimeMillis());
						while(reader.ready())
						{
							String s = reader.readLine();
							sb.append(s);

						}

						System.out.println(client.getRemoteSocketAddress()+"==��ȡָ�����ʱ��=="+ System.currentTimeMillis());
						System.out.println(sb.toString());

						if(sb.charAt(0)=='S')//����Ƿ���������Ϣ,������
						{
							String realcontent = sb.substring(1);
							String [] qqs = realcontent.split("T");
							String qq = qqs[0];
							String toqq =qqs[1];
							String content = sb.substring(sb.indexOf("E")+1)+"\n";
							Map<String,String> owner;
					//		System.out.println(qq+" ���� "+toqq+"  "+qqs[2] );
							if(map.get(qq)!=null)
							{	owner = map.get(qq);}
							else
							{ owner = new HashMap<>();}
							//if(owner.get(toqq)==null)
								owner.put(toqq, qqs[2]);
							//else
							//	owner.put(toqq, owner.get(toqq)+content);
							map.put(qq, owner);
						}else if(sb.charAt(0)=='R')//�����Ҫ��ȡ��Ϣ
						{//RclientTclient2
							//System.out.println("û�н����ȡ���򣿣�");
							String realcontent = sb.substring(1);
							String [] qqs = realcontent.split("T");
							String qq = qqs[0];
							String toqq =qqs[1];
							Map<String, String> owner = map.get(qq);
							sb = new StringBuilder();
							if(owner==null)
							{
								//System.out.println("û�����ݣ�");
								in.close();
								out.close();
								buffin.close();
								buffout.close();
								writer.close();
								reader.close();
								client.close();
								return;
							}
							String content = owner.get(toqq);
							//byte [] c_byte;
							//System.out.println("������ת��"+content);
							if(content!=null)
							{
								try{
									//c_byte = content.getBytes();
									//PrintStream ps= new PrintStream(out);
									//ps.println(content);
									//ps.flush();
									//ps.close();
									System.out.println(client.getRemoteSocketAddress()+"==����ǰ=="+ System.currentTimeMillis());
									writer.write(content);
									System.out.println(client.getRemoteSocketAddress()+"==���ͺ�=="+ System.currentTimeMillis());
									writer.flush();
									writer.close();
									reader.close();
									buffin.close();
									buffout.close();
									in.close();
									out.close();


								}catch(SocketException e){
									System.out.println("��;�ر��ˣ�");
									client.close();
									return;
								}
								owner.remove(toqq,content);
								//���ɾ���д���ȶ

							}


						}
	//					else if(sb.charAt(0)=='D')
	//					{
	//						String realcontent = sb.substring(1);
	//						String [] qqs = realcontent.split("T");
	//						String qq = qqs[0];
	//						String toqq =qqs[1];
	//						Map<String, String> owner = map.get(qq);
	//
	//						if(owner==null)
	//						{
	//							in.close();
	//							out.close();
	//							buffin.close();
	//							buffout.close();
	//							return;
	//						}
	//						String content = owner.get(toqq);
	//						System.out.println(content);
	//						owner.remove(toqq);
	//						if(null!=owner.get(toqq))
	//							System.out.println(owner.get(toqq));
	//					}
						in.close();
						out.close();
						buffin.close();
						buffout.close();
						writer.close();
						reader.close();
						client.close();
					} catch(SocketException e){
						try {
							client.close();
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					} catch (IOException e) {
						try {
							client.close();
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}
				});
				
			
			
		}
	}
	public static void main(String[] args) {
		try {
			new Server().listening();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

