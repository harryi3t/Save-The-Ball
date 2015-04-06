package tools;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {
	private static long lastTime = getTime();
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale ){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation,matrix,matrix);
		matrix.rotate(rotation.x, new Vector3f(1,0,0));
		matrix.rotate(rotation.y, new Vector3f(0,1,0));
		matrix.rotate(rotation.z, new Vector3f(0,0,1));
		matrix.scale(scale);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera){
		Vector3f negativeTranslation = new Vector3f(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z);
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.rotate((float)Math.toRadians(camera.getRotation().x), new Vector3f(1,0,0));
		matrix.rotate((float)Math.toRadians(camera.getRotation().y), new Vector3f(0,1,0));
		Matrix4f.translate(negativeTranslation,matrix,matrix);
		
		return matrix;
	}
	
	public static int orientation(Vector2f p, Vector2f q, Vector2f r){
		float val = (q.y - p.y) * (r.x - q.x) -
	              (q.x - p.x) * (r.y - q.y);
	    if (val == 0) return 0;  // colinear
	    return (val > 0)? 1: 2; // clock or counterclock wise
	}
	
	public static int getDelta(){
		long currentTime = getTime();
		int delta = (int)(currentTime - lastTime);
		lastTime = getTime();
		return delta;
	}
	
	public static long getTime(){
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}
