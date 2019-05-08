package _Util;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import Entity.SocketEntity;


public class SocketList {
	ArrayList<String> all_online = null;
	private static HashMap<String, Socket> map = new HashMap<String, Socket>();
	private static HashMap<String, String> map_net = new HashMap<String, String>();
	
	public static void addSocket(SocketEntity socketEntity) {
		map.put(socketEntity.getName(), socketEntity.getSocket());	
	}
	
	//通过昵称返回socket 类比socklist在客户端创建 ChatUIList
	public static Socket getSocket(String name) {
		return map.get(name);
	}
	
	public static HashMap<String, Socket> getMap(){
		return map;
	}
	
	public static void deleteSocket(String name) {
		if(map.get(name) != null) {
			map.remove(name);
		}
		return;
	}
	
	public static Set getAllOnline(){
		return map.keySet();
	}
	
	public static void addNet(String name, String net) {
		map_net.put(name, net);	
	}
	
	
	public static String getNet(String name) {
		return map_net.get(name);
	}
	
	public static void deleteNet(String name) {
		if(map_net.get(name) != null) {
			map_net.remove(name);
		}
		return;
	}
	
	public static HashMap<String, String> getMap_net(){
		return map_net;
	}
}
