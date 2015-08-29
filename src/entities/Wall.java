package entities;

import java.util.List;
import java.util.Random;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tools.Maths;

public class Wall extends Entity{

	public static final float wl =  0.70710678f;
	private static final float margin = 0.0f;	private boolean leftWall = true;
	private static final int minLen = 4;	// min length of wall
	private static final int maxLen = 10; 	// max length of wall
	private int length;
	private static int defaultLength = 6;
	private static int width = 2;
	private static final int height = 4;
	private static Random r = new Random(System.currentTimeMillis());
	
	public Wall(TexturedModel model) {
		super(model, new Vector3f(0,0,0f), new Vector3f(0,45,0),new Vector3f(width,height,-defaultLength));
		this.length = defaultLength;
	}
	public Wall(TexturedModel model,Vector3f position, Vector3f rotation, boolean isLeftWall,Vector3f scale) {
		super(model, position, rotation,scale);
		this.leftWall = isLeftWall;
		this.length = (int)-scale.z;
	}
	
	public boolean isLeftWall() {
		return leftWall;
	}
	
	public Wall getNextWall(){
		Vector3f position = new Vector3f(this.getPosition().x,this.getPosition().y,this.getPosition().z);
		Vector3f rotation =  new Vector3f(this.getRotation().x,this.getRotation().y,this.getRotation().z);
		int newlength = r.nextInt(maxLen-minLen+1)+minLen;
		Vector3f randomScale = new Vector3f(width,height,-newlength);
		
		if(this.isLeftWall()){
			position.x -= (this.length-width)*wl;
			position.z -= (this.length+width)*wl;
			rotation.y = -45;
		}else{
			position.x += (this.length-width)*wl;
			position.z -= (this.length-width)*wl;
			rotation.y = 45;
		}
		
		Wall newWall = new Wall(this.getModel(),position,rotation,!this.isLeftWall(),randomScale);
		return newWall;
	}
	/*
	 * Static method to add n continuous walls to a list provided.
	 */
	public static void addWallsToList(List<Wall> wallEntities,TexturedModel texturedModel, int n) {
		wallEntities.add(new Wall(texturedModel));
		for(int i=1;i<n;i++){
			wallEntities.add(wallEntities.get(i-1).getNextWall());
		}
	}
	
	public Vector4f isAbove(Vector3f ballPosition,boolean isLeftToRightMoving){
		Vector3f offsetBallPosition = new Vector3f(ballPosition.x,ballPosition.y,ballPosition.z);
		if(isLeftToRightMoving){
			offsetBallPosition.x += width*wl;
			offsetBallPosition.z -= width*wl;
		}
		else{
			offsetBallPosition.x -= width*wl;
			offsetBallPosition.z -= width*wl;
		}
			
		return isAbove(offsetBallPosition);		
	}
	public Vector4f isAbove(Vector3f pointToCheck) {
		float len = (this.length - 2*margin) * wl;
		float wid = (Wall.width - 2*margin) * wl;
		
		Vector2f p1 = new Vector2f(this.position.x,this.position.z-margin);
		Vector2f p2,p3,p4;
		if(isLeftWall()){
			p2 = new Vector2f(p1.x + wid, p1.y - wid);
			p3 = new Vector2f(p2.x - len, p2.y - len);
			p4 = new Vector2f(p1.x - len, p1.y - len);
		}
		else{
			p2 = new Vector2f(p1.x + wid, p1.y + wid);
			p3 = new Vector2f(p2.x + len, p2.y - len);
			p4 = new Vector2f(p1.x + len, p1.y - len);
		}
		Vector2f point = new Vector2f(pointToCheck.x,pointToCheck.z);
		
		int x1 = Maths.orientation(p1, p2, point);
		int x2 = Maths.orientation(p2, p3, point);
		int x3 = Maths.orientation(p3, p4, point);
		int x4 = Maths.orientation(p4, p1, point);
			
		Vector4f pointPosition = new Vector4f(x1,x2,x3,x4);
		return pointPosition; 
	}

}
