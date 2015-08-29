package engineTester;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

















// Must import the lwjgl Display class
import org.lwjgl.opengl.Display;
 
 
public class UI
{
    private MainGameLoop game;
    private static boolean isCreditPageVisible = false; 
     
    public UI() 
    {
        final JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Save The Ball");
        ImageIcon icon = new ImageIcon("res/icon/ball.png");
        mainFrame.setIconImage(icon.getImage());
        
        // Create a new canvas and set its size.
        final Canvas canvas = new Canvas();
        // Must be 640*480 to match the size of an Env3D window
        canvas.setSize(800, 600);
        // This is the magic!  The setParent method attaches the 
        // opengl window to the awt canvas.
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }
        
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("About");
        menuBar.add(menu);
        
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText("Credits");
        menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!isCreditPageVisible)
					isCreditPageVisible = true;
				else
					return;
				final JFrame frame = new JFrame();
		        frame.setTitle("About");
		        frame.setSize(500, 300);
		        ImageIcon icon = new ImageIcon("res/icon/ball.png");
		        frame.setIconImage(icon.getImage());
		        
		        JPanel panel = new JPanel();
		        GridBagLayout layout = new GridBagLayout();

		        panel.setLayout(layout);        
		        GridBagConstraints gbc = new GridBagConstraints();
		        //gbc.insets = new Insets(5, 5, 5, 5);
		       
		        gbc.gridx = 0;
		        gbc.gridy = 0;
		        panel.add(new JLabel(new ImageIcon("res/images/credits/vaibhav.png")),gbc);
		        
		        gbc.gridx = 1;
		        gbc.gridy = 0;
		        panel.add(new JLabel(new ImageIcon("res/images/credits/harry.png")),gbc);
		        
		        gbc.gridx = 2;
		        gbc.gridy = 0;
		        panel.add(new JLabel(new ImageIcon("res/images/credits/apoorv.jpg")),gbc);
		        
		        gbc.gridx = 0;
		        gbc.gridy = 1;
		        JLabel vaibhav = new JLabel("Vaibhav Kumar ");
		        vaibhav.setFont(new Font("Monotype Corsiva", 0, 15));
		        panel.add(vaibhav,gbc);
   
		        gbc.gridx = 1;
		        gbc.gridy = 1;
		        JLabel harry = new JLabel("Harendra Singh");
		        harry.setFont(new Font("Monotype Corsiva", 0, 15));
		        panel.add(harry,gbc);
		        
		        gbc.gridx = 2;
		        gbc.gridy = 1;      
		        JLabel apoorv =new JLabel("Apoorv Srivastava");
		        apoorv.setFont(new Font("Monotype Corsiva", 0, 15));
		        panel.add(apoorv,gbc);
		        
		        gbc.gridx = 1;
		        gbc.gridy = 3;       
		        panel.add(new JLabel("Copyright © 2015 PDPM IIITDMJ, All Rights Reserved"),gbc);
		        
		        //frame.setUndecorated(true);
		        frame.setResizable(false);
		        frame.setLocationRelativeTo(canvas);
		        
		        JButton exitButton = new JButton("Exit");
		        exitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						frame.dispose();
						isCreditPageVisible = false;
					}
				});
		        
		        gbc.gridx = 2;
		        gbc.gridy = 3;   
		        panel.add(exitButton,gbc);
		        
		        frame.add(panel,BorderLayout.CENTER);
		        
		        JLabel projectLabel = new JLabel("Computer Graphics Course Project [CS-601]",SwingConstants.CENTER);
		        projectLabel.setFont(new Font("Times New Roman", 0, 20));
		        frame.add(projectLabel,BorderLayout.NORTH);
		        frame.setVisible(true);
			}
		});
        menu.add(menuItem);
        
        mainFrame.add(menuBar,BorderLayout.NORTH);
        mainFrame.add(canvas,BorderLayout.CENTER); 
        
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mainFrame, 
                    "Are you sure to exit the game?", "Really Closing?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    	System.exit(0);
                	}
            }
        });
        
        mainFrame.pack();
        mainFrame.setVisible(true);        
        MainGameLoop.main(null);
         
    }
     
    public static void main(String args[]) 
    {
        new UI();
    }
}
