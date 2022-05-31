import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

/**
 * This class (Client) is the class of the TicTacToe game Server to handle user action and receive/send response from/to client
 * @author Lian Ho Yeung (3035714260)
 */

public class TicTacToeServer {

	ServerSocket serverSock;
	ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
	int player_counter=0;
	int connect_player_counter =0;
	ArrayList<Integer> p1_occupied;
	ArrayList<Integer> p2_occupied;
	ArrayList< ArrayList<Integer> > win_array; 
	
	/**
	 *This method will start the Server to handle user action and receive/send response from/to client
	 */
	public void go() {  
		
		win_array = new ArrayList< ArrayList<Integer> >();
		ArrayList<Integer> win1= new ArrayList<Integer>();
		win1.add(0);
		win1.add(1);
		win1.add(2);
		
		ArrayList<Integer> win2= new ArrayList<Integer>();
		win2.add(3);
		win2.add(4);
		win2.add(5);
		
		ArrayList<Integer> win3= new ArrayList<Integer>();
		win3.add(6);
		win3.add(7);
		win3.add(8);
		
		ArrayList<Integer> win4= new ArrayList<Integer>();
		win4.add(0);
		win4.add(3);
		win4.add(6);
		
		ArrayList<Integer> win5= new ArrayList<Integer>();
		win5.add(1);
		win5.add(4);
		win5.add(7);
		
		ArrayList<Integer> win6= new ArrayList<Integer>();
		win6.add(2);
		win6.add(5);
		win6.add(8);
		
		ArrayList<Integer> win7= new ArrayList<Integer>();
		win7.add(0);
		win7.add(4);
		win7.add(8);
		
		ArrayList<Integer> win8= new ArrayList<Integer>();
		win8.add(2);
		win8.add(4);
		win8.add(6);
		
		win_array.add(win1);
		win_array.add(win2);
		win_array.add(win3);
		win_array.add(win4);
		win_array.add(win5);
		win_array.add(win6);
		win_array.add(win7);
		win_array.add(win8);
		
		
		
		p1_occupied = new ArrayList<Integer>();
		p2_occupied = new ArrayList<Integer>();
		
		try {
			serverSock = new ServerSocket(5000);
			System.out.println("Server is running...");
			
			while (true) {
				//System.out.println("test");
				if (connect_player_counter<2) 
				{
					Socket sock = serverSock.accept();
					System.out.println("Server is connected to client");
					ClientHandler clientHandler = new ClientHandler(sock);
					Thread clientThread = new Thread(clientHandler);
					clientThread.start();
					connect_player_counter++;
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} 
		
	}
	
	class ClientHandler implements Runnable
	{
		private Socket sock;
		
		public ClientHandler(Socket sock) {
			this.sock = sock;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);  
				writers.add(writer);
				
				InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());  
				BufferedReader reader = new BufferedReader(streamReader); 
				
				String command;
				while ((command = reader.readLine()) != null) 
				{

					System.out.println("Command from client: " + command);
					
					if (command.startsWith("ready") && player_counter <2) 
					{
						player_counter++;
						for (PrintWriter eachWriter: writers) {
							eachWriter.println(player_counter);
						}
						System.out.println("Server is broadcasting " + " Player_counter=" +player_counter);
					}
					
					else if (command.startsWith("p1") || command.startsWith("p2")) 
					{
						String[] cmd_parts = command.split(" ");
						if (command.startsWith("p1"))
						{
							p1_occupied.add(Integer.parseInt(cmd_parts[1]));
							p1_occupied.sort(null);
						}
						else 
						{
							p2_occupied.add(Integer.parseInt(cmd_parts[1]));
							p1_occupied.sort(null);
						}
						
						boolean win_trigger = false;
						{
							for (int i = 0; i < win_array.size(); i++) 
							{
								ArrayList<Integer> win = win_array.get(i);
								if (p1_occupied.containsAll(win) || p2_occupied.containsAll(win))
								{
									command = "win "+ command;
									win_trigger = true;
								}
								
							}
							if (win_trigger == false && p1_occupied.size()+ p2_occupied.size() ==9) 
							{
								command = "draw " + command;
							}
						}
						
						
						for (PrintWriter eachWriter: writers) 
						{
							eachWriter.println(command);
						}
						
						
						//Check win then
						System.out.println("Server is broadcasting " + command);
						p1_occupied.sort(null);
						p1_occupied.sort(null);
						if (command.startsWith("win") || command.startsWith("draw")) {
							player_counter=0;
							connect_player_counter =0;
							p1_occupied = new ArrayList<Integer>();
							p2_occupied = new ArrayList<Integer>();
							this.sock.shutdownInput();
							this.sock.shutdownOutput();
							Thread.currentThread().interrupt();
						}
					}
					
					else if (command.startsWith("Player exit")) 
					{
						player_counter=0;
						connect_player_counter =0;
						p1_occupied = new ArrayList<Integer>();
						p2_occupied = new ArrayList<Integer>();
						command = "Exit";
						for (PrintWriter eachWriter: writers) 
						{
							eachWriter.println(command);
						}
						this.sock.shutdownInput();
						this.sock.shutdownOutput();
						Thread.currentThread().interrupt();
					}
					
					
					
				}				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 *This method will ensure the variable of player_counter will increase at the same time when player join 
	 */
	public synchronized void player_join() 
	{
		player_counter++;
	}
	
	/**
	 *This method is used to start the server
	 */
	public static void main(String[] args) throws IOException
	{

			TicTacToeServer server = new TicTacToeServer();  
			server.go();

	}
}
