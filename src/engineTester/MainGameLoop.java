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
import org.lwjgl.util.vector.Vector4f;

import entities.Ball;
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
import tools.Maths;

public class MainGameLoop {
	
	private static final float wl = 0.70710678f;
	static Ball ball = null;
	private static int delta = Maths.getDelta();
	private static boolean isGameOver = false;
	
	public static void main(String[] args) {
		DisplayManager.createDisplay(1200,800);
		
		Loader loader = new Loader();
		float[] wallPositions = {			
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
		float[] wallTextureCoords = {			
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
		int[] wallIndices = {
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
		float[] wallNormals = new float[] {
				0,0,1,
				0,0,-1,
				1,0,0,
				-1,0,0,
				0,1,0,
				0,-1,0};
		
		float[] floorPosition = {
			-100f,0f,10f,
			 10f,0f,10f,
			 10f,0f,-100f,
			 -100f,0f,-100f
		};
		float[] floorTextureCoords = {
				0,20,
				20,20,
				20,0,
				0,0
		};
		int[] floorIndices = {
			1,2,3,3,1,0
		};
		float[] floorNormal = {
			0,1,0	
		};
		
		RawModel wallModel = loader.loadToVAO(wallPositions, wallTextureCoords, wallNormals, wallIndices);
		RawModel floorModel = loader.loadToVAO(floorPosition, floorTextureCoords, floorNormal, floorIndices);
		RawModel ballModel = OBJLoader.loadObjModel("sphere", loader);
		
		ModelTexture wallTexture = new ModelTexture(loader.loadTexture("textures/redWall"));
		ModelTexture ballTexture = new ModelTexture(loader.loadTexture("textures/Football"));
		ModelTexture floorTexture = new ModelTexture(loader.loadTexture("textures/grass"));
		
		//Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("textures/grass","JPG")));
		//Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("textures/grass","JPG")));
		
		TexturedModel texturedWall = new TexturedModel(wallModel, wallTexture);
		TexturedModel texturedBall = new TexturedModel(ballModel, ballTexture);
		TexturedModel texturedfloor = new TexturedModel(floorModel, floorTexture);
		
		ball = new Ball(texturedBall, new Vector3f(0,4.5f,0), new Vector3f(0,0,0), new Vector3f(0.5f,0.5f,0.5f));
		Entity floor = new Entity(texturedfloor, new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1f,1f,1f));
		
		List<Wall> wallEntities = new ArrayList<Wall>();
		Wall.addWallsToList(wallEntities,texturedWall,5);
		
		Light light = new Light(new Vector3f(0,0,0), new Vector3f(1,1,1));
		Camera camera = new Camera();
		camera.setPosition(new Vector3f(0,12,6));
		camera.setRotation(new Vector3f(45,0,0));
		/*camera.setPosition(new Vector3f(0,50,0));
		camera.setRotation(new Vector3f(90,0,0));*/
		
		MasterRenderer renderer = new MasterRenderer();
		delta = Maths.getDelta();
		Wall currentWall = wallEntities.get(0);
		while(!Display.isCloseRequested()){
			delta = Maths.getDelta();
			camera.move();
			
			for(Entity e : wallEntities){
				renderer.processEntities(e);
			}
			renderer.processEntities(ball);
			renderer.processEntities(floor);
			//renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
			
			Vector4f bp = currentWall.isAbove(ball.getPosition()); // ballPosition wrt current wall
			//System.out.println(bp.x+" "+bp.y+" "+bp.z+" "+bp.w);
			// left wall falling North-East, need to check for any next wall that may save the ball
			if(bp.x == 1 && bp.y == 2 && bp.z == 1 && bp.w == 1 && currentWall.isLeftWall() && !ball.isFalling()){
				Wall nextWall = wallEntities.get(wallEntities.indexOf(currentWall)+1);
				Vector4f bp2 = nextWall.isAbove(ball.getPosition(),true); // ballPosition wrt next wall
				if(bp2.x == 1 && bp2.y == 1 && bp2.z == 1 && bp2.w == 1)
					currentWall = nextWall;
				else{
					System.out.println(ball.getPosition());
					System.out.println(nextWall.getPosition());
					System.out.println(bp2);
					ball.setFallLeft(false);
					ball.setFalling(true);
				}
			}
			// left wall falling North-West, no check needed as there cant be any other wall
			else if(bp.x == 1 && bp.y == 1 && bp.z == 2 && bp.w == 1 && currentWall.isLeftWall() && !ball.isFalling()){
				ball.setFallLeft(true);
				ball.setFalling(true);
			}
			// right wall falling North-East, no check needed as there cant be any other wall
			else if(bp.x == 1 && bp.y == 1 && bp.z == 2 && bp.w == 1 && !currentWall.isLeftWall() && !ball.isFalling()){
				ball.setFallLeft(false);
				ball.setFalling(true);
			}
			// right wall falling North-West, need to check for any next wall that may save the ball
			else if(bp.x == 1 && bp.y == 1 && bp.z == 1 && bp.w == 2 && !currentWall.isLeftWall() && !ball.isFalling()){
				Wall nextWall = wallEntities.get(wallEntities.indexOf(currentWall)+1);
				Vector4f bp2 = nextWall.isAbove(ball.getPosition(),false); // ballPosition wrt next wall
				if(bp2.x == 1 && bp2.y == 1 && bp2.z == 1 && bp2.w == 1)
					currentWall = nextWall;
				else{
					ball.setFallLeft(true);
					ball.setFalling(true);
				}
			}
			
			if(ball.isFalling() && !isGameOver){
				ball.drop(4);
				if(!ball.isFalling())
					isGameOver = true;
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				ball.moveLeft(4);
			else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				ball.moveRight(4);
			
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
		        else if(Keyboard.getEventKey() == Keyboard.KEY_P){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		        		wallEntities.get(0).isAbove(ball.getPosition());
		        }
		        else if(Keyboard.getEventKey() == Keyboard.KEY_4){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		        		System.out.println(currentWall.isAbove(ball.getPosition()));
		        }
		        else if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		        		resetBall(ball);
		        }
		        
		    }
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		System.exit(0);
	}

	
	public static void resetBall(Ball ball) {
		ball.setPosition(new Vector3f(0,4.5f,0));
		ball.setRotation(new Vector3f(0,0,0));
		isGameOver = false;
	}

	private static void addAnotherWall(List<Wall> we){
		Wall oldWall = we.remove(0);
		we.add(we.get(we.size()-1).getNextWall());
	}
}
