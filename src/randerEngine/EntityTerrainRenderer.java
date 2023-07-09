package randerEngine;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shader.TerrainShader;
import terrains.EntityTerrain;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBoxs.Maths;

import java.util.List;
import java.util.Map;

/**
 * Created by Suvo on 14-Dec-15.
 */
public class EntityTerrainRenderer {
    private TerrainShader shader;
    public EntityTerrainRenderer(TerrainShader shader,Matrix4f projectionMatrix){
        this.shader=shader;
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    public void render(Map<RawModel,List<EntityTerrain>> entities){
        for (RawModel model:entities.keySet()){
            List<EntityTerrain> batch=entities.get(model);
            prepareRawModel(batch.get(0));
            for (EntityTerrain entity:batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unBindTexture();

        }
    }

    private void bindTexture(EntityTerrain terrain){
        TerrainTexturePack texturePack=terrain.getTexturePack();
        TerrainTexture blendMap=terrain.getBlendMap();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureId());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, blendMap.getTextureId());
    }

    private void unBindTexture() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(EntityTerrain entity) {
        Matrix4f transformationMarix= Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMarix);
    }

    private void prepareRawModel(EntityTerrain model) {
        RawModel rawModel=model.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTexture(model);


    }


}
