package terrains;

import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import org.lwjgl.util.vector.Vector3f;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/**
 * Created by Suvo on 14-Dec-15.
 */
public class EntityTerrain {
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private Vector3f position;
    private float rotX,rotY,rotZ;
    private float scale;

    public EntityTerrain(RawModel model, TerrainTexturePack texturePack, TerrainTexture blendMap, Vector3f position,
                         float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;
    }
}
