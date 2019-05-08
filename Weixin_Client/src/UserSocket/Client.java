package UserSocket;
 
/*
 * 客户端与服务器交互的部分，包括客户端的发送方法和接收方法。
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import _Util.CommandTranser;

public class Client {
	private int port = 2222; 
	private String Sever_address = "192.168.43.110";//"172.23.0.1"; // //服务器主机ip
	private Socket socket;
	
	//实例化， 建立连接
	public Client(){
		try {
			socket = new Socket(Sever_address, port);
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	} 
	
	//向服务端发送数据
	public void sendData(CommandTranser cmd) {
		ObjectOutputStream oos = null; //主要的作用是用于写入对象信息与读取对象信息。 对象信息一旦写到文件上那么对象的信息就可以做到持久化了
		try {
			if(socket == null) {
				return;
			}
			oos = new ObjectOutputStream(socket.getOutputStream());//getOutputStream()方法连接的另一端将得到输入，同时返回一个OutputStream对象实例
			oos.writeObject(cmd);//把cmd数据传到另一端
		} catch(UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "服务器端未开启");
		}
	}
	
	//接受服务端发送的消息
	public CommandTranser getData() {
		ObjectInputStream ois = null;
		CommandTranser cmd = null;
		if(socket == null) {
			//System.out.println("weishenme");
			return null;
		}
		try {
			ois = new ObjectInputStream(socket.getInputStream());//getInputStream()方法获得网络连接输入，同时返回一个IutputStream对象实例
			cmd = (CommandTranser) ois.readObject();
			 
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		 
//		if("message".equals(cmd.getCmd())) {
//			System.out.println((String)cmd.getData());
//		}
		
		return cmd;
	}

}
