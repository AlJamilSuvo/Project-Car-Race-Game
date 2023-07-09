package mapeditior;

import entities.Entity;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by Suvo on 03-Dec-15.
 */
public class Editor extends JFrame{

    JButton addEntity;
    JButton startPreview;
    JButton loadPrev;
    JButton saveMap;
    JButton set;
    JComboBox<String> entityList;
    JFileChooser fileChooser;
    JComboBox<String> renderingEntityList;
    JComboBox<String> checkpoints;
    JButton deleteEntity;
    JTextField entitySize;
    JTextField entityRotX;
    JTextField entityRotY;
    JTextField entityRotZ;
    JButton addCheckPoint;
    List<String> checkPointList=new ArrayList<>();
    JButton setCheckPoint;
    HashMap<String, TextureModel> modelList;
    HashMap<Integer,String> renderList=new HashMap<>();
    int testingIndex;
    int nextCheckPointId=0;
    String testingModel;
    List<Entity> entities=new ArrayList<>();
    static MainGameTester tester;



    public Editor()  {
        super("Map Editor");

        loadPrev=new JButton("Load Map");
        addEntity=new JButton("Add New Entity");
        startPreview=new JButton("Start Preview");
        entityList=new JComboBox<>();
        fileChooser=new JFileChooser();
        renderingEntityList=new JComboBox<>();
        set=new JButton("Set This Entity");
        deleteEntity=new JButton("Delete Entity");
        entitySize=new JTextField();
        entityRotX=new JTextField();
        entityRotY=new JTextField();
        entityRotZ=new JTextField();
        addCheckPoint=new JButton("Add Check Point");
        setCheckPoint=new JButton("Set Check Point");
        saveMap=new JButton("Save Map");
        JPanel jPanel=new JPanel();
        jPanel.setLayout(new GridLayout(20,1));
        jPanel.add(startPreview);
        jPanel.add(loadPrev);
        jPanel.add(renderingEntityList);
        jPanel.add(addEntity);
        jPanel.add(entityList);
        jPanel.add(set);
        jPanel.add(deleteEntity);
        jPanel.add(new JLabel("Entity Size:"));
        jPanel.add(entitySize);
        jPanel.add(new JLabel("Rot X:"));
        jPanel.add(entityRotX);
        jPanel.add(new JLabel("Rot Y:"));
        jPanel.add(entityRotY);
        jPanel.add(new JLabel("Rot Z:"));
        jPanel.add(entityRotZ);
        jPanel.add(addCheckPoint);
        jPanel.add(setCheckPoint);
        jPanel.add(new JLabel(""));
        jPanel.add(saveMap);
        add(jPanel);
        setSize(200,700);


        startPreview.addActionListener(e -> {

            entities=MainGameTester.getEntities();
            modelList=MainGameTester.getModelList();
            for(String s:modelList.keySet()){
                entityList.addItem(s);
            }
        });
        loadPrev.addActionListener(e->{
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
                    int index=MainGameTester.addEntity(model,new Vector3f(x,y,z),rx,ry,rz,s);
                    renderList.put(index,str[0]);
                    String st=index+" "+str[0]+" ("+x+","+y+","+z+")";
                    renderingEntityList.addItem(st);
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
                    checkPointList.add(line);
                    nextCheckPointId++;
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        renderingEntityList.addActionListener(e->{
            String str=(String) renderingEntityList.getSelectedItem();
            String st[]=str.split(" ");
            int index=Integer.parseInt(st[0]);
            testingIndex=index;
            MainGameTester.setTestEntity(index);
            entityRotX.setText(String.valueOf(MainGameTester.getEntity(index).getRotX()));
            entityRotY.setText(String.valueOf(MainGameTester.getEntity(index).getRotY()));
            entityRotZ.setText(String.valueOf(MainGameTester.getEntity(index).getRotZ()));
            entitySize.setText(String.valueOf(MainGameTester.getEntity(index).getScale()));
            MainGameTester.setCamera(MainGameTester.getEntity(index).getPosition());
        });

        entityList.addActionListener(e->{
            String str=(String)entityList.getSelectedItem();
            TextureModel model=modelList.get(str);
            testingModel=str;
            testingIndex=MainGameTester.addEntity(model,new Vector3f(0,0,0),0,0,0,1);
            MainGameTester.setTestEntity(testingIndex);
            entityRotX.setText("0");
            entityRotY.setText("0");
            entityRotZ.setText("0");
            entitySize.setText("1");

        });
        set.addActionListener(e->{
            renderList.put(testingIndex,testingModel);
            Vector3f pos=MainGameTester.getEntity(testingIndex).getPosition();
            String st=testingIndex+" "+testingModel+" ("+pos.x+","+pos.z+","+pos.z+")";
            renderingEntityList.addItem(st);
            testingIndex=0;
            MainGameTester.nsetTest();
        });
        deleteEntity.addActionListener(e->{
            Vector3f pos=MainGameTester.getEntity(testingIndex).getPosition();
            String st=testingIndex+" "+testingModel+" ("+pos.x+","+pos.z+","+pos.z+")";
            renderingEntityList.removeItem(st);
            MainGameTester.removeEntity(testingIndex);
            testingIndex=0;
            MainGameTester.nsetTest();
        });

        entitySize.addActionListener(e->{
            float rx=Float.parseFloat(entityRotX.getText());
            float ry=Float.parseFloat(entityRotY.getText());
            float rz=Float.parseFloat(entityRotZ.getText());
            float s=Float.parseFloat(entitySize.getText());
            MainGameTester.setTestParam(rx,ry,rz,s);
        });
        entityRotX.addActionListener(e->{
            float rx=Float.parseFloat(entityRotX.getText());
            float ry=Float.parseFloat(entityRotY.getText());
            float rz=Float.parseFloat(entityRotZ.getText());
            float s=Float.parseFloat(entitySize.getText());
            MainGameTester.setTestParam(rx,ry,rz,s);
        });
        entityRotY.addActionListener(e->{
            float rx=Float.parseFloat(entityRotX.getText());
            float ry=Float.parseFloat(entityRotY.getText());
            float rz=Float.parseFloat(entityRotZ.getText());
            float s=Float.parseFloat(entitySize.getText());
            MainGameTester.setTestParam(rx,ry,rz,s);
        });
        entityRotZ.addActionListener(e->{
            float rx=Float.parseFloat(entityRotX.getText());
            float ry=Float.parseFloat(entityRotY.getText());
            float rz=Float.parseFloat(entityRotZ.getText());
            float s=Float.parseFloat(entitySize.getText());
            MainGameTester.setTestParam(rx,ry,rz,s);
        });
        addCheckPoint.addActionListener(e->{
            TextureModel model=modelList.get("check_point");
            testingModel="check_point";
            testingIndex=MainGameTester.addEntity(model,new Vector3f(0,0,0),0,0,0,1);
            MainGameTester.setTestEntity(testingIndex);
        });
        setCheckPoint.addActionListener(e->{
            String str=String.valueOf(nextCheckPointId);
            Vector3f position=entities.get(testingIndex).getPosition();
            str+=" "+position.x+" "+position.y+" "+position.z;
            checkPointList.add(str);
            MainGameTester.removeEntity(testingIndex);
            testingIndex=0;
        });
        saveMap.addActionListener(e->{
            try {
                BufferedWriter writer=new BufferedWriter(new FileWriter("res/map.details"));
                for (Entity entity:entities){
                    int index=entities.indexOf(entity);
                    String name=renderList.get(index);
                    writer.write(name+" "+entity.getPosition().x+" "+entity.getPosition().y+" "+entity.getPosition().z+" "+
                    entity.getRotX()+" "+entity.getRotY()+" "+entity.getRotZ()+" "+entity.getScale()+"\n");

                }
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String checkPointFile ="res/track.details";
            try {
                BufferedWriter writer=new BufferedWriter(new FileWriter(checkPointFile));
                for(String str:checkPointList){
                    writer.write(str+"\n");
                }
                writer.flush();
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        show();
   ;
    }



    public static void main(String[] args) {
        new Editor();
        tester=new MainGameTester();
    }

}
