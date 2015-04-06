package engineTester;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;
import tools.Maths;
import entities.Ball;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Wall;

public class MainGameLoop {
	
	static Ball ball = null;
	private static int delta = Maths.getDelta();
	private static boolean isGameOver = false;
	private static boolean moveLeft = true;
	private static final float initialBallSpeed = 4;
	private static float ballSpeed = initialBallSpeed;
	private static int initialNumWalls = 5;
	
	public static void main(String[] args) {
		
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();

		int screenHeight = d.height;
		int screenWidth = d.width;
		DisplayManager.createDisplay(screenWidth,screenHeight);
		
		Display.setVSyncEnabled(true);
		Score score = new Score(Display.getWidth(),Display.getHeight());
				
		Loader loader = new Loader();
	
		
		float[] floorPosition = {
			-3000f,0f,30f,
			 3000f,0f,30f,
			 3000f,0f,-10000f,
			 -3000f,0f,-10000f
		};
		float[] floorTextureCoords = {
				0,2000,
				2000,2000,
				2000,0,
				0,0
		};
		int[] floorIndices = {
				1,2,3,
				3,1,0
		};
		float[] floorNormal = {
			0,1,0,
			0,1,0,
			0,1,0
		};
		
		RawModel wallModel = OBJLoader.loadObjModel("wall", loader);
		RawModel floorModel = loader.loadToVAO(floorPosition, floorTextureCoords, floorNormal, floorIndices);
		RawModel ballModel = OBJLoader.loadObjModel("sphere", loader);
		
		ModelTexture wallTexture = new ModelTexture(loader.loadTexture("textures/redWall"));
		ModelTexture ballTexture = new ModelTexture(loader.loadTexture("textures/Football"));
		ModelTexture floorTexture = new ModelTexture(loader.loadTexture("textures/grass"));
		
		TexturedModel texturedWall = new TexturedModel(wallModel, wallTexture);
		TexturedModel texturedBall = new TexturedModel(ballModel, ballTexture);
		TexturedModel texturedfloor = new TexturedModel(floorModel, floorTexture);
		
		ball = new Ball(texturedBall, new Vector3f(0f,4.5f,-1f), new Vector3f(0,0,0), new Vector3f(0.5f,0.5f,0.5f));
		Entity floor = new Entity(texturedfloor, new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1f,1f,1f));
		
		List<Wall> wallEntities = new ArrayList<Wall>();
		Wall currentWall = null;
		
		Light light = new Light(new Vector3f(0,6,0), new Vector3f(1,1,1));
		Camera camera = new Camera(light,ball,floor);
		
		resetGame(score,ball, camera, light, wallEntities,currentWall, texturedWall,floor);
		currentWall = wallEntities.get(0);
		
		MasterRenderer renderer = new MasterRenderer();
		delta = Maths.getDelta();
		
		while(!Display.isCloseRequested()){
			if(!isGameOver)
				ballSpeed += 0.01;
			delta = Maths.getDelta();
			camera.move();
			
			for(Entity e : wallEntities){
				renderer.processEntities(e);
			}
			renderer.processEntities(floor);
			renderer.processEntities(ball);
			renderer.render(light, camera);
			if(ball.isFalling())
				score.storeSessionScore();
			if(isGameOver)
				score.showScore();
			
			score.updateScore(ballSpeed);
			DisplayManager.updateDisplay();
			
			Vector4f bp = currentWall.isAbove(ball.getPosition()); // ballPosition wrt current wall
			// left wall falling North-East, need to check for any next wall that may save the ball
			if(bp.x == 1 && bp.y == 2 && bp.z == 1 && bp.w == 1 && currentWall.isLeftWall() && !ball.isFalling() && ball.getPosition().y>0){
				Wall nextWall = wallEntities.get(wallEntities.indexOf(currentWall)+1);
				Vector4f bp2 = nextWall.isAbove(ball.getPosition(),true); // ballPosition wrt next wall
				if(bp2.x == 1 && bp2.y == 1 && bp2.z == 1 && bp2.w == 1){
					currentWall = nextWall;
					addAnotherWall(wallEntities);
				}
				else{
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
				if(bp2.x == 1 && bp2.y == 1 && bp2.z == 1 && bp2.w == 1){
					currentWall = nextWall;
					addAnotherWall(wallEntities);
				}
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
			
			/*if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				ball.moveLeft(4);
			else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				ball.moveRight(4);*/
			if(!isGameOver)
			{
				if(ball.isFalling() && ballSpeed>0){
					ballSpeed -= 0.5f;
				}
				if(moveLeft)
					ball.moveLeft(ballSpeed);
				else
					ball.moveRight(ballSpeed);
			}
			while(Keyboard.next())
		    {
		        if(Keyboard.getEventKey() == Keyboard.KEY_RETURN){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent()){
		        		resetGame(score,ball,camera,light,wallEntities,currentWall,texturedWall,floor);
		        		currentWall = wallEntities.get(0);
		        	}
		        }
		        else if(Keyboard.getEventKey() == Keyboard.KEY_LEFT){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		        		moveLeft = true;
		        }
		        else if(Keyboard.getEventKey() == Keyboard.KEY_RIGHT){
		        	if(Keyboard.getEventKeyState() && !Keyboard.isRepeatEvent())
		        		moveLeft = false;
		        }
		        
		    }
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
		System.exit(0);
	}

	
	public static void resetGame(Score score, Ball ball, Camera camera, Light light, List<Wall> wallEntities, Wall currentWall, TexturedModel texturedWall, Entity floor) {
		score.setCurrentSessionScore(0);
		score.setCurrentScore(0);
		ball.setPosition(new Vector3f(0,4.5f,-1));
		ball.setRotation(new Vector3f(0,0,0));
		isGameOver = false;
		ball.setFalling(false);
		ball.setVi(0);
		moveLeft = true;
		ballSpeed = initialBallSpeed;		
		light.setPosition(new Vector3f(0,6,0));
		wallEntities.clear();
		camera.reset();
		floor.setPosition(new Vector3f(0,0,0));
		Wall.addWallsToList(wallEntities,texturedWall,initialNumWalls);
	}

	private static void addAnotherWall(List<Wall> we){
		Wall oldWall = we.remove(0);
		we.add(we.get(we.size()-1).getNextWall());
	}
}
