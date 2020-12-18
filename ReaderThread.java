//package application;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class ReaderThread implements Runnable {
//
//    private final BufferedReader inFromServer;
////    private static final String DISCONNECT_COMMAND = "disconnect";
//    private static final Logger LOGGER = Logger.getLogger(ReaderThread.class.getName());
//
//    public ReaderThread(final BufferedReader inFromServer) {
//        this.inFromServer = inFromServer;
//    }
//
//    @Override
//    public void run() {
//    	while (true) {
//			try {
//				String reply = inFromServer.readLine();
//				serverReply = reply;
//				
//				if(reply.startsWith("MSG"))
//				{
//					reply = reply.replaceFirst("MSG", "");
//					chatText = chatText + reply + "\n";
//					txtChat.setText(chatText);
//				}
//				
//			} catch (final IOException e) {
//				LOGGER.log(Level.INFO, "Error occured while reading server response");
//				LOGGER.log(Level.INFO, e.getMessage(), e);
//				return null;
//			}
//		}W
//    	
////    	System.out.println("asd");
//    }
//}
