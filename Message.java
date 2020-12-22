package application;

import java.sql.Timestamp;

public final class Message implements Comparable<Message> {
	private String content;
	private Timestamp date;
	private String senderName;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public Message(String content, Timestamp date) {
		super();
		this.content = content;
		this.date = date;
	}
	@Override
	public int compareTo(Message o) {
		return getDate().compareTo(o.getDate());
	}
	@Override
	public String toString() {
		return "Message [content=" + content + ", date=" + date + "]";
	}
}
