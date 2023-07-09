package entities;

import models.TextureModel;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import randerEngine.DisplayManager;
import terrains.NormaledTerrain;
import terrains.Terrain;
import toolBoxs.Maths;

import java.util.List;


public class Player {
	
	private static final float TURN_SPEED=80;
	
	private static final float GRAVITY=-50;
	public Vector3f velocity;

	private float currentRunSpeed=0;
	private float currentTurnSpeed=0;
	private float upwardSpeed=0;
	private Vector3f frontDirection;
	EntityPack entity;
	int dragTime;
	public int collideTime;
	float prevAngle;
	public Player(EntityPack entity) {
		this.entity=entity;
		velocity=new Vector3f(0,0,0);
		prevAngle=this.getRotY();
		frontDirection=new Vector3f(0,0,0);
	}
	public void increaseRotation(float dx,float dy,float dz){
		this.entity.increaseRotation(dx, dy, dz);
	}
	public Vector3f getPosition() {
		return this.entity.getPosition();
	}
	public float getRotX() {
		return entity.getRotX();
	}
	public float getRotY() {
		return entity.getRotY();
	}
	public float getRotZ() {
		return entity.getRotZ();
	}
	public float getScale() {
		return entity.getScale();
	}
	private void  setRotZ(float rz){
		this.entity.setRotZ(rz);		
	}
	private void  setRotX(float rx){
		this.entity.setRotX(rx);
		
	}
	



	public List<TextureModel> getModel(){
		return entity.getModel();
	}
	public void moveCalculation(){
		float prev=getRotY();
		Vector3f precDirection=frontDirection;
        entity.increaseRotation(0,this.currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);
		frontDirection.x=(float) Math.sin(Math.toRadians(getRotY()));
		frontDirection.z=(float) Math.cos(Math.toRadians(getRotY()));
		frontDirection.normalise(frontDirection);
		float speed=Maths.getLength(velocity);
		if (speed>=1000){
			currentRunSpeed=0;
		}


		float dx= frontDirection.x*currentRunSpeed*DisplayManager.getFrameTimeSeconds();
		float dz= frontDirection.z*currentRunSpeed*DisplayManager.getFrameTimeSeconds();

		velocity.x+=dx;
		velocity.z+=dz;
		float friction=.3f;
		speed=Maths.getLength(velocity);
		if (prev!=getRotY()){

			dragTime=60;
		}
		if(dragTime>0){
			friction=1.2f;
		}
		if (collideTime>0){
			//this.entity.setRotX(entity.getRotX()-.2f);
			//this.entity.increasePosition(0,-.2f,0);
			//collideTime--;
		}
		else{
			entity.setRotX(0);
		}


		if (speed<1) friction=speed;
			float fdx= -velocity.x*friction*DisplayManager.getFrameTimeSeconds();
			float fdz= -velocity.z*friction*DisplayManager.getFrameTimeSeconds();
			velocity.x+=fdx;
			velocity.z+=fdz;
			//System.out.println(velocity.x+" "+velocity.z+" "+fdx+" "+fdz);

		entity.increasePosition(velocity.x*DisplayManager.getFrameTimeSeconds(),velocity.y,velocity.z*DisplayManager.getFrameTimeSeconds());

	}
	public void collideUpdate(){
		//entity.setRotX(10);
		//this.entity.increasePosition(velocity.x,10,velocity.z);
		//collideTime=60;
	}




    public void move(NormaledTerrain terrain){
        checkInput();
        increaseRotation(0, this.currentTurnSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		moveCalculation();
		upwardSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
		entity.increasePosition(0, upwardSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight=terrain.getTerrainHeight(getPosition().x, getPosition().z);
		if (getPosition().y<terrainHeight){
			upwardSpeed=0;
			getPosition().y=terrainHeight;
		}

    }
	public void move(Terrain terrain){
		checkInput();

		moveCalculation();
		upwardSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
		entity.increasePosition(0, upwardSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight=terrain.getTerrainHeight(getPosition().x, getPosition().z);
		if (getPosition().y<terrainHeight){
			upwardSpeed=0;
			getPosition().y=terrainHeight;
		}
	}
	private void checkInput(){
		if (Keyboard.isKeyDown(Keyboard.KEY_W)||Keyboard.isKeyDown(Keyboard.KEY_UP)){
			if(currentRunSpeed<200) currentRunSpeed+=10f;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)||Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			if (currentRunSpeed>-200) currentRunSpeed-=10f;
		}
		else {

			 currentRunSpeed=0;
		}
		float speed=Maths.getLength(velocity);
		if (speed>5){
			if (Keyboard.isKeyDown(Keyboard.KEY_A)||Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				this.currentTurnSpeed=TURN_SPEED;
			}else if (Keyboard.isKeyDown(Keyboard.KEY_D)||Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				this.currentTurnSpeed=-TURN_SPEED;
			}else{
				this.currentTurnSpeed=0;
			}
		}else{
			this.currentTurnSpeed=0;
		}
		
		
		
	}

	public float getSpeeed(){
		return Maths.getLength(velocity);
	}
	public Vector3f getVelocity() {
		return velocity;
	}

	public Vector3f getSpeedDirection() {
		return velocity;
	}
	public EntityPack getEntity() {
		return entity;
	}
	public float getCurrentRunSpeed() {
		return currentRunSpeed;
	}

	public Vector3f getFrontDirection() {
		return frontDirection;
	}

	public void setCurrentRunSpeed(float currentRunSpeed) {
		this.currentRunSpeed = currentRunSpeed;
	}
}
