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
	Map<String,Map<String,String>> map;//value给key发佛那个的消息
	byte [] buff = new byte[1024];
	Server() throws IOException{//服务器处理的操作有，1，存储消息等所有者来拿
		listen = new ServerSocket(9999);//这个是发消息
		//read
		container = new ArrayList<>(10);
		map = new ConcurrentHashMap<>();
	}//定义交互格式: 发送为开头S(sb T sb)，接收为开头R
	
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
						System.out.println(client.getRemoteSocketAddress()+"==服务器获得连接=="+ System.currentTimeMillis());
						while(reader.ready())
						{
							String s = reader.readLine();
							sb.append(s);

						}

						System.out.println(client.getRemoteSocketAddress()+"==读取指令完毕时间=="+ System.currentTimeMillis());
						System.out.println(sb.toString());

						if(sb.charAt(0)=='S')//如果是发送来的消息,存起来
						{
							String realcontent = sb.substring(1);
							String [] qqs = realcontent.split("T");
							String qq = qqs[0];
							String toqq =qqs[1];
							String content = sb.substring(sb.indexOf("E")+1)+"\n";
							Map<String,String> owner;
					//		System.out.println(qq+" 发给 "+toqq+"  "+qqs[2] );
							if(map.get(qq)!=null)
							{	owner = map.get(qq);}
							else
							{ owner = new HashMap<>();}
							//if(owner.get(toqq)==null)
								owner.put(toqq, qqs[2]);
							//else
							//	owner.put(toqq, owner.get(toqq)+content);
							map.put(qq, owner);
						}else if(sb.charAt(0)=='R')//如果是要拿取消息
						{//RclientTclient2
							//System.out.println("没有进入读取程序？？");
							String realcontent = sb.substring(1);
							String [] qqs = realcontent.split("T");
							String qq = qqs[0];
							String toqq =qqs[1];
							Map<String, String> owner = map.get(qq);
							sb = new StringBuilder();
							if(owner==null)
							{
								//System.out.println("没有内容？");
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
							//System.out.println("服务器转发"+content);
							if(content!=null)
							{
								try{
									//c_byte = content.getBytes();
									//PrintStream ps= new PrintStream(out);
									//ps.println(content);
									//ps.flush();
									//ps.close();
									System.out.println(client.getRemoteSocketAddress()+"==发送前=="+ System.currentTimeMillis());
									writer.write(content);
									System.out.println(client.getRemoteSocketAddress()+"==发送后=="+ System.currentTimeMillis());
									writer.flush();
									writer.close();
									reader.close();
									buffin.close();
									buffout.close();
									in.close();
									out.close();


								}catch(SocketException e){
									System.out.println("中途关闭了？");
									client.close();
									return;
								}
								owner.remove(toqq,content);
								//这个删除有待商榷

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

