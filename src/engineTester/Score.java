package engineTester;

import java.awt.Font;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
 






import java.util.stream.Collector.Characteristics;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
 
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
  
public class Score {
  
    /** The fonts to draw to the screen */
    private TrueTypeFont font;
    private TrueTypeFont font2;
    
    private int width;
    private int height;
    
    private float currentScore = 0;
    private int currentSessionScore;
    private int highScore;
     
    /** Boolean flag on whether AntiAliasing is enabled or not */
    private boolean antiAlias = true;
 
  
    /**
     * Initialise resources
     */
    public Score(int width, int height) {
    	readHighScore();
    	
    	this.height = height;
    	this.width = width;
    	
    	//load a default java font
        Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
        font = new TrueTypeFont(awtFont, antiAlias);
         
        // load font from file
        try {
            InputStream inputStream = ResourceLoader.getResourceAsStream("res/fonts/brick.ttf");
             
            Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            awtFont2 = awtFont2.deriveFont(50f); // set font size
            font2 = new TrueTypeFont(awtFont2, antiAlias);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public void prepare(){
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);                    
		            
		GL11.glClearDepth(1);                                       
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
    }
    
	public void updateScore(float ballSpeed) {
		if(currentSessionScore==0) // the ball has not yet fallen
			currentScore += ballSpeed/10;
		prepare();
		
		font2.drawString(0, 0, "", Color.yellow);
		font.drawString(width-200, 20, "Score "+(int)currentScore, Color.green);
	}
	
	public void showScore(){
		prepare();
		int x = width/2-300;
		int y = height/2-100;
		GL11.glColor3f(1, 1, 1);
		GL11.glRectf(x-50, y-150, x+640, y+200);
		font2.drawString(x, y, "Your Score = "+(int)currentScore, Color.red);
		font2.drawString(x, y + 100, "High Score = "+highScore, Color.black);
		font2.drawString(x+100,y-100, "GAME OVER", Color.magenta);
		font.drawString(x+100,y+170, "Please <Return> to Restart . . .", Color.black);
	}

	public void storeSessionScore() {
		currentSessionScore = (int) currentScore;
		if(currentSessionScore > highScore){
			writeHighScore(currentSessionScore);
			highScore = currentSessionScore;
		}
	}

	public void setCurrentSessionScore(int currentSessionScore) {
		this.currentSessionScore = currentSessionScore;
	}

	public void setCurrentScore(float currentScore) {
		this.currentScore = currentScore;
	}
	
	public void readHighScore(){
		try {
			FileReader reader = new FileReader("res/score/highScore.txt");
			char[] score = new char[5];
			char r;
			StringBuilder builder = new StringBuilder(); 
			
			while(Character.isDigit(r=(char) reader.read())){
				builder.append(r);
			}
			
			highScore = Integer.parseInt(builder.toString());
			
		} catch (IOException | NumberFormatException e) {
			// No high score
			highScore = 0;
			System.out.println("No high score found");
		}
	}
	
	public void writeHighScore(int score){
		try {
			FileWriter writer = new FileWriter("res/score/highScore.txt", false);
			writer.write(String.valueOf(score));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}