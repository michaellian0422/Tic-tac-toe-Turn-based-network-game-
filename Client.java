import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * This class (Client) is thhe class of Client to handle user action and server response
 * @author Lian Ho Yeung (3035714260)
 */

public class Client {

	Socket sock;
	PrintWriter writer;
	JTextField name_input;
	JFrame frame;
	JLabel InfoLabel;
	JButton btn_submit;
	Boolean ready_stage = false;
	Boolean your_turn = false;
	ArrayList<JLabel> JLabels;
	ArrayList<JLabel_listener> JLabels_listeners;
	ArrayList<Integer> occupied;
	int Player_seat;
	String Player1_text;
	String Player2_text;
	
	/**
	 *This method will start the Client to handle user actions and server response
	 */
	public void go() 
	{
		JLabels_listeners = new ArrayList <JLabel_listener>();
		occupied = new ArrayList<Integer>();
		Player1_text = "<html><span style='color:red; font-size: 36px'>X</span></html>";
		Player2_text = "<html><span style='color:green; font-size: 36px'>O</span></html>";
		GUI game = new GUI();
		game.goGUI();
		
		JMenuItem exit = game.getexit();
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ready_stage == true) 
				{
					writer.println("Player exit");
				}
				System.exit(0);
			}
		});
		btn_submit = game.get_btn_submit();
		JLabels = game.get_JLabels();
		name_input = game.get_name_input();
		InfoLabel = game.get_InfoLabel();
		frame = game.get_JFrame();
		Player_seat = 0;
		btn_submit.addActionListener(new btn_submit_listener());
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent e) {
		        e.getWindow().dispose();
		        writer.println("Player exit");
		    }
		});
		for (int i=0; i<9; i++) 
		{
			JLabel_listener JLabels_listener = new JLabel_listener();
			JLabels.get(i).addMouseListener(JLabels_listener);
			JLabels_listeners.add(JLabels_listener);
		}
		try 
		{
			sock = new Socket("127.0.0.1", 5000);
			writer = new PrintWriter(sock.getOutputStream(), true);  
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);  
			String command;
			while ((command = reader.readLine()) != null) 
			{
				System.out.println(Player_seat + " Message received from server: " + command);
				if (command.startsWith("Exit")) 
				{
					JOptionPane.showMessageDialog(frame, "Game Ends. One of the players left!");
					your_turn = false;
					writer.println("Player exit");
					System.out.println("Disconnecting");
					System.exit(0);
				}
				//Player1 join
				else if (command.startsWith("1") && Player_seat ==0 && ready_stage ==true) 
				{
					Player_seat = 1;
				}
				
				//Player2 join and game start
				else if (command.startsWith("2") && Player_seat == 0) 
				{
					Player_seat = 2;
					System.out.println("Player 2 joined. Game start.");
					InfoLabel.setText("Wait for opponent.");
				}
				
				//Player1 game start
				else if (command.startsWith("2") && Player_seat == 1) 
				{
					System.out.println("Player 2 joined. Game start.");
					your_turn = true;
					InfoLabel.setText("Your turn.");
				} 
				
				//
				else if (command.startsWith("p1") || command.startsWith("p2"))
				{
					String[] cmd_parts = command.split(" ");
					if (Player_seat==1 ) 
					{
						if (command.startsWith("p2")) 
						{
							occupied.add(Integer.parseInt(cmd_parts[1]));
							JLabels.get(Integer.parseInt(cmd_parts[1])).setText(Player2_text);
							your_turn = true;
							InfoLabel.setText("Your opponent has moved, now is your turn.");
						}
					}
					
					else
					{
						if (command.startsWith("p1")) 
						{
							occupied.add(Integer.parseInt(cmd_parts[1]));
							JLabels.get(Integer.parseInt(cmd_parts[1])).setText(Player1_text);
							your_turn = true;
							InfoLabel.setText("Your opponent has moved, now is your turn.");
						}
					}
				}
				
				//
				else if (command.startsWith("draw")) 
				{
					String[] cmd_parts = command.split(" ");
					if (Player_seat==1 && command.startsWith("draw p1"))
					{
						JOptionPane.showMessageDialog(frame, "Draw!");
					}
					
					else if (Player_seat==1 && command.startsWith("draw p2"))
					{
						JLabels.get(Integer.parseInt(cmd_parts[2])).setText(Player2_text);
						JOptionPane.showMessageDialog(frame, "Draw!");
					}
					
					else if (Player_seat==2 && command.startsWith("draw p2"))
					{
						JOptionPane.showMessageDialog(frame, "Draw!");
					}
					else if (Player_seat==2 && command.startsWith("draw p1"))
					{
						JLabels.get(Integer.parseInt(cmd_parts[2])).setText(Player1_text);
						JOptionPane.showMessageDialog(frame, "Draw!");
					}
					InfoLabel.setText("Draw");
					writer.println("Player exit");
					System.exit(0);
				}
				
				else if (command.startsWith("win")) 
				{
					String[] cmd_parts = command.split(" ");
					if (Player_seat==1 && command.startsWith("win p1"))
					{
						JOptionPane.showMessageDialog(frame, "Congratulations. You win.");
					}
					else if (Player_seat==1 && command.startsWith("win p2"))
					{
						JLabels.get(Integer.parseInt(cmd_parts[2])).setText(Player2_text);
						JOptionPane.showMessageDialog(frame, "You lose!");
					}
					else if (Player_seat==2 && command.startsWith("win p2"))
					{
						JOptionPane.showMessageDialog(frame, "Congratulations. You win.");
					}
					else if (Player_seat==2 && command.startsWith("win p1"))
					{
						JLabels.get(Integer.parseInt(cmd_parts[2])).setText(Player1_text);
						JOptionPane.showMessageDialog(frame, "You lose!");
					}
					InfoLabel.setText("Game Ends.");
					writer.println("Player exit");
					System.exit(0);
				}
				else if (command.startsWith("disconnect")) 
				{
					try {
						sock.shutdownOutput();
						System.out.println("Disconnecting");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
		
			
			
		}
		
		catch (Exception ex) 
		{ 
			ex.printStackTrace(); 
		}
	
		
	}
	
	class JLabel_listener implements MouseListener{
		@Override
		public void mouseEntered(MouseEvent e) 
		{
		}

		@Override
		public void mouseClicked(MouseEvent e) 
		{
			int index = JLabels_listeners.indexOf(this);
			if (your_turn == true && occupied.indexOf(index)==-1) 
			{
				if (Player_seat==1) {
					JLabels.get(index).setText(Player1_text);
				}
				else {
					JLabels.get(index).setText(Player2_text);
				}
				your_turn = false;
				String msg = "p"+ Player_seat + " " +index;
				writer.println(msg);
				System.out.println("Message sent from client: "+msg);
				InfoLabel.setText("Valid move, wait for your opponent.");
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e) {

			
		}
	}
	
	class btn_submit_listener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!name_input.getText().isEmpty()) 
			{
				String Username = name_input.getText();
				frame.setTitle("Tic Tac Toe-Player: "+Username);
				InfoLabel.setText("WELCOME "+Username);
				name_input.setEnabled(false);
				btn_submit.setEnabled(false);
				ready_stage = true;
				System.out.println(Username +" is ready.");
				writer.println("ready");
			}
		}
	}
	
	/**
	 *This method will start the client server
	 *
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client player = new Client();
		player.go();
		
	}

}
