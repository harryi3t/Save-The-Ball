package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private static final float initialDistanceFromBall = 10;
	private static final float InitialPitch = 50; // angle of depression of ball as seen from camera
	private static final float initialDirectionalAngle = 0;  //angle of camera around z-axis
	
	private float distanceFromBall;
	private float pitch; 
	private float directionalAngle; 
	
	private Vector3f position = new Vector3f(0,0,0);
	private Vector3f rotation = new Vector3f(0,0,0);
	private float moveSpeed = 0.1f;
	private float lastBallx = 0;
	private float lastBallz = 0;
	private float lastfloorz = 0;
	private static boolean temp = true;
	
	private Light light;
	private Entity ball;
	private Entity floor;
	
	public Camera(Light light, Ball ball, Entity floor) {
		reset();
		this.light = light;
		this.ball = ball;
		this.floor = floor;
		rotation.x = pitch;	
	}
	
	public void move(){
		calculateZoom();
		calculatePitch();
		calculateDirectionalAngle();
		float zDistance = calculateZDistance();
		float xDistance = calculateXDistance();
		float yDistance = calculateVerticalDistance();
		Vector3f calculatedPosition = new Vector3f(xDistance,yDistance,zDistance);
		setCameraPosition(calculatedPosition);
	}
	
	
	private void calculateZoom(){
		distanceFromBall -= Mouse.getDWheel()*0.01f;
		if(distanceFromBall<2)
			distanceFromBall =2;
		else if(distanceFromBall>40)
			distanceFromBall = 40;
	}
	
	public void reset(){
		distanceFromBall = initialDistanceFromBall;
		pitch = InitialPitch; 
		rotation.x = pitch;
		directionalAngle = initialDirectionalAngle;
		rotation.y = -directionalAngle;
		setLastBallx(0);
		setLastBallz(0);
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(0)){
			pitch -= Mouse.getDY();
			if(pitch<10) pitch = 10;
			else if(pitch>90) pitch = 90;
			rotation.x = pitch;
		}
	}
	
	private void calculateDirectionalAngle() {
		if(Mouse.isButtonDown(0)){
			directionalAngle -= Mouse.getDX();
			if(directionalAngle>90)
				directionalAngle = 90;
			else if(directionalAngle<-90)
				directionalAngle = -90;
			rotation.y = -directionalAngle;
		}
	}
	
	private float calculateZDistance(){
		return (float) (distanceFromBall*Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(directionalAngle)));
	}
	
	private float calculateXDistance(){
		return (float) (distanceFromBall*Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(directionalAngle)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromBall*Math.sin(Math.toRadians(pitch)));
	}
	
	private void setCameraPosition(Vector3f calculatedPosition){
		position.y = ball.getPosition().y + calculatedPosition.y;
		position.z = ball.getPosition().z + calculatedPosition.z;
		position.x = ball.getPosition().x + calculatedPosition.x;
		light.setPosition(position);
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
	public void setLastBallx(float lastBallx) {
		this.lastBallx = lastBallx;
	}
	public void setLastBallz(float lastBallz) {
		this.lastBallz = lastBallz;
	}
	public void setLastfloorz(float lastfloorz) {
		this.lastfloorz = lastfloorz;
	}

}
