package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatHistoryService {
	
	public String createChatHistory(
			String chatText, 
			Connection connection, 
			String selectedUser, 
			String user) throws SQLException {
		
		chatText = "";
		
		UserData u = new UserData(connection);
		User receiverUser = u.getUser(selectedUser);
		User senderUser = u.getUser(user);
		
		List<Message> senderMsgs = u.getMessages(receiverUser, senderUser);
		
		List<Message> receiverMsgs = u.getMessages(senderUser, receiverUser);
		
		for(Message m : senderMsgs)
		{
			m.setSenderName("You");
		}
		
		for(Message m : receiverMsgs)
		{
			m.setSenderName(selectedUser);
		}
		
		List<Message> allMsgs = new ArrayList<>();
		allMsgs.addAll(senderMsgs);
		allMsgs.addAll(receiverMsgs);
		Collections.sort(allMsgs);
		
		for(Message m : allMsgs)
		{
			String content = m.getContent();
			content = DateUtils.getTimeToString(m.getDate()) + " " + m.getSenderName() + ": " + m.getContent();
			chatText = chatText + content + "\n";
		}
		
		chatText = chatText + "				^Previous messages^				" + "\n";
//		txtChat.setText(chatText);
		
		return chatText;
	}
}
