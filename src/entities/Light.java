package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position;
	private Vector3f color;
	private boolean isLightOn = true;
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public void moveBy(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	public Vector3f getColor() {
		return color;
	}
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void toggleLight(){
		isLightOn = isLightOn?false:true;
	}
	public boolean isLightOn() {
		return isLightOn;
	}
	
}
