package renderEngine;

import java.util.List;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import shaders.TerrainShader;
import terrains.Terrain;
import tools.Maths;

public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader,Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Terrain> terrains){
		for(Terrain terrain:terrains){
			prepareTerrain(terrain);
			loadModalMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), 
					GL11.GL_UNSIGNED_INT, 0);
			unbindTerrain();
		}
	}
	
	public void prepareTerrain(Terrain terrain){
		RawModel model = terrain.getModel();
		GL30.glBindVertexArray(model.getVaoID());
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexture().getTextureID());
	}
	
	private void unbindTerrain(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModalMatrix(Terrain terrain){
		Matrix4f TransformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(),0,terrain.getZ()),new Vector3f(0,0,0),new Vector3f(1,1,1));
		shader.loadTransformationMatrix(TransformationMatrix);
	}
}
