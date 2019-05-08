package _Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

 
public class Service {
	public void startService() {
		try {
			// 服务端在2222端口监听客户端请求的TCP连接
			ServerSocket ss = new ServerSocket(2222);
			Socket socket = null;
			
			while(true) {//循环监听
	            // 等待客户端的连接
				socket = ss.accept();
				// 为每个客户端连接开启一个线程
				ServerThread thread = new ServerThread(socket);
				thread.start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
