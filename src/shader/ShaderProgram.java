/**
 * 
 */
package shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Suvo
 *
 */
public abstract class ShaderProgram {
	private int programID;
	private int vertextShaderID;
	private int fragmentShaderID;
	public static FloatBuffer matrixBuffer=BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile,String fragmentFile){
		vertextShaderID=loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
		fragmentShaderID=loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
		programID=GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertextShaderID);
		GL20.glAttachShader(programID, fragmentShaderID); 
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocation();
		
	}
	protected abstract void getAllUniformLocation();
	protected int getUniformLocation(String uniformName){
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void loadFloat(int location,float value){
		GL20.glUniform1f(location, value);
	}
	protected void loadInt(int location,int value){
		GL20.glUniform1i(location, value);
	}
	protected void loadVector(int location,Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	protected void loadVector(int location, Vector2f vector){
		GL20.glUniform2f(location,vector.x,vector.y);
	}
	protected void loadBoolean(int location,boolean value){
		float toLoad=0;
		if(value) toLoad=1;
		GL20.glUniform1f(location, toLoad);
	}
	protected void loadMatrix(int location,Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	public void start(){
		GL20.glUseProgram(programID);
	}
	public void stop(){
		GL20.glUseProgram(0);
	}
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertextShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertextShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected void bindAttribute(int attribute,String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected abstract void bindAttributes();
	
	private int loadShader(String file,int type){
		StringBuilder shaderSource=new StringBuilder();
		try{
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String line;
			while((line=reader.readLine())!=null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		int shaderID=GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if ((GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS))==GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));	
		}
		return shaderID;
	}
	

}
