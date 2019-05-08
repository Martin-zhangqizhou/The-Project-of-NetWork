package Frame;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.net.InetAddress;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import Entity.ChatUIEntity;
import Entity.User;
import UserSocket.Client;
import _Util.ChatUIList;
import _Util.CommandTranser;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
/*
 *  头像部分应该一个用户对应一个，待解决？？？最好是用户可以设置图像，初始为同一个图像
 *  //个性签名需要用户自定义，待改进？？？初始为同意个个性签名（也可以不设个性签名)
 */

public class FriendsUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private User owner;// 当前用户
	private Client client;// 客户端
    private JButton changepwd_bt;
    private JButton addfriends_bt;
    private JButton world_bt;
    private JButton udp_bt;
    private DatagramSocket socket;
    private DatagramSocket serversocket;
    private int width = 400;
    private int height = 670;
    private int MAX = 11;
    
	public FriendsUI(User owner, Client client) throws IOException {
		this.owner = owner;
		this.client = client;
		//初始化界面
		init();
		//receive_udp();
		
		setTitle(owner.getUsername() + "-在线");
		setSize(width, height);
		setLocation(1050, 50);
		ImageIcon logo = new ImageIcon("image/friendsui/login_successful_image.jpg"); //左上方小图标
		setIconImage(logo.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

	// 界面
	/*
	 * 又了解了一下 流式布局 和 边框（界）布局 分别见参考
	 * https://blog.csdn.net/liujun13579/article/details/7771191
	 *  https://blog.csdn.net/liujun13579/article/details/7772215
	 */
	//这里主体参考的 https://www.cnblogs.com/qingyundian/p/8012527.html
	
	@SuppressWarnings("unused")
	private void receive_udp() throws IOException{
		//监听端口20001
		serversocket = new DatagramSocket(20002);
		String response = "收到了";
		byte[] arr = new byte[1024];
	    DatagramPacket packet = new DatagramPacket(arr, arr.length);
	    
	    serversocket.receive(packet);
	    
	    byte[] arr1 = packet.getData();
	    String s = new String(arr1);
	    System.out.println(s);
		serversocket.close();
	}
	
	private void init() throws IOException {
		
		//登录成功后上部分，包括头像， 用户名， 个性签名， 这里个性签名固定
		final JPanel upper_N = new JPanel();
		upper_N.setLayout(new BorderLayout()); // 采用边界布局
		add(upper_N, BorderLayout.NORTH);
		
		//头像部分应该一个用户对应一个，待解决？？？
		ImageIcon my_avata = new ImageIcon("image/friendsui/sdust.jpg"); //头像部分
		final JLabel upper_N_W = new JLabel(my_avata);
		upper_N.add(upper_N_W, BorderLayout.WEST);
		upper_N_W.setPreferredSize(new Dimension(79, 79));
		
		final JPanel upper_N_Cen = new JPanel(); 
		upper_N_Cen.setLayout(new BorderLayout());
		upper_N.add(upper_N_Cen, BorderLayout.CENTER);
		
		
		final JLabel upper_N_Cen_Cen = new JLabel(); //用户名部分
		upper_N_Cen_Cen.setText(owner.getUsername());
		upper_N_Cen_Cen.setFont(new Font("隶书", 1, 16));
		upper_N_Cen.add(upper_N_Cen_Cen, BorderLayout.CENTER);
		
		//个性签名需要用户自定义，待改进？？？
		final JLabel upper_N_Cen_S = new JLabel(); // 个性签名部分
		upper_N_Cen_S.setText("Hello World!");
		upper_N_Cen.add(upper_N_Cen_S, BorderLayout.SOUTH);
		
		
		//登陆成功后下部分 修改密码, 添加好友
		final JPanel down_S = new JPanel();
		down_S.setLayout(new BorderLayout());
		add(down_S, BorderLayout.SOUTH);
		
		final JPanel down_S_W = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		down_S_W.setLayout(flowLayout);
		down_S.add(down_S_W); //下面的EAST已删除,不用标识位置了 
		
		
		addfriends_bt = new JButton(); //这里待测试 去掉 set...
		down_S_W.add(addfriends_bt);
		addfriends_bt.setHorizontalTextPosition(SwingConstants.LEFT);
		addfriends_bt.setHorizontalAlignment(SwingConstants.LEFT);
		addfriends_bt.setText("添加好友");
		addfriends_bt.addActionListener(this);
		
		udp_bt = new JButton();
		down_S_W.add(udp_bt);
		udp_bt.setText("局域网搜索");
		udp_bt.addActionListener(this);
		
		changepwd_bt = new JButton();
		down_S_W.add(changepwd_bt);
		changepwd_bt.setText("修改密码");
		changepwd_bt.addActionListener(this);
		
		final JTabbedPane jtp = new JTabbedPane();
		add(jtp, BorderLayout.CENTER);
		
		final JPanel friend_pal = new JPanel();
		final JPanel world_propaganda = new JPanel();
		final JPanel UDP_search = new JPanel();
		
		//朋友列表
		int friendsnum = owner.getFriendsNum();//获取我的好友的数量
		friend_pal.setLayout(new GridLayout(50, 1, 4, 4));//创建具有指定行数、列数以及组件水平、纵向一定间距的网格布局。
		final JLabel friendsname[];// 虚拟出我的好友 = new JLabel[];
		friendsname = new JLabel[friendsnum];
		
		//
		int usersnum = owner.getAllOnlineNum();
		UDP_search.setLayout(new GridLayout(50, 1, 4, 4));//创建具有指定行数、列数以及组件水平、纵向一定间距的网格布局。
		final JLabel usersname[];// 虚拟出我的好友 = new JLabel[];
		usersname = new JLabel[usersnum];
		
		
		//MAX张好友头像 
		ImageIcon icon[] = new ImageIcon[MAX];
		for(int i = 0; i < MAX; ++i) {
			//System.out.println("image/friendsui/" + Integer.toString(i) + ".jpg");
			icon[i] = new ImageIcon((String)("image/friendsui/" + Integer.toString(i) + ".jpg"));
			icon[i].setImage(icon[i].getImage().getScaledInstance(75, 75,
					Image.SCALE_DEFAULT));
		}	
		//朋友
		String insert = new String();
		ArrayList<String> friendslist = new ArrayList<String>(owner.getFriend());//好友列表
		for (int i = 0; i < friendsnum; ++i) {
			// 设置icon显示位置在jlabel的左边
			insert = (String)friendslist.get(i);//返回在此列表中的指定位置的元素，即用户名字
			while(insert.length() < 38) {//用户名字长度不超过38
				insert = (String)(insert + " ");
			}
			friendsname[i] = new JLabel(insert, icon[i % MAX], JLabel.LEFT);
			friendsname[i].addMouseListener(new MyMouseListener());
			friend_pal.add(friendsname[i]);
		}
		//在线用户
		System.out.println("在线用户数"+usersnum);
		if(usersnum>0){
			String users = new String();
			ArrayList<String> userslist = new ArrayList<String>(owner.getAllOnline());//用户列表
			System.out.println(userslist);
			for (int i = 0; i < usersnum; ++i) {
				// 设置icon显示位置在jlabel的左边
				users = (String)userslist.get(i);//返回在此列表中的指定位置的元素，即用户名字
				if(users.equals(owner.getUsername())){
					continue;
				}
				while(users.length() < 38) {//用户名字长度不超过38
					users = (String)(users + " ");
				}
				System.out.println(users);
				usersname[i] = new JLabel(users, icon[i % MAX], JLabel.LEFT);
				usersname[i].addMouseListener(new MyMouseListener());
				UDP_search.add(usersname[i]);
			}
		}
		
		//世界喊话部分
		ImageIcon world_image = new ImageIcon("image/friendsui/world.jpg");
		world_image.setImage(world_image.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		world_bt = new JButton();
		world_bt.setIcon(world_image);
		world_bt.setBackground(Color.white);
		world_bt.setBorderPainted(false); //如果进度条应该绘制边框，则为 true；否则为 false
		world_bt.setBorder(null); //设置此组件的边框 无
		world_bt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //将光标设为 “小手”形状
		world_bt.addActionListener(this);
		world_propaganda.add(world_bt, BorderLayout.CENTER);
		
		
		final JScrollPane jsp = new JScrollPane(friend_pal);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		jtp.addTab("我的好友", jsp);
		jtp.addTab("群发消息", world_propaganda);
		
		final JScrollPane udp = new JScrollPane(UDP_search);
		udp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jtp.addTab("局域网通信", udp);
		
		//窗口关闭事件
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("logout");
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				cmd.setData("hand");
				cmd.setReceiver("Server");
				cmd.setSender(owner.getUsername());
				client.sendData(cmd);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("更新消息框");
				/*
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				cmd.setData("no_hand");
				cmd.setReceiver("Server");
				cmd.setSender(owner.getUsername());
				client.sendData(cmd);
				*/
			}
		});		
	}
	 
	@Override
	public void actionPerformed(ActionEvent e){
		//点击修改密码 或 添加好友
		
		//修改密码
		if(e.getSource() == changepwd_bt){
			new ChangePwdUI(owner, client);
		}

		//添加好友页面
		if(e.getSource() == addfriends_bt){
			new AddFriendUI(owner, client);
		}

		if(e.getSource() == world_bt) {
			ArrayList<String> friends =  owner.getFriend();
			System.out.println(friends);
			ChatUI chatUI = ChatUIList.getChatUI("WorldChat");
			if(chatUI == null) {
				chatUI = new ChatUI("WorldChat", "WorldChat", owner.getUsername(), client, owner);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName("WorldChat");
				chatUIEntity.setChatUI(chatUI);
				ChatUIList.addChatUI(chatUIEntity);
			} else {
				chatUI.show(); //如果以前创建过仅被别的窗口掩盖了 就重新显示
			}
		}
		
		if(e.getSource() == udp_bt) {
			System.out.println("局域网搜索");
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("all_online");
			cmd.setData(owner.getUsername());
			cmd.setReceiver(owner.getUsername());
			cmd.setSender(owner.getUsername());
			
			client.sendData(cmd);
			System.out.println("命令已发送");
			//ArrayList<String> friends =  owner.getFriend();
			//System.out.println(friends);
			/*
			System.out.println("局域网搜索");
			//1.创建对象
			//构造数据报套接字并将其绑定到本地主机上任何可用的端口。
			try {
				socket = new DatagramSocket();
				String requestData = "check";
				byte[] requestDataBytes = requestData.getBytes();
				
				//构建packet
				DatagramPacket requestPacket = new DatagramPacket(requestDataBytes,
						requestDataBytes.length);
				// 20000端口, 广播地址
		        requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
		        requestPacket.setPort(20002);
				
		        socket.send(requestPacket);
		        socket.close();
		        
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
	}
	
	class MyMouseListener extends MouseAdapter{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			//双击我的好友弹出与该好友的聊天框
			if(e.getClickCount() == 2) {
				JLabel label = (JLabel)e.getSource(); //getSource()返回的是Object,
				
				//通过label中的getText获取聊天对象
				String friendname = label.getText().trim();
				System.out.println(friendname + "*");
				//查看与该好友是否创建过窗口
				ChatUI chatUI = ChatUIList.getChatUI(friendname);
				if(chatUI == null) {
					chatUI = new ChatUI(owner.getUsername(), friendname, owner.getUsername(), client, owner);
					ChatUIEntity chatUIEntity = new ChatUIEntity();
					chatUIEntity.setName(friendname);
					chatUIEntity.setChatUI(chatUI);
					ChatUIList.addChatUI(chatUIEntity);
				} else {
					chatUI.show(); //如果以前创建过仅被别的窗口掩盖了 就重新显示
				}
				
			}	
		}
		
		//鼠标进去好友列表 背景色变色	
		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel label = (JLabel)e.getSource();
			label.setOpaque(true); //设置控件不透明
			label.setBackground(new Color(255, 240, 230));
		}
		
		// 如果鼠标退出我的好友列表 背景色变色
		@Override
		public void mouseExited(MouseEvent e) {
			JLabel label = (JLabel) e.getSource();
			label.setOpaque(false);
			label.setBackground(Color.WHITE);
		}
	}

}
