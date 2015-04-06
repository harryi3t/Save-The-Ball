package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	public static void createDisplay(){
		createDisplay(1280,768);
	}
	
	public static void createDisplay(int width, int height){
		try {
            Display.setDisplayMode(new DisplayMode(width,height));
            Display.create();
            Display.setTitle("Save The Ball");
            Display.setVSyncEnabled(true);
            Display.setResizable(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
	}
	
	public static void updateDisplay(){
		Display.sync(60);
		Display.update();
	}
	
	public static void closeDisplay(){
		Display.destroy();
	}
}