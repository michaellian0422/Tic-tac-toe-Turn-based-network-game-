import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.util.ArrayList;
/**
 * This class (GUI) is the simple GUI of the card game.
 * @author Lian Ho Yeung (3035714260)
 */

public class GUI{
	ArrayList<JLabel> JLabels;
	JTextField name_input;
	JButton btn_submit;
	JLabel InfoLabel;
	JFrame frame;
	JMenuItem menuitem_exit;
	
	/**
	 *This method start the GUI.
	 */
	public void goGUI() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu_control = new JMenu("Control");
		menuitem_exit = new JMenuItem("Exit");
		menu_control.add(menuitem_exit);
		
		JMenu menu_help = new JMenu("Help");
		JMenuItem menuitem_help = new JMenuItem("Instruction");
		
		
		menu_help.add(menuitem_help);
		menuBar.add(menu_control);
		menuBar.add(menu_help);
		
		JLabels = new ArrayList<JLabel>();
		for (int i=0; i<9;i++)
		{
			JLabels.add(new JLabel());
		}
		
		JLabels.get(0).setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, Color.BLACK));
		JLabels.get(1).setBorder(BorderFactory.createMatteBorder(0, 5, 5, 5, Color.BLACK));
		JLabels.get(2).setBorder(BorderFactory.createMatteBorder(0, 5, 5, 0, Color.BLACK));
		JLabels.get(3).setBorder(BorderFactory.createMatteBorder(5, 0, 5, 5, Color.BLACK));
		JLabels.get(4).setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
		JLabels.get(5).setBorder(BorderFactory.createMatteBorder(5, 5, 5, 0, Color.BLACK));
		JLabels.get(6).setBorder(BorderFactory.createMatteBorder(5, 0, 0, 5, Color.BLACK));
		JLabels.get(7).setBorder(BorderFactory.createMatteBorder(5, 5, 0, 5, Color.BLACK));
		JLabels.get(8).setBorder(BorderFactory.createMatteBorder(5, 5, 0, 0, Color.BLACK));
		
		
		for (int i=0; i<9;i++)
		{
			JLabels.get(i).setHorizontalAlignment(SwingConstants.CENTER);
			JLabels.get(i).setBackground(Color.white);
			JLabels.get(i).setOpaque(true);
		}
		
		JPanel MainPanel = new JPanel();
		MainPanel.setLayout(new GridLayout(3,3));
		for (int i=0; i<9; i++) 
		{
			MainPanel.add(JLabels.get(i));
		}
		

		JPanel InputPanel = new JPanel();
		name_input = new JTextField (20);
		btn_submit = new JButton ("Submit");
		InputPanel.add(name_input);
		InputPanel.add(btn_submit);
		
		InfoLabel = new JLabel("Enter your player name");
		
		
		frame = new JFrame();
		frame.setJMenuBar(menuBar);
		frame.add(MainPanel);
		frame.add(InfoLabel, BorderLayout.NORTH);
		frame.add(InputPanel, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("A Simple Card Game");
		frame.setSize(500, 500);
		
		
		menuitem_help.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(frame, "<html>Some information about the game:<br>"
						+ "<html>Criteria for a valid move:<br>"
						+ "<html>-The move is not occuppied by any mark.<br>"
						+ "<html>-The move is made in the player's turn.<br>"
						+ "<html>-THe move is made within the 3 x 3 board.<br>"
						+ "<html>-The game would continue and switch among the opposite player until it reaches either one of the following conditions:<br>"
						+ "<html>-Player 1 wins.<br>"
						+ "<html>-Player 2 wins.<br>"
						+ "<html>-Draw.");
			}
		});
		
		frame.setVisible(true);
		
		
	}
	
	/**
	 *This method will return of a arraylist of the JLabel of each block of the card-game
	 * @return a arraylist of the JLabel, which contain each block of the card-game
	 */
	public ArrayList<JLabel> get_JLabels() {
		return JLabels;
	}
	
	/**
	 *This method will return of the JtextField of the name input field
	 * @return he JtextField of the name input field
	 */
	public JTextField get_name_input() {
		return name_input;
	}
 	
	/**
	 *This method will return of the Jbutton of the name submit button
	 * @return the Jbutton of the name submit button
	 */
	public JButton get_btn_submit() {
		return btn_submit;
	}
	
	/**
	 *This method will return of the JLabel of the game Info label at the top
	 * @return the JLabel of the game Info label at the top
	 */
	public JLabel get_InfoLabel() {
		return InfoLabel;
	}
	
	/**
	 *This method will return of the JFram of the game GUI Frame
	 * @return the JFram of the game GUI Frame
	 */
	public JFrame get_JFrame() {
		return frame;
	}
	
	
	/**
	 *This method will return the exit (menu item) in the menu bar
	 * @return the JmenuItem exit (menu item) in the menu bar
	 */
	public JMenuItem getexit() {
		return menuitem_exit;
	}
	
	/**
	 *This method will start the GUI
	 *
	 */
	public static void main(String[] args) 
	{
		GUI game = new GUI();
		game.goGUI();
	}

}