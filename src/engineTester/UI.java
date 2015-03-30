package engineTester;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

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
         
        // The exit button.
        JButton b1 = new JButton("Exit");
        JButton b2 = new JButton("Reset Ball");
        JMenu contextMenu = new JMenu();
        
        contextMenu.add(new JMenuItem("Drop"));
         
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               MainGameLoop.resetBall(MainGameLoop.ball);
            }
        });
         
        // Create a new canvas and set its size.
        Canvas canvas = new Canvas();
        // Must be 640*480 to match the size of an Env3D window
        canvas.setSize(1200, 800);
        // This is the magic!  The setParent method attaches the 
        // opengl window to the awt canvas.
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }
         
        // Construct the GUI as normal
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(b1);
        panel.add(b2);
        frame.add(panel,BorderLayout.NORTH);
        //frame.add(contextMenu, BorderLayout.NORTH);
        frame.add(canvas, BorderLayout.CENTER);
         
        frame.pack();
        frame.setVisible(true);
         
        // Make sure you run the game, which 
        // executes on a separate thread.
        MainGameLoop.main(null);
         
    }
     
    public static void main(String args[]) 
    {
        new UI();
    }
}
