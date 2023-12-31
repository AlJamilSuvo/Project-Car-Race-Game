package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shader.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/fontRendering/fontVertex.txt";
	private static final String FRAGMENT_FILE = "src/fontRendering/fontFragment.txt";

	private int location_color;
	private int location_translation;

	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}



	@Override
	protected void getAllUniformLocation() {
		location_color=super.getUniformLocation("color");
		location_translation=super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0,"position");
		super.bindAttribute(1,"textCoords");
	}

	public void loadColor(Vector3f color){
		super.loadVector(location_color,color);
	}
	public void loadTranslation(Vector2f translation){
		super.loadVector(location_translation,translation);
	}


}
