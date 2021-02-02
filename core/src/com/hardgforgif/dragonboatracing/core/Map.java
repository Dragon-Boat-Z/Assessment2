package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Json;
import com.google.gson.*;
import com.hardgforgif.dragonboatracing.GameData;

import java.lang.reflect.Type;

public class Map {
    // Map components
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    // The size of the screen we will render the map on
    private float screenWidth;

    // The width and the height of the map in tiles, used to calculate ratios
    private int mapWidth;
    private int mapHeight;

    // The width of each tile in Pixels
    private float unitScale;

    private Lane[] lanes = new Lane[GameData.numberOfBoats];

    private Texture finishLineTexture;
    private Sprite finishLineSprite;
    private Texture startLineTexture;
    private Sprite startLineSprite;

    public Map(String tmxFile, float width){
        tiledMap = new TmxMapLoader().load(tmxFile);
        screenWidth = width;

        MapProperties prop = tiledMap.getProperties();
        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);

        unitScale = screenWidth / mapWidth / 32f;
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
    }

    /**
     * @return The ratio between a tile and a meter in the game world
     */
    public float getTilesToMetersRatio() {
        return ((this.screenWidth / GameData.METERS_TO_PIXELS) / this.mapWidth);
    }

    /**
     * Creates bodies on the edges of the river, based on a pre-made layer of objects in Tiled
     * @param collisionLayerName Name of the Tiled layer with the rectangle objects
     * @param world World to spawn the bodies in
     */
    public void createMapCollisions(String collisionLayerName, World world) {
        // Get the objects from the object layer in the tilemap
        MapLayer collisionLayer = tiledMap.getLayers().get(collisionLayerName);
        MapObjects objects = collisionLayer.getObjects();

        // Iterate through the rectangles and create their physic bodies
        for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;

            // Find where we need to place the physics body
            float positionX = (rectangle.getX() * unitScale / GameData.METERS_TO_PIXELS) +
                                (rectangle.getWidth() * unitScale / GameData.METERS_TO_PIXELS / 2);
            float positionY = (rectangle.getY() * unitScale / GameData.METERS_TO_PIXELS) +
                                (rectangle.getHeight() * unitScale / GameData.METERS_TO_PIXELS / 2);
            bodyDef.position.set(positionX, positionY);

            Body objectBody = world.createBody(bodyDef);

            // Create the objects fixture, aka shape and physical properties
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rectangle.getWidth() * unitScale / GameData.METERS_TO_PIXELS / 2,
                           rectangle.getHeight() * unitScale / GameData.METERS_TO_PIXELS / 2);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 0f;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = 0f;
            Fixture fixture = objectBody.createFixture(fixtureDef);

            shape.dispose();
        }
    }

    /**
     * Renders the map on the screen
     */
    public void renderMap(OrthographicCamera camera, Batch batch) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.begin();
        batch.draw(finishLineSprite, finishLineSprite.getX(), finishLineSprite.getY(), finishLineSprite.getOriginX(),
                finishLineSprite.getOriginY(),
                finishLineSprite.getWidth(), finishLineSprite.getHeight(), finishLineSprite.getScaleX(),
                finishLineSprite.getScaleY(), finishLineSprite.getRotation());

        batch.draw(startLineSprite, startLineSprite.getX(), startLineSprite.getY(), startLineSprite.getOriginX(),
                startLineSprite.getOriginY(),
                startLineSprite.getWidth(),startLineSprite.getHeight(),startLineSprite.getScaleX(),
                startLineSprite.getScaleY(),startLineSprite.getRotation());

        batch.end();
    }

    /**
     * Instantiates the lane array and spawns obstacles on each of the lanes
     * @param world World to spawn the obstacles in
     */
    public void createLanes(World world) {
        //MapLayer leftLayer = tiledMap.getLayers().get("CollisionLayerLeft");
        //MapLayer rightLayer = tiledMap.getLayers().get("Lane1");

        //lanes[0] = new Lane(mapHeight, leftLayer, rightLayer, 25);
        //lanes[0].constructBoundaries(unitScale);
        //lanes[0].spawnObstacles(world, mapHeight / GameData.PIXELS_TO_TILES);

        int nrObstacles = 30;

        MapLayer leftLayer = tiledMap.getLayers().get("CollisionLayerLeft");
        MapLayer rightLayer = tiledMap.getLayers().get("Lane1");
        lanes[0] = new Lane(mapHeight, leftLayer, rightLayer, nrObstacles, 0);
        lanes[0].constructBoundaries(unitScale);
        lanes[0].spawnObstacles(world, mapHeight / GameData.PIXELS_TO_TILES);

        for(int i = 1; i < GameData.numberOfBoats; i++) {
            leftLayer = tiledMap.getLayers().get("Lane" + i);
            if(i != GameData.numberOfBoats - 1) {
                rightLayer = tiledMap.getLayers().get("Lane" + (i + 1));
            }
            else {
                rightLayer = tiledMap.getLayers().get("CollisionLayerRight");
            }
            lanes[i] = new Lane(mapHeight, leftLayer, rightLayer, nrObstacles, i);
            lanes[i].constructBoundaries(unitScale);
            lanes[i].spawnObstacles(world, mapHeight / GameData.PIXELS_TO_TILES);
        }
    }

    /**
     * Creates the finish line at a fixed position
     * @param textureFile The texture oof the finish line
     */
    public void createFinishLine(String textureFile){
        // Create the texture and the sprite of the finish line
        finishLineTexture = new Texture(textureFile);
        finishLineSprite = new Sprite(finishLineTexture);

        // Find out where it's going to start at, and how wide it will be, based on the limits of the edge lanes
        float startpoint = lanes[0].getLimitsAt(9000f)[0];
        float width = lanes[3].getLimitsAt(9000f)[1] - startpoint;

        // Set it's new found position and width
        finishLineSprite.setPosition(startpoint, 9000f);
        finishLineSprite.setSize(width, 100);
    }

    public void createStartLine(String textureFile){
        // Create the texture and the sprite of the finish line
        startLineTexture = new Texture(textureFile);
        startLineSprite = new Sprite(startLineTexture);

        // Find out where it's going to start at, and how wide it will be, based on the limits of the edge lanes
        float startpoint = lanes[0].getLimitsAt(350f)[0];
        float width = lanes[3].getLimitsAt(350f)[1] - startpoint;

        // Set it's new found position and width
        startLineSprite.setPosition(startpoint, 350f);
        startLineSprite.setSize(width, 100);
    }

    public static class MapSerializer implements JsonSerializer<Map> {
        public JsonElement serialize(Map aMap, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            for(int i = 0; i < aMap.getLanes().length; i++) {
                int obstLength = aMap.getLanes()[i].getObstacles().length;
                JsonArray obstacles = new JsonArray(obstLength);
                for(Obstacle o : aMap.getLanes()[i].getObstacles()) {
                    JsonObject obstacle = new JsonObject();
                    obstacle.add("x_position",new JsonPrimitive(o.getObstacleSprite().getX()));
                    obstacle.add("y_position",new JsonPrimitive(o.getObstacleSprite().getY()));
                    obstacle.add("obstacle_type",new JsonPrimitive(o.getObstacleType()));

                    obstacles.add(obstacle);
                }
                obj.add("lane_" + i, obstacles);
            }
            return obj;
        }
    }

    //getters
    public TiledMap getTiledMap(){
        return this.tiledMap;
    }

    public TiledMapRenderer getTiledMapRenderer(){
        return this.tiledMapRenderer;
    }

    public float getScreenWidth(){
        return this.screenWidth;
    }

    public int getMapWidth(){
        return this.mapWidth;
    }

    public int getMapHeight(){
        return this.mapHeight;
    }

    public float getUnitScale(){
        return this.unitScale;
    }

    public Lane[] getLanes(){
        return this.lanes;
    }

    public Texture getFinishLineTexture(){
        return this.finishLineTexture;
    }

    public Sprite getFinishLineSprite(){
        return this.finishLineSprite;
    }

    public Sprite getStartLineSprite() { return this.startLineSprite; }
}
