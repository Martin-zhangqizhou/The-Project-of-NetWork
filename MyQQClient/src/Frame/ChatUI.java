package Frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import UserSocket.Client;
import _Util.ChatUIList;
import _Util.CommandTranser;

/**
* @author zzz
* @version 创建时间：2018年7月4日 下午1:41:36
*/
public class ChatUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextArea chat_windows; //双方交流信息的文本框
	private JTextField message_txt; //写信息的文本框
	private JButton send_btn; //发送按钮
	private JPanel panel;
	private String owner_name;
	private String friend_name;
	private String who;
	private Client client; //在发消息用到， 收消息不用这个处理
	//private ChatTread thread; //接受信息线程
	
	public ChatUI(String ower_name, String friend_name, String who, Client client){
		this.owner_name = ower_name;
		this.friend_name = friend_name;
		this.client = client;
		this.who = who;
		
		//生成聊天页面
		init();
		
		setTitle(ower_name + "正在和" + friend_name + "聊天");
		setSize(350, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		
		// 开启客户端接收信息线程,将收到的消息反馈到 chat_windows
		//thread = new ChatTread(client); //将唯一client传入
		//thread.start();
	}
	
	private void init() {
		setLayout(new BorderLayout());
		panel = new JPanel();
		message_txt = new JTextField(20);
		send_btn = new JButton("发送");
		panel.add(message_txt);
		panel.add(send_btn);
		chat_windows = new JTextArea();
		chat_windows.setEditable(false);
		chat_windows.add(new JScrollBar(JScrollBar.VERTICAL)); //滚动条
		add(chat_windows, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		send_btn.addActionListener(this);
		
		//窗口关闭事件
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ChatUIList.deletChatUI(friend_name);
			}

			@Override
			public void windowClosed(WindowEvent e) {
				ChatUIList.deletChatUI(friend_name);
			}
		});
	}
	//发送方消息
	public JTextArea getChatWin() {
		return chat_windows;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//点击发送按钮
		if(e.getSource() == send_btn) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss a");
			
			String message = "你说 : " + message_txt.getText() + "\t"
					+ sdf.format(date) + "\n";
			//聊天框添加消息
			chat_windows.append(message);
			
			//数据
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("message");
			if("WorldChat".equals(owner_name)) {
				cmd.setCmd("WorldChat");
			}
			cmd.setSender(who);
			cmd.setReceiver(friend_name);
			cmd.setData(message_txt.getText());
			
			//发送
			client.sendData(cmd);
			
			//发送完消息后清除输入框的内容
			message_txt.setText(null);
		}
	}
	
}
