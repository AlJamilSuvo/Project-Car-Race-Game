/**
 * 
 */
package shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolBoxs.Maths;
import entities.Camera;
import entities.Light;

/**
 * @author Suvo
 *
 */
public class TerrainShader extends ShaderProgram{
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPosition[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	
	public static final String VERTEX_FILE="src/shader/terrainVertextShader.txt";
	public static final String FRAGMENT_FILE="src/shader/terrainFragmentShader.txt";

	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
		super.bindAttribute(2, "normals");
		
	}

	@Override
	protected void getAllUniformLocation() {
		location_transformationMatrix=super.getUniformLocation("transformationMatrix");
		location_projectionMatrix=super.getUniformLocation("projectionMatrix");
		location_viewMatrix=super.getUniformLocation("viewMatrix");
		location_lightPosition=new int[4];
		location_lightColor=new int[4];
		location_attenuation=new int[4];
		for(int i=0;i<4;i++){
			location_lightPosition[i]=super.getUniformLocation("lightPosition["+String.valueOf(i)+"]");
			location_lightColor[i]=super.getUniformLocation("lightColor["+String.valueOf(i)+"]");
			location_attenuation[i]=super.getUniformLocation("attenuation["+String.valueOf(i)+"]");
		}
		location_shineDamper=super.getUniformLocation("shineDamper");
		location_reflectivity=super.getUniformLocation("reflectivity");
		location_skyColor=super.getUniformLocation("skyColor");
		location_backgroundTexture=super.getUniformLocation("backgroundTexture");
		location_rTexture=super.getUniformLocation("rTexture");
		location_gTexture=super.getUniformLocation("gTexture");
		location_bTexture=super.getUniformLocation("bTexture");
		location_blendMap=super.getUniformLocation("blendMap");
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	public void loadShiner(float damper,float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	public void loadSkyColor(Vector3f skyColor){
		super.loadVector(location_skyColor, skyColor);
	}
	public void loadLight(List<Light> lights){
		for(int i=0;i<4;i++){
			if (i<lights.size()){
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			}
			else{
				super.loadVector(location_lightPosition[i], new Vector3f(0,0,0));
				super.loadVector(location_lightColor[i], new Vector3f(0,0,0));
				super.loadVector(location_attenuation[i], new Vector3f(1,0,0));
			}
		}
		
		
	}
	public void loadTransformationMatrix(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix=Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
