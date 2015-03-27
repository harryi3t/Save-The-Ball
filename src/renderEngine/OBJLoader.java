package renderEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader {
	public static RawModel loadObjModel(String fileName, Loader loader){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("res/models/"+fileName+".obj"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] vertexArray = null;
		float[] textureArray = null;
		float[] normalArray = null;
		int[] indexArray = null;
		
		try {
			while(true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")){
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}
				else if(line.startsWith("vt ")){
					Vector2f textureCoords = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(textureCoords);
				}
				else if(line.startsWith("vn ")){
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]),Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}
				else if(line.startsWith("f ")){
					textureArray = new float[vertices.size()*2];
					normalArray = new float[vertices.size()*3];
					break;
				}				
			
			}
			
			
			System.out.println(textures.size());
			System.out.println(normals.size());
			System.out.println(vertices.size());
			
			
			while(line!=null){
				if(!line.startsWith("f ")){
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalArray);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		vertexArray = new float[vertices.size()*3];
		indexArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex : vertices){
			vertexArray[vertexPointer++] = vertex.x;
			vertexArray[vertexPointer++] = vertex.y;
			vertexArray[vertexPointer++] = vertex.z;
		}
		for(int i=0;i<indices.size();i++)
			indexArray[i]=indices.get(i);
		
		return loader.loadToVAO(vertexArray, textureArray,normalArray, indexArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, 
			List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray){
		
		int currentVertexPointer = Integer.parseInt(vertexData[0])-1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;
		
	}
}
