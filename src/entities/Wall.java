package entities;

import java.sql.Time;
import java.util.List;
import java.util.Random;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Wall extends Entity{

	public static final float wl =  0.70710678f;
	private boolean leftWall = true;
	private static final int minLen = 4;	// min length of wall
	private static final int maxLen = 10; 	// max length of wall
	private int length;
	private static int defaultLength = 6;
	private static int width = 2;
	private static Random r = new Random(System.currentTimeMillis());
	
	public Wall(TexturedModel model) {
		super(model, new Vector3f(0,0,0f), new Vector3f(0,45,0),new Vector3f(width,1,-defaultLength));
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
		Vector3f randomScale = new Vector3f(width,1,-newlength);
		
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

}
