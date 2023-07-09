/**
 * 
 */
package engineTester;

import java.io.*;
import java.util.*;

import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import guis.GuiRenderer;
import guis.GuiTexture;
import randerEngine.DisplayManager;
import randerEngine.Loader;
import randerEngine.MasterRenderer;
import terrains.EntityTerrain;
import terrains.NormaledTerrain;
import textures.ModelTexture;
import textures.NormaledTerrainTexturePack;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBoxs.Collision;
import toolBoxs.Maths;
import toolBoxs.MousePicker;

/**
 * @author Suvo
 *
 */
public class MainGameLoop {
	/**
	 * @param args
	 */


	static List<NormaledTerrain> terrains =new ArrayList<>();
	static List<Entity> entities=new ArrayList<>();
	static Player player;
	static HashMap<String,TextureModel> modelList=new HashMap<>();
	static HashMap<NormaledTerrain,String> tr=new HashMap<>();

	public static void main(String[] args) {
		Random rand = new Random();

		DisplayManager.createDisplay();
		List<Light> lights = new ArrayList<Light>();

		Loader loader = new Loader();
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("/font/comic"), new File("res/font/comic.fnt"));
		GUIText text = new GUIText("Java Online Car Game", 3f, font, new Vector2f(0f, -.5f), 1f, true);
		GUIText load = new GUIText("Loading", .5f, font, new Vector2f(0f, -.70f), 1, true);
		load.setColour(0, 1, 0);
		text.setColour(1, 0, 0);
		TextMaster.loadText(text);
		TextMaster.loadText(load);
		TextMaster.render();
		DisplayManager.updateDisplay();

		List<Entity> grassEntity = new ArrayList<Entity>();


		light = new Light(new Vector3f(0, 1000, 0), new Vector3f(0.95f, 0.95f, 0.95f));
		lights.add(light);
		TextMaster.removeText(load);
		TextMaster.removeText(text);
		load.remove();
		load = new GUIText("Loading 1%", .5f, font, new Vector2f(0f, -.70f), 1, true);
		load.setColour(0, 1, 0);
		TextMaster.loadText(load);
		TextMaster.render();
		DisplayManager.updateDisplay();


		ModelTexture houseTex = new ModelTexture(loader.loadTexture("wood house texture"));
		ModelTexture grillTex = new ModelTexture(loader.loadTexture("grill wall"));
		ModelTexture polytreeTexture = new ModelTexture(loader.loadTexture("lowPolyTree"));
		ModelTexture fernTex = new ModelTexture(loader.loadTexture("grass"));
		ModelTexture buildTex = new ModelTexture(loader.loadTexture("building"));
		ModelTexture cocTex = new ModelTexture(loader.loadTexture("Coconut tree"));
		ModelData houseData = OBJFileLoader.loadOBJ("wood house");
		ModelData checkPoint = OBJFileLoader.loadOBJ("check_point");
		RawModel checkPointRawModel = loader.LoadToVAO(checkPoint.getVertices(), checkPoint.getTextureCoords(), checkPoint.getNormals(), checkPoint.getIndices());

		RawModel houseRawModel = loader.LoadToVAO(houseData.getVertices(), houseData.getTextureCoords(), houseData.getNormals(), houseData.getIndices());
		ModelData buildingData = OBJFileLoader.loadOBJ("building");
		RawModel buildingRawModel = loader.LoadToVAO(buildingData.getVertices(), buildingData.getTextureCoords(), buildingData.getNormals(), buildingData.getIndices());
		ModelData polyTreeData = OBJFileLoader.loadOBJ("lowpolyTree");
		RawModel polyTreeModel = loader.LoadToVAO(polyTreeData.getVertices(), polyTreeData.getTextureCoords(), polyTreeData.getNormals(), polyTreeData.getIndices());
		ModelData fernData = OBJFileLoader.loadOBJ("grass model");
		RawModel fernModel = loader.LoadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		ModelData cocTreeData = OBJFileLoader.loadOBJ("coconut_tree");
		RawModel cocTreeRawModel = loader.LoadToVAO(cocTreeData.getVertices(), cocTreeData.getTextureCoords(), cocTreeData.getNormals(), cocTreeData.getIndices());
		ModelData grilldata = OBJFileLoader.loadOBJ("grill wall");
		RawModel grillRawModel = loader.LoadToVAO(grilldata.getVertices(), grilldata.getTextureCoords(), grilldata.getNormals(), grilldata.getIndices());


		TextureModel house = new TextureModel(houseRawModel, houseTex);
		TextureModel fernTexModel = new TextureModel(fernModel, fernTex);
		TextureModel polyTreeTexModel = new TextureModel(polyTreeModel, polytreeTexture);
		TextureModel buildingModel = new TextureModel(buildingRawModel, buildTex);
		TextureModel cocTreeModel = new TextureModel(cocTreeRawModel, cocTex);
		TextureModel checkPointModel = new TextureModel(checkPointRawModel, polytreeTexture);
		TextureModel grillModel = new TextureModel(grillRawModel, grillTex);
		modelList.put("house", house);
		modelList.put("grass", fernTexModel);
		modelList.put("tree", polyTreeTexModel);
		modelList.put("building", buildingModel);
		modelList.put("Cococnut Tree", cocTreeModel);
		modelList.put("check_point", checkPointModel);
		modelList.put("grill", grillModel);
		TextMaster.removeText(load);
		load.remove();
		load = new GUIText("Loading 10%", .5f, font, new Vector2f(0f, -.70f), 1, true);
		load.setColour(0, 1, 0);
		TextMaster.loadText(load);
		TextMaster.render();
		DisplayManager.updateDisplay();
/*****
 String modelDetailsFile="res/map.details";
 String checkPointFile ="res/track.details";
 try{
 BufferedReader reader=new BufferedReader(new FileReader(modelDetailsFile));
 String line;
 while((line=reader.readLine())!=null){
 String str[]=line.split(" ");
 TextureModel model=modelList.get(str[0]);
 if (model==null) continue;
 float x= Float.parseFloat(str[1]);
 float y=Float.parseFloat(str[2]);
 float z=Float.parseFloat(str[3]);
 float rx=Float.parseFloat(str[4]);
 float ry=Float.parseFloat(str[5]);
 float rz=Float.parseFloat(str[6]);
 float s=Float.parseFloat(str[7]);
 Entity entity=new Entity(model,new Vector3f(x,y,z),rx,ry,rz,s,null);
 entities.add(entity);
 }
 } catch (FileNotFoundException e1) {
 e1.printStackTrace();
 } catch (IOException e1) {
 e1.printStackTrace();
 }
 TextMaster.removeText(load);
 load.remove();
 load=new GUIText("Loading 20%",.5f,font,new Vector2f(0f,-.70f),1,true);
 load.setColour(0,1,0);
 TextMaster.loadText(load);
 TextMaster.render();
 DisplayManager.updateDisplay();

 /*try{
 BufferedReader reader=new BufferedReader(new FileReader(checkPointFile));
 String line;
 checkPointList.clear();
 while((line=reader.readLine())!=null){
 checkPointList.add(line);
 nextCheckPointId++;
 }
 } catch (FileNotFoundException e1) {
 e1.printStackTrace();
 } catch (IOException e1) {
 e1.printStackTrace();
 }*/


		/****@Entity****/

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("pitch"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("sand"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture rNorTexture = new TerrainTexture(loader.loadTexture("norpitch"));
		TerrainTexture gNorTexture = new TerrainTexture(loader.loadTexture("cross"));
		TerrainTexture bNorTexture = new TerrainTexture(loader.loadTexture("norGrass"));
		TerrainTexture bgNorTexture = new TerrainTexture(loader.loadTexture("norGrass"));
		TerrainTexture blendMap;// = new TerrainTexture(loader.loadTexture("map"));
		NormaledTerrainTexturePack texturePack = new NormaledTerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture, rNorTexture, gNorTexture, bNorTexture, bgNorTexture);
		//NormaledTerrain terrain=new NormaledTerrain(0,0,loader,texturePack,blendMap,"plain height");
		//TerrainTexturePack gtexturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);//,rNorTexture,gNorTexture,bNorTexture,bgNorTexture);
		int k = 0;
		TextMaster.removeText(load);
		//terrains.add(terrain);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				 blendMap=new TerrainTexture(loader.loadTexture("map/a "+j+i));
				NormaledTerrain terrain = new NormaledTerrain(i, j, loader, texturePack, blendMap, "plain height");
				String str = String.valueOf(j) + "," + String.valueOf(i);
				tr.put(terrain, str);
				terrains.add(terrain);
				k++;
				TextMaster.removeText(load);
				load.remove();
				load = new GUIText("Loading " + String.valueOf(20 + k * 2) + "%", .5f, font, new Vector2f(0f, -.70f), 1, true);
				load.setColour(0, 1, 0);
				TextMaster.loadText(load);
				TextMaster.render();
				DisplayManager.updateDisplay();
			}
		}



		ModelTexture pTexture=new ModelTexture(loader.loadTexture("car texture"));
		pTexture.setReflectivity(2);
		pTexture.setShineDamper(100);
		ModelData pData=OBJFileLoader.loadOBJ("car final");
		RawModel pRawModel=loader.LoadToVAO(pData.getVertices(), pData.getTextureCoords(), pData.getNormals(), pData.getIndices());
		TextureModel pModel=new TextureModel(pRawModel,pTexture);
		Entity Eplayer=new Entity(pModel,new Vector3f(0,1,0),0,180,0,3f,OBJFileLoader.loadOBJ("car collide"));


		ModelData rockModel=OBJFileLoader.loadOBJ("rock");
		RawModel rockRawModel=loader.LoadToVAO(rockModel.getVertices(),rockModel.getTextureCoords(),rockModel.getNormals(),rockModel.getIndices());
		//EntityTerrain entityTerrain=new EntityTerrain(rockRawModel,gtexturePack,blendMap,new Vector3f(-400,1,-200),0,0,0,50);



		ModelTexture mt1=new ModelTexture(loader.loadTexture("/car_hd/black"));
		ModelTexture mt2=new ModelTexture(loader.loadTexture("/car_hd/body"));
		ModelTexture mt3=new ModelTexture(loader.loadTexture("/car_hd/glass"));
		ModelTexture mt4=new ModelTexture(loader.loadTexture("/car_hd/gray"));
		ModelTexture mt5=new ModelTexture(loader.loadTexture("/car_hd/red"));
		ModelTexture mt6=new ModelTexture(loader.loadTexture("/car_hd/tire"));
		ModelTexture mt7=new ModelTexture(loader.loadTexture("/car_hd/white"));
		ModelData data1=OBJFileLoader.loadOBJ("/car_hd/black");
		RawModel rm1=loader.LoadToVAO(data1.getVertices(), data1.getTextureCoords(), data1.getNormals(), data1.getIndices());
		ModelData data2=OBJFileLoader.loadOBJ("/car_hd/body");
		RawModel rm2=loader.LoadToVAO(data2.getVertices(), data2.getTextureCoords(), data2.getNormals(), data2.getIndices());
		ModelData data3=OBJFileLoader.loadOBJ("/car_hd/glass");
		RawModel rm3=loader.LoadToVAO(data3.getVertices(), data3.getTextureCoords(), data3.getNormals(), data3.getIndices());
		ModelData data4=OBJFileLoader.loadOBJ("/car_hd/gray");
		RawModel rm4=loader.LoadToVAO(data4.getVertices(), data4.getTextureCoords(), data4.getNormals(), data4.getIndices());
		ModelData data5=OBJFileLoader.loadOBJ("/car_hd/red");
		RawModel rm5=loader.LoadToVAO(data5.getVertices(), data5.getTextureCoords(), data5.getNormals(), data5.getIndices());
		ModelData data6=OBJFileLoader.loadOBJ("/car_hd/tire");
		RawModel rm6=loader.LoadToVAO(data6.getVertices(), data6.getTextureCoords(), data6.getNormals(), data6.getIndices());
		ModelData data7=OBJFileLoader.loadOBJ("/car_hd/white");
		RawModel rm7=loader.LoadToVAO(data7.getVertices(), data7.getTextureCoords(), data7.getNormals(), data7.getIndices());
		TextureModel tm1=new TextureModel(rm1,mt1);
		TextureModel tm2=new TextureModel(rm2,mt2);
		TextureModel tm3=new TextureModel(rm3,mt3);
		TextureModel tm4=new TextureModel(rm4,mt4);
		TextureModel tm5=new TextureModel(rm5,mt5);
		TextureModel tm6=new TextureModel(rm6,mt6);
		TextureModel tm7=new TextureModel(rm7,mt7);
		List<TextureModel> carModel=new ArrayList<>();
		carModel.add(tm1);
		carModel.add(tm2);
		carModel.add(tm3);
		carModel.add(tm4);
		carModel.add(tm5);
		carModel.add(tm6);
		carModel.add(tm7);
		EntityPack pack=new EntityPack(carModel,new Vector3f(-400,0,-400),0,180,0,1.5f,OBJFileLoader.loadOBJ("/car_hd/collide"));



		player=new Player(pack);
		TextMaster.removeText(load);
		load.remove();
		load=new GUIText("Loading 100%",.5f,font,new Vector2f(0f,-.70f),1,true);
		load.setColour(0,1,0);
		TextMaster.loadText(load);
		TextMaster.render();
		DisplayManager.updateDisplay();
		TextMaster.removeText(load);
		TextMaster.removeText(text);
		load.remove();
		text.remove();

		                            		/*Player*/
		ModelTexture terrainTexture=new ModelTexture(loader.loadTexture("grass"));
		terrainTexture.setShineDamper(1.0f);
		
		
		
		
		MasterRenderer renderer=new MasterRenderer(loader);

		
		
		/* Terrain Texture*/
		boolean check=false;
		Camera camera=new Camera(player);
		TextMaster.loadText(text);
		MousePicker picker=new MousePicker(camera,renderer.getProjectionMatrix(),terrains);
		while(!Display.isCloseRequested()){

			//collision();
			TextMaster.removeText(text);
			text.remove();


			light.setColor(new Vector3f(1f,1f,1f));
			light.setPosition(new Vector3f(player.getPosition().x,500,player.getPosition().z));
			skycolor=new Vector3f(0.6f,.6f,.85f);
			NormaledTerrain playerTerrain=getTerrainIndex(player.getPosition().x,player.getPosition().z);
			player.move(playerTerrain);
			camera.move();
			picker.update();
			renderer.procesNormalTerrain(playerTerrain);
			text=new GUIText(tr.get(playerTerrain).toString(),2f,font,new Vector2f(.5f,.5f),.5f,false);
			TextMaster.loadText(text);
			int count=0;
			for (NormaledTerrain t:terrains){
				/*if (Maths.getDistance(player,new Vector3f(t.getX(),0,t.getZ()))<1600){
					if (Maths.getDistance(player,new Vector3f(t.getX(),0,t.getZ()))<1000) {
						renderer.procesNormalTerrain(t);
						count++;
					}
					else{
						Vector3f vert=new Vector3f();
						Vector3f.sub(new Vector3f(t.getX(),0,t.getZ()),player.getPosition(),vert);
						vert.normalise(vert);
						if (Vector3f.dot(vert,player.getFrontDirection())>0){
							renderer.procesNormalTerrain(t);
							count++;
						}
					}
				}*/
				renderer.procesNormalTerrain(t);

			}
			System.out.println(count);

			for(Entity e:entities){

				//if (Maths.getDistance(player,e)<300)
				renderer.processEntity(e);
				
			}
		//	renderer.processEntityTerrain(entityTerrain);
			for(Entity e:grassEntity){
				
				renderer.processEntity(e);
			}
			renderer.render(player,lights, camera,skycolor);
			text.setColour(1,0,0);
			text.setPosition(new Vector2f(.05f,-.5f));
			TextMaster.render();
			
			DisplayManager.updateDisplay();
			
			
			
		}
		TextMaster.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}
	
	static NormaledTerrain getTerrainIndex(float x,float z){
		for (NormaledTerrain t:terrains){
			if (x<t.getX() && x>t.getX()-800 && z<t.getZ() && z>t.getZ()-800) return t;
		}
		return terrains.get(0);
	}
	private static void collision(){

		for (Entity entity:entities){
				if (Maths.getDistance(player,entity)<20){
			//	if (Collision.testInterSection(player.getEntity(),speed,entity,new Vector3f(),.01f)){
				if (Collision.testInterSection(player.getEntity(), entity)){
					player.velocity.x=-5*player.velocity.x;
					player.velocity.z=-5*player.velocity.z;
					player.setCurrentRunSpeed(0);
					player.collideUpdate();

				}
			}
			
		}
	}
	
	static Light light;
	private static Vector3f skycolor;

}


