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
     
    public UI() 
    {
        JFrame frame = new JFrame();
        frame.setTitle("Save The Ball");
        ImageIcon icon = new ImageIcon("res/icon/ball.png");
        frame.setIconImage(icon.getImage());
        
        // Create a new canvas and set its size.
        Canvas canvas = new Canvas();
        // Must be 640*480 to match the size of an Env3D window
        canvas.setSize(800, 600);
        // This is the magic!  The setParent method attaches the 
        // opengl window to the awt canvas.
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }
        
        frame.add(canvas,BorderLayout.CENTER); 
        
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame, 
                    "Are you sure to exit the game?", "Really Closing?", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    	System.exit(0);
                	}
                else{
                	
                }
            }
        });
        
        frame.pack();
        frame.setVisible(true);
        
        MainGameLoop.main(null);
         
    }
     
    public static void main(String args[]) 
    {
        new UI();
    }
}
