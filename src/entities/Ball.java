package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Ball extends Entity {

	private boolean isFalling = false;
	private boolean isGoingDown = false;
	private boolean fallLeft = false;
	private int counter = 0;
	private float vi = 0;
	private static final int MAX_BOUNCES = 8;
	private int bounces = 0;
	
	
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
		
		float vf = (vi - 10*counter);
		
		if(vf<0){
			isGoingDown = true;
		}
		
		if(this.position.y < 0.5f && isGoingDown){
			isGoingDown = false;
			vi = -0.9f*vf;
			vf = 0;
			counter = 1;
			bounces++;
			if(bounces == MAX_BOUNCES){
				setFalling(false);
				bounces = 0;
			}
		}
		super.moveBy(0, (vf*counter)/10000f, 0);
	}
	
	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
		if(isFalling){
			setGoingDown(true);
		}
		counter = 0;
	}

	public boolean isFallLeft() {
		return fallLeft;
	}

	public void setFallLeft(boolean fallLeft) {
		this.fallLeft = fallLeft;
	}

	public void setVi(float vi) {
		this.vi = vi;
	}

	public void setGoingDown(boolean isGoingDown) {
		this.isGoingDown = isGoingDown;
	}
	
}
