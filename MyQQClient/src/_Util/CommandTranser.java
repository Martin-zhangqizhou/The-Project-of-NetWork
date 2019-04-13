package _Util;

import java.io.Serializable;

/**
* @author zzz
* @version 创建时间：2018年7月4日 上午12:12:43
*/
public class CommandTranser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sender = null;// 发送者
	private String receiver = null;// 接受者
	private Object data = null;// 传递的数据
	private boolean flag = false;// 指令的处理结果
	private String cmd = null;// 服务端要做的指令
	private String result = null;// 处理结果

	public String getSender() {
		return sender;
	}

	public String setSender(String sender) {
		return this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String setReceiver(String receiver) {
		return this.receiver = receiver;
	}

	public Object getData() {
		return data;
	}

	public Object setData(Object data) {
		return this.data = data;
	}

	public boolean isFlag() {
		return flag;
	}

	public boolean setFlag(boolean flag) {
		return this.flag = flag;
	}

	public String getResult() {
		return result;
	}

	public String setResult(String result) {
		return this.result = result;
	}

	public String getCmd() {
		return cmd;
	}

	public String setCmd(String cmd) {
		return this.cmd = cmd;
	}

}

