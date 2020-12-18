package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainScreenController implements Initializable {

	private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 6543;
    private static final Logger LOGGER = Logger.getLogger(MainScreenController.class.getName());
	
    private Socket socket;
	private BufferedReader inFromServer;
	private PrintWriter outToServer;
	private Scanner scanner;
	
	private String user;
	
	private String serverReply;
	
	private String chatText = "";
	
	@FXML
	private Text txtLoggedInAs;
	
	@FXML
	private Text txtChattingWith;
	
	@FXML
	private ListView<String> listUsers;
	
	@FXML
	private Button btnOpenChat;
	
	@FXML
	private Button btnLoadUsers;
	
	@FXML
	private TextArea txtChat; 
	
	@FXML 
	private TextField txtMsg;
	
	@FXML
	private Label userNameLabel;
	
	private String selectedUser;
	
	
	Connection connection = null;
	
	public MainScreenController() throws UnknownHostException, IOException {
		connection = ConnectionConfiguration.getConnection();
	}
	
	public void connect(ActionEvent event) throws IOException
	{
		initData(selectedUser, socket, inFromServer, outToServer, scanner);
		txtChattingWith.setText("You are chatting with: " + selectedUser);
		outToServer.println("CONNECTTO" + " " + selectedUser);
		chatText = "";
		txtChat.clear();
		
	}
	
	public void exit(ActionEvent event) throws IOException
	{
		outToServer.println("CLOSE" + " " + user);
		Platform.exit();
		
	}
	
	public void send(ActionEvent event) throws IOException
	{
		
		String msg = txtMsg.getText().toString();
		
		if(msg.length()>0)
		{
			outToServer.println(Command.MSG + " " + user + ": " + msg);
			chatText = chatText + "You: " + msg + "\n";
			txtChat.setText(chatText);
			txtMsg.clear();
		}
		
	}
	
	private void initData(String chatWith, Socket socket, BufferedReader inFromServer, PrintWriter outToServer, Scanner scanner)
	{
		this.socket = socket;
		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
		this.scanner = scanner;
		
		selectedUser = chatWith;
	}
	
	public void loadUsers(ActionEvent event) throws IOException
	{
		outToServer.println(Command.LOADUSERS);
		List<String> users = new ArrayList<>();
		if(serverReply != null)
		{
			char[] chars = serverReply.toCharArray();
			if(chars.length != 0 && chars[0] == 'L')
			{
				String[] msg = serverReply.split("/");
				for( int i = 1; i < msg.length; i++)
				{
					users.add(msg[i]);
				}
				
			}
			
			ObservableList<String> items =FXCollections.observableArrayList ();
			items.addAll(users);
			listUsers.setItems(items);
			
		}
		
	}
	
	public void initUser(String user)
	{
		this.user = user;
		userNameLabel.setText(user);
	}
	
	public void initConnention() throws UnknownHostException, IOException
	{
		socket = new Socket(SERVER_HOST, SERVER_PORT);
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outToServer = new PrintWriter(socket.getOutputStream(), true);

		System.out.println("Connected to the server.");

		outToServer.println(user);

		Thread th = new Thread(new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while (true) {
					try {
						String reply = inFromServer.readLine();
						serverReply = reply;
						
						if(reply.startsWith("MSG"))
						{
							reply = reply.replaceFirst("MSG", "");
							chatText = chatText + reply + "\n";
							txtChat.setText(chatText);
						}
						
					} catch (final IOException e) {
						LOGGER.log(Level.INFO, "Error occured while reading server response");
						LOGGER.log(Level.INFO, e.getMessage(), e);
						return null;
					}
				}
			}

		});
		th.setDaemon(true);
		th.start();

	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		listUsers.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ov, 
		                    String old_val, String new_val) {
		                        selectedUser = new_val;
		                        btnOpenChat.setDisable(false);
				}
		});

	}

}
