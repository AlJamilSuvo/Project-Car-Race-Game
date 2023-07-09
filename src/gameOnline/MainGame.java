package gameOnline;

import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import randerEngine.DisplayManager;
import randerEngine.Loader;
import randerEngine.MasterRenderer;
import terrains.NormaledTerrain;
import textures.ModelTexture;
import textures.NormaledTerrainTexturePack;
import textures.TerrainTexture;
import toolBoxs.Collision;
import toolBoxs.Maths;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class MainGame {
	List<NormaledTerrain> terrains = new ArrayList<>();
	List<Entity> entities = new ArrayList<>();
	List<Vector3f> checkPointList=new ArrayList<>();
	List<Vector3f> checkPoint=new ArrayList<>();
	int currentCheckPoin=0;
	Player player;
	Camera camera;
	List<EntityPack> others = new ArrayList<>();
	List<Vector2f> otherV;
	Light light;
	Vector3f skycolor;
	MasterRenderer renderer;
	List<Light> lights = new ArrayList<>();
	Loader loader;
	GuiRenderer guiRenderer;
	List<Vector3f> otherPos;
	List<Vector3f> otherRot;
	boolean isOK = false;
	HashMap<String,TextureModel> modelList=new HashMap<>();
	HashMap<TextureModel,ModelData> collide=new HashMap<>();
	GUIText text;
	GUIText speedText;

	FontType normalfont;
	FontType speedfont;


	public MainGame(Vector3f playerPos, Vector3f playerRot, List<Vector3f> otherPos, List<Vector3f> otherRot,List<Vector2f> otherV) {
		DisplayManager.createDisplay();
		loader = new Loader();
		TextMaster.init(loader);
		normalfont =new FontType(loader.loadTexture("/font/moire"),new File("res/font/moire.fnt"));
		speedfont =new FontType(loader.loadTexture("/font/quartz"),new File("res/font/quartz.fnt"));
		text=new GUIText("Java Online Car Game",3f, normalfont,new Vector2f(0f,-.5f),1f,true);
		speedText =new GUIText("Speed",.25f,speedfont,new Vector2f(0f,-95f),1f,true);
		this.otherV=otherV;
		this.otherPos = otherPos;
		this.otherRot = otherRot;
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture guibg = new GuiTexture(loader.loadTexture("bg"), new Vector2f(0f, 0.0f), new Vector2f(.5f, .1f));
		GuiTexture guipg = new GuiTexture(loader.loadTexture("pg"), new Vector2f(0f, 0.0f), new Vector2f(0f, .1f));
		guis.add(guibg);
		guis.add(guipg);
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		guipg.setScale(new Vector2f(.05f, .1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		guipg.setScale(new Vector2f(.1f, .1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();
		light = new Light(new Vector3f(0, 1000, 0), new Vector3f(0.95f, 0.95f, 0.95f));
		lights.add(light);


		/*****@Entity****/

		ModelTexture wheat=new ModelTexture(loader.loadTexture("wheat"));
		ModelTexture fernTex=new ModelTexture(loader.loadTexture("fern"));
		//fernTex.setUsingFakeLighting(true);
		//fernTex.setHasTransparency(true);
		ModelData ferndata=OBJFileLoader.loadOBJ("fern");
		RawModel fern=loader.LoadToVAO(ferndata.getVertices(), ferndata.getTextureCoords(), ferndata.getNormals(), ferndata.getIndices());



		// wheat.setUsingFakeLighting(true);
		ModelTexture bTreeTex=new ModelTexture(loader.loadTexture("birch_tree"));
		ModelTexture houseTex=new ModelTexture(loader.loadTexture("wood house texture"));
		ModelTexture grillTex=new ModelTexture(loader.loadTexture("grill wall"));
		ModelTexture polytreeTexture=new ModelTexture(loader.loadTexture("lowPolyTree"));
		ModelTexture grassTex=new ModelTexture(loader.loadTexture("grassTexture"));
		//  grassTex.setUsingFakeLighting(true);
		grassTex.setHasTransparency(true);
		ModelTexture buildTex=new ModelTexture(loader.loadTexture("bush"));
		buildTex.setUsingFakeLighting(true);
		ModelTexture cocTex=new ModelTexture(loader.loadTexture("Coconut tree"));
		ModelData houseData=OBJFileLoader.loadOBJ("wood house");
		ModelData checkPoint=OBJFileLoader.loadOBJ("check_point");
		RawModel checkPointRawModel=loader.LoadToVAO(checkPoint.getVertices(), checkPoint.getTextureCoords(), checkPoint.getNormals(), checkPoint.getIndices());
		RawModel houseRawModel=loader.LoadToVAO(houseData.getVertices(), houseData.getTextureCoords(), houseData.getNormals(), houseData.getIndices());
		ModelData buildingData=OBJFileLoader.loadOBJ("brush");
		RawModel buildingRawModel=loader.LoadToVAO(buildingData.getVertices(), buildingData.getTextureCoords(), buildingData.getNormals(), buildingData.getIndices());
		ModelData polyTreeData=OBJFileLoader.loadOBJ("lowpolyTree");
		RawModel polyTreeModel=loader.LoadToVAO(polyTreeData.getVertices(), polyTreeData.getTextureCoords(), polyTreeData.getNormals(), polyTreeData.getIndices());
		ModelData fernData=OBJFileLoader.loadOBJ("grass");
		RawModel fernModel=loader.LoadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		ModelData cocTreeData=OBJFileLoader.loadOBJ("coconut_tree");
		RawModel cocTreeRawModel=loader.LoadToVAO(cocTreeData.getVertices(), cocTreeData.getTextureCoords(), cocTreeData.getNormals(), cocTreeData.getIndices());
		ModelData grilldata=OBJFileLoader.loadOBJ("grill wall");
		RawModel grillRawModel=loader.LoadToVAO(grilldata.getVertices(), grilldata.getTextureCoords(), grilldata.getNormals(), grilldata.getIndices());
		ModelData bTreedata =OBJFileLoader.loadOBJ("birch_tree");
		RawModel bTreeRawModel=loader.LoadToVAO(bTreedata.getVertices(), bTreedata.getTextureCoords(), bTreedata.getNormals(), bTreedata.getIndices());


		TextureModel house=new TextureModel(houseRawModel,houseTex);
		TextureModel fernTexModel=new TextureModel(fernModel,grassTex);
		TextureModel polyTreeTexModel=new TextureModel(polyTreeModel,polytreeTexture);
		TextureModel buildingModel=new TextureModel(buildingRawModel,buildTex);
		TextureModel cocTreeModel=new TextureModel(cocTreeRawModel,cocTex);
		TextureModel checkPointModel=new TextureModel(checkPointRawModel,polytreeTexture)   ;
		TextureModel grillModel=new TextureModel(grillRawModel,grillTex);
		TextureModel treeModel=new TextureModel(bTreeRawModel,bTreeTex);
		TextureModel wheatModel=new TextureModel(fernModel,wheat);
		TextureModel FernModel=new TextureModel(fern,fernTex);
		modelList.put("house",house);
		modelList.put("grass",fernTexModel);
		modelList.put("tree",polyTreeTexModel);
		modelList.put("bush",buildingModel);
		modelList.put("Cococnut_Tree",cocTreeModel);
		modelList.put("check_point",checkPointModel);
		modelList.put("grill",grillModel);
		modelList.put("Birch_Tree",treeModel);
		modelList.put("Wheat",wheatModel);
		modelList.put("Fern",FernModel);





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

		//terrains.add(terrain);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				blendMap=new TerrainTexture(loader.loadTexture("map/a "+j+i));
				NormaledTerrain terrain = new NormaledTerrain(i, j, loader, texturePack, blendMap, "plain height");
				terrains.add(terrain);
			}
		}
		collide.put(grillModel,OBJFileLoader.loadOBJ("wall collide"));
		collide.put(buildingModel,OBJFileLoader.loadOBJ("bush_collide"));


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
				Entity entity=new Entity(model,new Vector3f(x,y,z),rx,ry,rz,s,collide.get(model));
				entities.add(entity);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try{
			BufferedReader reader=new BufferedReader(new FileReader(checkPointFile));
			String line;
			checkPointList.clear();
			while((line=reader.readLine())!=null){

				float x,y,z;
				String []s=line.split(" ");
				x=Float.parseFloat(s[1]);
				y=Float.parseFloat(s[2]);
				z=Float.parseFloat(s[3]);
				checkPointList.add(new Vector3f(x,y,z));
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		/****@Entity****/

		guipg.setScale(new Vector2f(.2f, .1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();




		guipg.setScale(new Vector2f(.3f, .1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();

		guipg.setScale(new Vector2f(.4f, .1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();

		guipg.setScale(new Vector2f(.5f, .1f));
		guiRenderer.renderer(guis);
		DisplayManager.updateDisplay();


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
		EntityPack pack=new EntityPack(carModel,playerPos,playerRot.x,playerRot.y,playerRot.z,1.5f,OBJFileLoader.loadOBJ("/car_hd/collide"));



		player=new Player(pack);




		for (int i = 0; i < otherPos.size(); i++) {
			EntityPack otherPlayer = new EntityPack(carModel, otherPos.get(i), otherRot.get(i).x, otherRot.get(i).y, otherRot.get(i).z, 1.5f, OBJFileLoader.loadOBJ("/car_hd/collide"));
			others.add(otherPlayer);
			otherPlayer.setCheckPoint(0);
		}





		renderer = new MasterRenderer(loader);
		camera = new Camera(player);
		run();


	}

	private void setUpOthers() {
		for (int i = 0; i < otherPos.size(); i++) {
			others.get(i).setPosition(otherPos.get(i));
			others.get(i).setRotX(otherRot.get(i).x);
			others.get(i).setRotY(otherRot.get(i).y);
			others.get(i).setRotZ(otherRot.get(i).z);
			others.get(i).setVelocity(new Vector3f(otherV.get(i).x,0,otherV.get(i).y));
			Vector3f nextCheckPoint=checkPointList.get(others.get(i).getCheckPoint()+1);
			if (Maths.getDistance(others.get(i).getPosition(),nextCheckPoint)<5){
				others.get(i).setCheckPoint(others.get(i).getCheckPoint()+1);
				if (others.get(i).checkPoint==checkPointList.size()) System.out.println("Other :"+i+" Win");

			}
		}
	}



	public void run() {
		new InfoUpdater();
		try {
			Game.sendOK();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TextMaster.init(loader);
		while (!Display.isCloseRequested()) {
			collision();
			if (Game.isCollision){
				float cx,cy,cry;
				cx=Game.getCx();
				cy=Game.getCy();
				cry=Game.getCry();
				Game.setIsCollision(false);
				addCollision(cx,cy,cry);
			}
			TextMaster.removeText(text);
			TextMaster.removeText(speedText);
			text.remove();
			speedText.remove();
			otherPos = Game.getOtherPos();
			otherRot = Game.getOtherRot();
			if (!isOK) isOK = Game.isOK();
			setUpOthers();
			long ping = Game.getUpdateInfo();
			light.setPosition(new Vector3f(player.getPosition().x, 500, player.getPosition().z));
			if (ping != -1) DisplayManager.setTitle(ping);
			light.setColor(new Vector3f(.95f, .95f, .95f));
			skycolor = new Vector3f(0.544f, 0.62f, .69f);


			player.move(getTerrainIndex(player.getPosition().x, player.getPosition().z));
			int sp=(int)player.getSpeeed();
			speedText=new GUIText("Speed : "+String.valueOf(sp)+" KM/H",1f,speedfont,new Vector2f(0,-1f),1f,false);
			speedText.setColour(1,1,1);
			camera.move();
			setCheckPoint();

			text=new GUIText(getCurrentStanding(),1f, normalfont,new Vector2f(0,0),1f,false);
			TextMaster.loadText(speedText);
			for (NormaledTerrain t : terrains) {
				renderer.procesNormalTerrain(t);
			}
			for (Entity e : entities) {
				if (Maths.getDistance(player, e) < 300) renderer.processEntity(e);
			}
			for (EntityPack e : others) {

				renderer.renderEntityPack(e);
			}
			renderer.render(player, lights, camera, skycolor);
			text.setColour(1,1,1);
			TextMaster.render();
			DisplayManager.updateDisplay();


		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

	NormaledTerrain getTerrainIndex(float x, float z) {
		for (NormaledTerrain t : terrains) {
			if (x < t.getX() && x > t.getX() - 800 && z < t.getZ() && z > t.getZ() - 800) return t;
		}
		return terrains.get(0);
	}
	private void collision() {
		for (Entity entity : entities) {
			entity.getModel().getModelTexture().setUsingFakeLighting(false);
			Vector3f vert = new Vector3f();
			Vector3f.sub(player.getPosition(), entity.getPosition(), vert);
			Vector3f speed = new Vector3f(player.getSpeedDirection().x * Math.abs(player.getCurrentRunSpeed()), 0,
					player.getSpeedDirection().z * Math.abs(player.getCurrentRunSpeed()));
			if (Maths.getDistance(player, entity) < 50) {

				if (Collision.testInterSection(player.getEntity(), entity)) {
					player.velocity.x*=-.6f;
					player.velocity.z*=-.6f;
					player.collideUpdate();

				}
			}

		}
		for (EntityPack entity : others) {
			Vector3f vert = new Vector3f();
			Vector3f.sub(entity.getPosition(), player.getPosition(), vert);
			float speed = player.getSpeeed();
			if (Maths.getDistance(player, entity) < 50) {
				if (Collision.testInterSection(player.getEntity(), entity)) {
					Vector3f collisionDirection=new Vector3f();
					if (Vector3f.dot(vert, player.getFrontDirection()) > 0) {
						collisionDirection=player.getFrontDirection();
					}
					else collisionDirection=new Vector3f(-player.getFrontDirection().x,0,-player.getFrontDirection().z);
					Vector3f collisionPerpendicular=new Vector3f(collisionDirection.x,0,-collisionDirection.z);
					Vector3f otherFront=new Vector3f((float)Math.sin(Math.toRadians(entity.getRotY())),0,(float)Math.cos(Math.toRadians(entity.getRotY())));
					float otherCollisionAngle=Vector3f.angle(collisionDirection,otherFront);
					otherFront.normalise(otherFront);
					float otherSpeed=Maths.getLength(entity.getVelocity());
					float otherSpeedPer=otherSpeed*(float) Math.cos(Vector3f.angle(otherFront,collisionPerpendicular));
					float plyerSpeed=otherSpeed*(float) Math.cos(Vector3f.angle(otherFront,collisionDirection));
					float otherColSpeed=speed;
					float ox=otherSpeedPer*(float) Math.cos(Vector3f.angle(new Vector3f(1,0,0),collisionPerpendicular))+
							otherColSpeed*(float) Math.cos(Vector3f.angle(new Vector3f(1,0,0),collisionDirection));
					float oz=otherSpeedPer*(float) Math.cos(Vector3f.angle(new Vector3f(0,0,1),collisionPerpendicular))+
							otherColSpeed*(float) Math.cos(Vector3f.angle(new Vector3f(0,0,1),collisionDirection));
					float ry=(float) Math.atan(ox/oz);
					int index=others.indexOf(entity);
					Game.collision(index,ox,oz,-ry);
					float px=plyerSpeed*(float) Math.cos(Vector3f.angle(new Vector3f(1,0,0),collisionDirection));
					float pz=plyerSpeed*(float) Math.cos(Vector3f.angle(new Vector3f(0,0,1),collisionDirection));
					float pry;
					if (pz!=0)pry=(float)Math.toDegrees(Math.atan(px/pz));
					else pry=90;
					player.velocity.x=px;
					player.velocity.z=pz;
					player.increaseRotation(0,pry,0);
					player.collideTime=60;
					player.getEntity().increasePosition(player.velocity.x*DisplayManager.getFrameTimeSeconds(),0,player.velocity.z*DisplayManager.getFrameTimeSeconds());
				//	System.out.println(px+" "+pz+" "+pry+" "+" "+ox+" "+oz+" "+ry);

				}

			}
		}
	}

	public void addCollision(float dx,float dz,float ry){
		player.velocity.x=dx;
		player.velocity.z=dz;
		player.increaseRotation(0,ry,0);
		player.getEntity().increasePosition(player.velocity.x*DisplayManager.getFrameTimeSeconds(),0,player.velocity.z*DisplayManager.getFrameTimeSeconds());
	}


	private void setCheckPoint(){
		Vector3f nextCheckPoint=checkPointList.get(currentCheckPoin+1);
		if (Maths.getDistance(player,nextCheckPoint)<5){
			currentCheckPoin++;
			if (currentCheckPoin==checkPointList.size()) System.out.println("Player Win");

		}

	}


	private String getCurrentStanding(){
		String str="";
		if (others.get(0).checkPoint>currentCheckPoin){
			str="1. Friend 2.You";
		}
		else if (currentCheckPoin>others.get(0).checkPoint){
			str="1. You 2.Friend";
		}
		else{
			Vector3f nextCheckPoint=checkPointList.get(currentCheckPoin+1);
			if (Maths.getDistance(player,nextCheckPoint)<Maths.getDistance(others.get(0).getPosition(),nextCheckPoint)){
				str="1. You 2.Friend";
			}
			else str="1.Friend 2.You";
		}
		return str;
	}




	
	
	
	
	
	class InfoUpdater implements Runnable{
		
		Thread t;
		InfoUpdater(){
			t=new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			while(true){
				Game.setPlayer(player);
				try {
					t.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
}