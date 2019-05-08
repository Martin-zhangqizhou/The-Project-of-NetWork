package Frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.*;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Entity.User;
import UserSocket.Client;
import _Util.ChatUIList;
import _Util.CommandTranser;
/*
 * 消息框
 */
 
public class ChatUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextPane chat_windows; //双方交流信息的文本框
	private JTextField message_txt; //写信息的文本框
	private JButton send_btn; //发送按钮
	private JButton img_btn;
	private JPanel panel;
	private String owner_name;
	private String friend_name;
	private String who;
	private User owner;
	private Client client; //在发消息用到， 收消息不用这个处理
	private StyledDocument doc; 
	private int width = 500;
	private int height = 500;
	//private ChatTread thread; //接受信息线程
	
	public ChatUI(String ower_name, String friend_name, String who, Client client, User owner){
		this.owner_name = ower_name;
		this.friend_name = friend_name;
		this.client = client;
		this.who = who;
		this.owner = owner;
		//生成聊天页面
		init();
		
		setTitle(ower_name + "正在和" + friend_name + "聊天");
		setSize(width, height);
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
		img_btn = new JButton("选择图片");
		/*
		JFileChooser chooser = new JFileChooser("选择图片");//创建一个文件选择器
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "JPG & PNG Images", "jpg", "png");//创建这个文件的规范
		File f =null;
		chooser.setFileFilter(filter);//加上这个文件规范
		    */
		
		panel.add(message_txt);
		panel.add(send_btn);
		panel.add(img_btn);
		chat_windows = new JTextPane();
		chat_windows.setEditable(false);
		chat_windows.add(new JScrollBar(JScrollBar.VERTICAL)); //滚动条
		add(chat_windows, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);
		send_btn.addActionListener(this);
		img_btn.addActionListener(this);
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
	/*
	private void insertIcon(File file) {
		  chat_windows.setCaretPosition(chat_windows.getLength()); // 设置插入位置
		  chat_windows.insertIcon(new ImageIcon(file.getPath())); // 插入图片
		  insert(new FontAttrib()); // 这样做可以换行
	}*/
	//发送方消息
	public JTextPane getChatWin() {
		return chat_windows;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//点击发送按钮
		if(e.getSource() == send_btn) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss a");
			
			String message = "我说 : " + message_txt.getText() + "\t"
					+ sdf.format(date) + "\n";
			//聊天框添加消息
			//chat_windows.setText(chat_windows.getText());
			//chat_windows.setText(chat_windows.getText()+message);
			doc = chat_windows.getStyledDocument();
			try {
				doc.insertString(doc.getLength(), message, null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chat_windows.setCaretPosition(doc.getLength());//用来接到后面的位置  
			//数据
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("message");
			if("WorldChat".equals(owner_name)) {
				cmd.setCmd("WorldChat");
			}
			cmd.setSender(who);
			cmd.setReceiver(friend_name);
			cmd.setData(message_txt.getText());
			
			ArrayList<String> friends = owner.getFriend();
			cmd.setFriends(friends);
			//发送
			client.sendData(cmd);
			
			//发送完消息后清除输入框的内容
			message_txt.setText(null);
		}else if(e.getSource() == img_btn) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss a");
			
			String message = "我说 : " + message_txt.getText() + "\t"
					+ sdf.format(date) + "\n";
			CommandTranser cmd = new CommandTranser();
			cmd.setCmd("message");
			if("WorldChat".equals(owner_name)) {
				cmd.setCmd("WorldChat");
			}
			cmd.setSender(who);
			cmd.setReceiver(friend_name);
			ArrayList<String> friends = owner.getFriend();
			cmd.setFriends(friends);
			
			System.out.println("发送图片");
			JFileChooser chooser = new JFileChooser();//创建一个文件选择器
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "JPG & PNG Images", "jpg", "png");//创建这个文件的规范
		    File f =null;
		    chooser.setFileFilter(filter);//加上这个文件规范
		    int returnVal = chooser.showOpenDialog( new  TextField());//返回值 点确定
		    System.out.println(returnVal);
		    if(returnVal == JFileChooser.APPROVE_OPTION) { //判断是否选择为确定
		    	f= chooser.getSelectedFile(); //选择的文件 返回给 File
		          //  chooser.getSelectedFile();
			    String flujin =f.getParent()+"\\"+f.getName();//将文件的路径 给变为String
			    System.out.print(flujin);
			    ImageIcon i = new ImageIcon(flujin); //创建一个ImageIcon 并给一个这个文件an的路径
			    i.setImage(i.getImage().getScaledInstance(110,100,Image.SCALE_DEFAULT));//设置（图片）文件的大小
			    //chat_windows.setText(chat_windows.getText()+"我说：");
			    doc = chat_windows.getStyledDocument();
				try {
					doc.insertString(doc.getLength(), "我说：", null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				chat_windows.setCaretPosition(doc.getLength());//用来接到后面的位置  
			    chat_windows.insertIcon(i);
			    doc = chat_windows.getStyledDocument();
				try {
					doc.insertString(doc.getLength(), sdf.format(date) + "\n", null);
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				chat_windows.setCaretPosition(doc.getLength());
				cmd.setData(i);
				client.sendData(cmd);
		    }
		}
	}
}
