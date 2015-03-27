package tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class Maths {
	
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
	
	public static Matrix4f createViewMatrix(Camera camera){
		Vector3f negativeTranslation = new Vector3f(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z);
		
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.rotate((float)Math.toRadians(camera.getRotation().x), new Vector3f(1,0,0));
		matrix.rotate((float)Math.toRadians(camera.getRotation().y), new Vector3f(0,1,0));
		Matrix4f.translate(negativeTranslation,matrix,matrix);
		
		return matrix;
	}
}
