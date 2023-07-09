package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import randerEngine.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Suvo on 09-Dec-15.
 */
public class TextMaster {
    private static Loader loader;
    private static Map<FontType,List<GUIText>> texts=new HashMap<>();
    private static FontRenderer renderer;


    public static void init(Loader theLoader){
        renderer=new FontRenderer();
        loader=theLoader;
    }

    public static void render(){
        renderer.render(texts);
    }

    public static void loadText(GUIText text){
        FontType font=text.getFont();
        TextMeshData data=font.loadText(text);
        int vao=loader.loadToVAO(data.getVertexPositions(),data.getTextureCoords());
        text.setMeshInfo(vao,data.getVertexCount());
        List<GUIText> textBatch=texts.get(font);
        if (textBatch==null){
            textBatch=new ArrayList<>();
            texts.put(font,textBatch);
        }
        textBatch.add(text);
    }

    public static void removeText(GUIText text){
        List<GUIText> textBatch=texts.get(text.getFont());
        textBatch.remove(text);
        if (textBatch.isEmpty()){
            texts.remove(textBatch);
        }
    }

    public static void cleanUp(){
        renderer.cleanUp();
    }
}
