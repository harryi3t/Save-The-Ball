package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Ball extends Entity {

	private boolean isFalling = false;
	private boolean fallLeft = false;
	private int counter = 0;
	
	
	public Ball(TexturedModel model, Vector3f position, Vector3f rotation,
			Vector3f scale) {
		super(model, position, rotation, scale);
	}
	
	public void moveLeft(float speed) {
		moveBy(-1*speed/100f, 0, -1*speed/100f);
		rotation.y = (float)Math.toRadians(-45);
		rotateBy(0, 0, 2+speed);
		
	}
	public void moveRight(float speed) {
		moveBy(1*speed/100f, 0, -1*speed/100f);
		rotation.y = (float)Math.toRadians(45);
		rotateBy(0, 0, -2-speed);
	}
	
	public void drop(int speed){
		counter++;
		if(isFallLeft()){
			moveLeft(speed);
			rotateBy(0, 0, 2+speed);
		}
		else{
			moveRight(speed);
			rotateBy(0, 0, -2-speed);
		}
		
		if(this.position.y > 0.5f)
			super.moveBy(0, (-1*counter*counter)/10000f, 0);
		else{
			counter-=2;
			if(counter<0)
				setFalling(false);
		}
		
	}
	
	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
		counter = 0;
	}

	public boolean isFallLeft() {
		return fallLeft;
	}

	public void setFallLeft(boolean fallLeft) {
		this.fallLeft = fallLeft;
	}
}
