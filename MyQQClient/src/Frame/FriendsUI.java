package Frame;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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

 
public class FriendsUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private User owner;// 当前用户
	private Client client;// 客户端
    private JButton changepwd_bt;
    private JButton addfriends_bt;
    private JButton world_bt;
    
	public FriendsUI(User owner, Client client) {
		this.owner = owner;
		this.client = client;
		//初始化界面
		init();
		
		setTitle(owner.getUsername() + "-在线");
		setSize(260, 670);
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
	private void init() {
		// TODO Auto-generated method stub
		//登录成功后上部分，包括头像， 用户名， 个性签名， 这里个性签名固定
		final JPanel upper_N = new JPanel();
		upper_N.setLayout(new BorderLayout()); // 采用边界布局
		add(upper_N, BorderLayout.NORTH);
		
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
		
		final JLabel upper_N_Cen_S = new JLabel(); // 个性签名部分
		upper_N_Cen_S.setText("Welcome to SDUST");
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
		
		changepwd_bt = new JButton();
		down_S_W.add(changepwd_bt);
		changepwd_bt.setText("修改密码");
		changepwd_bt.addActionListener(this);
		
		final JTabbedPane jtp = new JTabbedPane();
		add(jtp, BorderLayout.CENTER);
		
		final JPanel friend_pal = new JPanel();
		final JPanel world_propaganda = new JPanel();
		
		int friendsnum = owner.getFriendsNum();
		friend_pal.setLayout(new GridLayout(50, 1, 4, 4));
		final JLabel friendsname[];// 虚拟出我的好友 = new JLabel[];
		friendsname = new JLabel[friendsnum];
		//五张好友头像 
		ImageIcon icon[] = new ImageIcon[5];
		for(int i = 0; i < 5; ++i) {
			//System.out.println("image/friendsui/" + Integer.toString(i) + ".jpg");
			icon[i] = new ImageIcon((String)("image/friendsui/" + Integer.toString(i) + ".jpg"));
			icon[i].setImage(icon[i].getImage().getScaledInstance(75, 75,
					Image.SCALE_DEFAULT));
		}	
		String insert = new String();
		ArrayList<String> friendslist = new ArrayList<String>(owner.getFriend());
		for (int i = 0; i < friendsnum; ++i) {
			// 设置icon显示位置在jlabel的左边
			insert = (String)friendslist.get(i);
			while(insert.length() < 38) {
				insert = (String)(insert + " ");
			}
			friendsname[i] = new JLabel(insert, icon[i % 5], JLabel.CENTER);
			friendsname[i].addMouseListener(new MyMouseListener());
			friend_pal.add(friendsname[i]);

		}
		
		//世界喊话部分
		ImageIcon world_image = new ImageIcon("image/friendsui/world.jpg");
		world_image.setImage(world_image.getImage().getScaledInstance(245, 493, Image.SCALE_DEFAULT));
		world_bt = new JButton();
		world_bt.setIcon(world_image);
		world_bt.setBackground(Color.white);
		world_bt.setBorderPainted(false); //如果进度条应该绘制边框，则为 true；否则为 false
		world_bt.setBorder(null); //设置此组件的边框 无
		world_bt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //将光标设为 “小手”形状
		world_bt.addActionListener(this);
		world_propaganda.add(world_bt);
		
		
		final JScrollPane jsp = new JScrollPane(friend_pal);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jtp.addTab("我的好友", jsp);
		jtp.addTab("世界喊话", world_propaganda);
		
		//窗口关闭事件
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				cmd.setReceiver("Server");
				cmd.setSender(owner.getUsername());
				client.sendData(cmd);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				CommandTranser cmd = new CommandTranser();
				cmd.setCmd("logout");
				cmd.setReceiver("Server");
				cmd.setSender(owner.getUsername());
				client.sendData(cmd);
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
			ChatUI chatUI = ChatUIList.getChatUI("WorldChat");
			if(chatUI == null) {
				chatUI = new ChatUI("WorldChat", "WorldChat", owner.getUsername(), client);
				ChatUIEntity chatUIEntity = new ChatUIEntity();
				chatUIEntity.setName("WorldChat");
				chatUIEntity.setChatUI(chatUI);
				ChatUIList.addChatUI(chatUIEntity);
			} else {
				chatUI.show(); //如果以前创建过仅被别的窗口掩盖了 就重新显示
			}
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
				//System.out.println(friendname + "*");
				//查看与该好友是否创建过窗口
				ChatUI chatUI = ChatUIList.getChatUI(friendname);
				if(chatUI == null) {
					chatUI = new ChatUI(owner.getUsername(), friendname, owner.getUsername(), client);
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
