package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0,0,0);
	private Vector3f rotation = new Vector3f(0,0,0);
	private float moveSpeed = 0.1f;
	
	public Camera() {
	}
	
	public void move(){
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z -= moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D) ){
			position.x += moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x -= moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z += moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)){
			position.y += moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_1)){
			position.y -= moveSpeed;
		}
		if(Keyboard.getEventKey()==Keyboard.KEY_EQUALS && Keyboard.getEventKeyState()){
			moveSpeed += 0.001f;
			System.out.println(moveSpeed);
		}
		if(Keyboard.getEventKey()==Keyboard.KEY_MINUS && Keyboard.getEventKeyState()
			&& !Keyboard.isRepeatEvent()){
			if(moveSpeed>0.01)
				moveSpeed -= 0.001f;
			System.out.println(moveSpeed);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	public void setRotation(float rx, float ry, float rz) {
		this.rotation.x = rx;
		this.rotation.y = ry;
		this.rotation.z = rz;
	}
	public void setRotationX(float rx) {
		this.rotation.x = rx;
	}
	public void setRotationY(float ry) {
		this.rotation.y = ry;
	}
	public void setRotationZ(float rz) {
		this.rotation.z = rz;
	}
}
