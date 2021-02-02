package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.hardgforgif.dragonboatracing.GameData;

import java.lang.reflect.Type;
import java.util.Random;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class Lane {
    public int laneNo;
    private float[][] leftBoundary;
    private int leftIterator = 0;
    private float[][] rightBoundary;
    private int rightIterator = 0;
    private MapLayer leftLayer;
    private MapLayer rightLayer;

    private Obstacle[] obstacles;

    public Lane(int mapHeight, MapLayer left, MapLayer right, int nrObstacles, int laneNo_){
        leftBoundary = new float[mapHeight][2];
        rightBoundary = new float[mapHeight][2];

        leftLayer = left;
        rightLayer = right;

        obstacles = new Obstacle[nrObstacles];
        laneNo = laneNo_;
    }

    /**
     * Construct bodies that match the lane separators
     * @param unitScale The size of a tile in pixels
     */
    public void constructBoundaries(float unitScale){
        MapObjects objects = leftLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)){
            Rectangle rectangle = rectangleObject.getRectangle();
            float height = rectangle.getY() * unitScale;
            float limit = (rectangle.getX() * unitScale) + (rectangle.getWidth() * unitScale);
            leftBoundary[leftIterator][0] = height;
            leftBoundary[leftIterator++][1] = limit;
        }

        objects = rightLayer.getObjects();

        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)){
            Rectangle rectangle = rectangleObject.getRectangle();
            float height = rectangle.getY() * unitScale;
            float limit = rectangle.getX() * unitScale;
            rightBoundary[rightIterator][0] = height;
            rightBoundary[rightIterator++][1] = limit;
        }
    }

    public float[] getLimitsAt(float yPosition){
        float[] lst = new float[2];
        int i;
        for (i = 1; i < leftIterator; i++){
            if (leftBoundary[i][0] > yPosition) {
                break;
            }
        }
        lst[0] = leftBoundary[i - 1][1];

        for (i = 1; i < rightIterator; i++){
            if (rightBoundary[i][0] > yPosition) {
                break;
            }
        }
        lst[1] = rightBoundary[i - 1][1];
        return lst;
    }

    /**
     * Spawn obstacles on the lane
     * @param world World to spawn obstacles in
     * @param mapHeight Height of the map to draw on
     */
    public void spawnObstacles(World world, float mapHeight){
        int nrObstacles = obstacles.length;
        float segmentLength = mapHeight / nrObstacles;
        for (int i = 0; i < nrObstacles; i++){
            int randomIndex = new Random().nextInt(6);
            float scale = 0f;
            if (randomIndex == 0 || randomIndex == 5)
                scale = -0.8f;
            obstacles[i] = new Obstacle("Obstacles/Obstacle" + (randomIndex + 1) + ".png");
            obstacles[i].setObstacleType(randomIndex+1);
            float segmentStart = i * segmentLength;
            float yPos = (float) (600f + (segmentStart + Math.random() * segmentLength));

            float[] limits = this.getLimitsAt(yPos);
            float leftLimit = limits[0] + 50;
            float rightLimit = limits[1];
            float xPos = (float) (leftLimit + Math.random() * (rightLimit - leftLimit));


            obstacles[i].createObstacleBody(world, xPos / GameData.METERS_TO_PIXELS, yPos / GameData.METERS_TO_PIXELS,
                    "Obstacles/Obstacle" + (randomIndex + 1) + ".json", scale);
        }
    }

    /**
     * JSON Serializer for Lane, will just return laneNo (0-6)
     * @return laneNo in JSON form.
     */
    public static class LaneSerializer implements JsonSerializer<Lane> {
        public JsonElement serialize(Lane aLane, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(aLane.laneNo);
        }
    }

    //getters
    public float[][] getLeftBoundary(){
        return this.leftBoundary;
    }

    public int getLeftIterator(){
        return this.leftIterator;
    }

    public float[][] getRightBoundary(){
        return this.rightBoundary;
    }

    public int getRightIterator(){
        return this.rightIterator;
    }

    public MapLayer getLeftLayer(){
        return this.leftLayer;
    }

    public MapLayer getRightLayer(){
        return this.rightLayer;
    }

    public Obstacle[] getObstacles(){
        return this.obstacles;
    }


}
