package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Entity {
	private TexturedModel model;
	private Vector3f position; 
	private Vector3f rotation; // in radian
	private Vector3f scale;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation,
			Vector3f scale) {
		this.model = model;
		this.position = position;
		this.rotation = new Vector3f((float)Math.toRadians(rotation.x),
				(float)Math.toRadians(rotation.y),(float)Math.toRadians(rotation.z));
		this.scale = scale;
	}
	
	public void moveBy(float dx,float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void rotateBy(float dx,float dy, float dz){
		this.rotation.x += (float)Math.toRadians(dx);
		this.rotation.y += (float)Math.toRadians(dy);
		this.rotation.z += (float)Math.toRadians(dz);
		if(rotation.x > 2*Math.PI)
			rotation.x -= 2*Math.PI;
		else if(rotation.x < 0)
			rotation.x += 2*Math.PI;
		if(rotation.y > 2*Math.PI)
			rotation.y -= 2*Math.PI;
		else if(rotation.x < 0)
			rotation.y += 2*Math.PI;
		if(rotation.z > 2*Math.PI)
			rotation.z -= 2*Math.PI;
		else if(rotation.z < 0)
			rotation.x += 2*Math.PI;
	}
	
	
	public TexturedModel getModel() {
		return model;
	}
	public void setModel(TexturedModel model) {
		this.model = model;
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
		this.rotation = new Vector3f((float)Math.toRadians(rotation.x),
				(float)Math.toRadians(rotation.y),(float)Math.toRadians(rotation.z));
	}
	public Vector3f getScale() {
		return scale;
	}
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public void moveLeft(int speed) {
		moveBy(-1*speed/100f, 0, -1*speed/100f);
		rotation.y = (float)Math.toRadians(-45);
		rotateBy(0, 0, 2+speed);
		
	}
	public void moveRight(int speed) {
		moveBy(1*speed/100f, 0, -1*speed/100f);
		rotation.y = (float)Math.toRadians(45);
		rotateBy(0, 0, -2-speed);
	}
	
}
