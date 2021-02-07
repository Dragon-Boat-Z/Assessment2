package com.hardgforgif.dragonboatracing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.gson.*;
import com.hardgforgif.dragonboatracing.UI.MenuUI;
import com.hardgforgif.dragonboatracing.UI.UI;
import com.hardgforgif.dragonboatracing.core.*;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.net.HttpRequestBuilder.json;

public class GameData {
    // Create the game state variables
    public static boolean mainMenuState = true;
    public static boolean choosingBoatState = false;
    public static boolean gamePlayState = false;
    public static boolean showResultsState = false;
    public static boolean resetGameState = false;
    public static boolean GameOverState = false;
    public static boolean pauseState = false;
    public static boolean saveState = false;
    public static boolean loadState = false;

    // Create the game UI and the game music
    public static UI currentUI = new MenuUI();
    public static String previousState = "MenuUI";

    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("Vibing.ogg"));

    // Set the rations between the pixels, meters and tiles
    public final static float METERS_TO_PIXELS = 100f;
    public static float TILES_TO_METERS;
    public static float PIXELS_TO_TILES;
  
    //Number of legs
    public static int numberOfLegs = 3;

    // Create a list of possible boat stats
    // Ordered by: robustness, speed, acceleration, maneuverability
    public static float[][] boatsStats = new float[][] {
        {120, 110, 100, 80}, 
        {55, 115, 130, 60},                                  
        {90, 100, 100, 130},
        {65, 120, 90, 65},
        {100, 110, 100, 110},
        {150, 125, 90, 55},
        {90, 100, 120, 90}
    };

    // Store information about each lane's boat
    // Number of boats
    public static int numberOfBoats = 7;
    public static int numberOfFinalists = 4;

    public static Boat[] boats = new Boat[numberOfBoats];

    // Boat's starting location
    public static float[][] startingPoints = new float[][]{
        {1.3496367f, 4f},
        {3.072728f, 4f},
        {4.7367244f, 4f},
        {6.4000597f, 4f},
        {8.0634f, 4f},
        {9.72706f, 4f},
        {11.451699f, 4f}
    };

    // Boat's starting location
    public static float[][] generateStartingPoints(Map map) {
        float[][] startingPoints = new float[numberOfBoats][2];
        for(int i = 0; i < numberOfBoats; i++) {
            float[] limits = map.getLanes()[i].getLimitsAt(0);
            startingPoints[i][0] = (limits[0] + limits[1])/(2*METERS_TO_PIXELS);
            startingPoints[i][1] = 4f;
        }
        return startingPoints;
    }

    // Boat's type
    public static int[] boatTypes = new int[numberOfBoats];
    // Boat's standing
    public static int[] standings = new int[numberOfBoats];
    // Boat's penalties
    public static float[] penalties = new float[numberOfBoats];
    // Player warning
    public static boolean playerWarning = false;
    // Result of the boat as a Pair<lane number, result>
    public static List<Float[]> results = new ArrayList<>();

    // Current leg and the current timer in the leg
    public static int currentLeg = 0;
    public static float currentTimer = 0f;

    // Difficulty constants for the AI
    public static float[] difficulty = new float[]{0.92f, 0.97f, 1f};

    //Difficulty selected start.
    public static int difficultySelected = 0;

    public static boolean saveGame(int saveSlot, Map map) throws IOException {
        // Create gson builder for converting from Java objects to Json format.
        GsonBuilder gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setLenient().setPrettyPrinting();
        // Allocate custom serializers for Lane, Boat and Obstacle classes.
        gson.registerTypeAdapter(Lane.class, new Lane.LaneSerializer());
        gson.registerTypeAdapter(Boat.class, new Boat.BoatSerializer());
        gson.registerTypeAdapter(Map.class, new Map.MapSerializer());

        // Create Json object for GameData.
        JsonObject data = new JsonObject();
        data.add("leg_number",new JsonPrimitive(GameData.currentLeg));
        data.add("current_timer", new JsonPrimitive(GameData.currentTimer));
        data.add("difficulty", new JsonPrimitive(GameData.difficultySelected));

        // Create file handler with set file path in Assessment2/save_data.
        FileHandle fileHandle = Gdx.files.internal("save_data/save_state_" + saveSlot + ".json");
        String absolutePath = fileHandle.file().getAbsolutePath();
        FileWriter writer = new FileWriter(absolutePath);

        // Write all json objects to the file.
        writer.write("{\n\"boats\": " + gson.create().toJson(boats) + ",\n\"game_data\":" + gson.create().toJson(data) + ",\n\n\"map\": " + gson.create().toJson(map) + "\n}");
        writer.close();

        // Returns true if no IOException is thrown.
        return true;
    }

    public static boolean loadGame(int saveSlot) throws IOException {
        // Create file handler with set file path in Assessment2/save_data.
        FileHandle fileHandle = Gdx.files.internal("save_data/save_state_" + saveSlot + ".json");
        String absolutePath = fileHandle.file().getAbsolutePath();
        FileReader reader = new FileReader(absolutePath);
        JsonStreamParser streamParser = new JsonStreamParser(reader);

        // Create gson object and read save state
        Gson gson = new Gson();
        JsonObject load_state = gson.fromJson(streamParser.next(), JsonObject.class);

        // Separate state into boats, gamedata and objects
        JsonArray boatArray = load_state.get("boats").getAsJsonArray();
        JsonObject gamedata = load_state.get("game_data").getAsJsonObject();
        JsonObject objectArray = load_state.get("map").getAsJsonObject();

        currentTimer = gamedata.get("current_timer").getAsFloat();
        currentLeg = gamedata.get("leg_number").getAsInt();
        difficultySelected = gamedata.get("difficulty").getAsInt();

        // For each boat, initialise the boat and all obstacles in that boat's lane
        for(int i = 0; i < numberOfBoats; i++) {
            JsonObject o = boatArray.get(i).getAsJsonObject();
            if(i == 0) {
                Player p = Player.from_json(o, Game.getMap()[currentLeg], Game.getWorld()[currentLeg]);
                boats[i] = p;
            }
            else {
                AI a = AI.from_json(o, Game.getMap()[currentLeg], Game.getWorld()[currentLeg]);
                boats[i] = a;
            }
            boatTypes[i] = boats[i].getBoatType();
            JsonArray obsts = objectArray.get("lane_" + i).getAsJsonArray();
            Game.getMap()[currentLeg].getLanes()[i].spawnObstacles(Game.getWorld()[currentLeg], Game.getMap()[currentLeg].getMapHeight() / GameData.PIXELS_TO_TILES, obsts);
        }
        reader.close();
        return true;
    }

    //Testing.
}
