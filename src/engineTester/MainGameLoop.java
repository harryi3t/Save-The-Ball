package engineTester;

import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Wall;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {
	
	private static final float wl = 0.70710678f;
	
	public static void main(String[] args) {
		DisplayManager.createDisplay(600,400);
		
		Loader loader = new Loader();
		float[] positions = {			
				0f,1f,0f,	
				0f,0f,0f,	
				1f,0f,0f,	
				1f,1f,0f,		
				
				0f,1f,1f,	
				0f,0f,1f,	
				1f,0f,1f,	
				1f,1f,1f,
				
				1f,1f,0f,	
				1f,0f,0f,	
				1f,0f,1f,	
				1f,1f,1f,
				
				0f,1f,0f,	
				0f,0f,0f,	
				0f,0f,1f,	
				0f,1f,1f,
				
				0f,1f,1f,
				0f,1f,0f,
				1f,1f,0f,
				1f,1f,1f,
				
				0f,0f,1f,
				0f,0f,0f,
				1f,0f,0f,
				1f,0f,1f
				
		};
		float[] textureCoords = {			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0	
		};
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		float[] normals = new float[] {
				0,0,1,
				0,0,-1,
				1,0,0,
				-1,0,0,
				0,1,0,
				0,-1,0};
		
		RawModel wallModel = loader.loadToVAO(positions, textureCoords, normals, indices);
		RawModel ballModel = OBJLoader.loadObjModel("sphere", loader);
		
		ModelTexture wallTexture = new ModelTexture(loader.loadTexture("textures/redWall"));
		ModelTexture ballTexture = new ModelTexture(loader.loadTexture("textures/Football"));
		
		//Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("textures/grass","JPG")));
		//Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("textures/grass","JPG")));
		
		TexturedModel texturedWall = new TexturedModel(wallModel, wallTexture);
		TexturedModel texturedBall = new TexturedModel(ballModel, ballTexture);
		
		Entity ball = new Entity(texturedBall, new Vector3f(0,1.5f,0), new Vector3f(0,0,0), new Vector3f(0.5f,0.5f,0.5f));
		
		List<Wall> wallEntities = new ArrayList<Wall>();
		Wall.addWallsToList(wallEntities,texturedWall,5);
		
		Light light = new Light(new Vector3f(0,0,0), new Vector3f(1,1,1));
		Camera camera = new Camera();
		camera.setPosition(new Vector3f(0,4,3));
		camera.setRotation(new Vector3f(45,0,0));
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			camera.move();
			
			for(Entity e : wallEntities){
				renderer.processEntities(e);
			}
			renderer.processEntities(ball);
			//renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				ball.moveLeft(4);
			else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				ball.moveRight(4);
			else if(Keyboard.isKeyDown(Keyboard.KEY_P)){
				ball.moveBy(0, -0.01f, 0);
			}
			
			while(Keyboard.next())
		    {
		        if(Keyboard.getEventKey() == Keyboard.KEY_L){
		            if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		                light.toggleLight();
		    	}
		        else if(Keyboard.getEventKey() == Keyboard.KEY_3){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		        		addAnotherWall(wallEntities);
		        }
		    }
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
	private static void addAnotherWall(List<Wall> we){
		Wall oldWall = we.remove(0);
		we.add(we.get(we.size()-1).getNextWall());
	}
}
