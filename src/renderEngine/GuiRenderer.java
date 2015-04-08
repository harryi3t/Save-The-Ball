package renderEngine;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class GuiRenderer {
	private Texture texture;
	Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
    TrueTypeFont font = new TrueTypeFont(awtFont, true);
	
    public GuiRenderer(String fileSrc, String ext) {
    	try {
        	texture = TextureLoader.getTexture(ext, new FileInputStream(new File(fileSrc)));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    public void render(){
    	int width = Display.getWidth();
    	int height = Display.getHeight();
    	
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);                    
		            
		GL11.glClearDepth(1);                                       
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glViewport(0,0,width,height);
		GL11.glOrtho(0, width, height, 0, 1, -1);
         
        font.drawString(110,110, "Score ", Color.green);
        /*
         GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
         texture.bind();
         GL11.glBegin(GL11.GL_TRIANGLES);
         GL11.glTexCoord2f(1, 0);
         GL11.glVertex2i(450, 10);
         GL11.glTexCoord2f(0, 0);
         GL11.glVertex2i(10, 10);
         GL11.glTexCoord2f(0, 1);
         GL11.glVertex2i(10, 450);
         GL11.glTexCoord2f(0, 1);
         GL11.glVertex2i(10, 450);
         GL11.glTexCoord2f(1, 1);
         GL11.glVertex2i(450, 450);
         GL11.glTexCoord2f(1, 0);
         GL11.glVertex2i(450, 10);
         GL11.glEnd();*/
    }

	public void cleanUp(){
    	texture.release();
    }
}
