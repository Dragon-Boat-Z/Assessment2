package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.Game;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ChoosingUI extends UI{
    private static final int DEFAULT_BUTTON_WIDTH = 260;
    private static final int DEFAULT_BUTTON_HEIGHT = 150;

    private Texture background;
    private Sprite background_sprite;

    private Texture bar;
    private Sprite[] barSprites = new Sprite[4];
    private Texture boatTexture;
    private Sprite[] boatSprites = new Sprite[GameData.numberOfBoats];

    private BitmapFont label;

    private float[] currentStats = new float[4];

    private boolean selectedBoat = false;
    private boolean selectedDifficulty = false;

    ScrollingBackground scrollingBackground = new ScrollingBackground();

    private Texture diffEasySelected, diffNormalSelected, diffHardSelected, diffEasyUnselected,
            diffNormalUnselected, diffHardUnselected, backButtonSelected, backButtonUnselected;

    private static final int BACK_BUTTON_WIDTH = DEFAULT_BUTTON_WIDTH;
    private static final int BACK_BUTTON_HEIGHT = DEFAULT_BUTTON_HEIGHT;
    private static final int BACK_BUTTON_Y = 50;
    private static final int DIFFICULTY_WIDTH = DEFAULT_BUTTON_WIDTH;
    private static final int DIFFICULTY_HEIGHT = DEFAULT_BUTTON_HEIGHT;
    private static final int DIFFICULT_EASY_Y = 530;
    private static final int DIFFICULT_NORMAL_Y = 375;
    private static final int DIFFICULT_HARD_Y = 220;




    public ChoosingUI() {
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.getDefaultSpeed());

        background = new Texture(Gdx.files.internal("Background.png"));
        background_sprite = new Sprite(background);
        background_sprite.setPosition(200,410);
        background_sprite.setSize(900,260);


        label = new BitmapFont();
        label.getData().setScale(1.4f);
        label.setColor(Color.BLACK);


        bar = new Texture("Robustness_bar.png");
        barSprites[0] = new Sprite(bar);
        barSprites[0].setPosition(430,600);

        bar = new Texture("Speed_bar.png");
        barSprites[1] = new Sprite(bar);
        barSprites[1].setPosition(430,550);

        bar = new Texture("Acceleration_bar.png");
        barSprites[2] = new Sprite(bar);
        barSprites[2].setPosition(430,500);

        bar = new Texture("Maneuverability_bar.png");
        barSprites[3] = new Sprite(bar);
        barSprites[3].setPosition(430,450);

        for (int i = 1; i <= GameData.numberOfBoats; i++){
            boatTexture = new Texture("Boat" + i + ".png");
            boatSprites[i - 1] = new Sprite(boatTexture);
            boatSprites[i - 1].scale(-0.6f);
            //boatSprites[i - 1].setPosition(150 + 300f * (i - 1), -200f);
            boatSprites[i - 1].setPosition(150 + 150f * (i - 1), -100f);
        }

        diffEasySelected = new Texture("DifficultyEasySelected.png");
        diffEasyUnselected = new Texture("DifficultyEasyUnselected.png");
        diffNormalSelected = new Texture("DifficultyNormalSelected.png");
        diffNormalUnselected = new Texture("DifficultyNormalUnselected.png");
        diffHardSelected = new Texture("DifficultyHardSelected.png");
        diffHardUnselected = new Texture("DifficultyHardUnselected.png");

        backButtonSelected = new Texture("BackSelected.png");
        backButtonUnselected = new Texture("BackUnselected.png");
    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        //Check if we need to display boats or difficulty selection
        if(!selectedBoat) {
            //Check if the mouse is hovering over a boat, and update the bars displayed
            for (int i = 0; i < GameData.numberOfBoats; i++) {
                // Get the position of the boat
                float boatX = boatSprites[i].getX() + boatSprites[i].getWidth() / 2 -
                        boatSprites[i].getWidth() / 2 * boatSprites[i].getScaleX();
                float boatY = boatSprites[i].getY() + boatSprites[i].getHeight() / 2 -
                        boatSprites[i].getHeight() / 2 * boatSprites[i].getScaleY();
                float boatWidth = boatSprites[i].getWidth() * boatSprites[i].getScaleX();
                float boatHeight = boatSprites[i].getHeight() * boatSprites[i].getScaleY();

                // Check if the mouse is hovered over it
                if (mousePos.x > boatX && mousePos.x < boatX + boatWidth &&
                        mousePos.y > boatY && mousePos.y < boatY + boatHeight) {
                    //4 stats (robustness, etc.) not 4 boats (now 7).
                    currentStats[0] = GameData.boatsStats[i][0];
                    currentStats[1] = GameData.boatsStats[i][1];
                    currentStats[2] = GameData.boatsStats[i][2];
                    currentStats[3] = GameData.boatsStats[i][3];
                }
            }

            float full_bar = screenWidth / 3;
            batch.begin();
            scrollingBackground.updateAndRender(delta, batch);
            background_sprite.draw(batch);

            // Display the bars based on the selected boat
            for (int i = 0; i < 4; i++) {
                barSprites[i].setSize(full_bar * (currentStats[i] / 100), 30);
                barSprites[i].draw(batch);
            }

            label.draw(batch, "Robustness:", 260, 625);
            label.draw(batch, "Speed:", 260, 575);
            label.draw(batch, "Acceleration:", 260, 525);
            label.draw(batch, "Maneuverability:", 260, 475);


            // Display the boats
            for (int i = 0; i < GameData.numberOfBoats; i++) {
                batch.draw(boatSprites[i], boatSprites[i].getX(), boatSprites[i].getY(), boatSprites[i].getOriginX(),
                        boatSprites[i].getOriginY(),
                        boatSprites[i].getWidth(), boatSprites[i].getHeight(), boatSprites[i].getScaleX(),
                        boatSprites[i].getScaleY(), boatSprites[i].getRotation());
            }
            batch.end();

            playMusic();
        }

        else {
            batch.begin();
            scrollingBackground.updateAndRender(delta, batch);
            // If the mouse is not hovered over the buttons, draw the unselected buttons:
            // Back button
            float x = screenWidth / 2 - DEFAULT_BUTTON_WIDTH / 2;
            if (
                    mousePos.x < x + BACK_BUTTON_WIDTH && mousePos.x > x &&
                            mousePos.y < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT &&
                            mousePos.y > BACK_BUTTON_Y
            ) {
                batch.draw(backButtonSelected, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
            } else {
                batch.draw(backButtonUnselected, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
            }

            batch.draw(diffEasyUnselected, x, DIFFICULT_EASY_Y, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
            batch.draw(diffNormalUnselected, x, DIFFICULT_NORMAL_Y, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
            batch.draw(diffHardUnselected, x, DIFFICULT_HARD_Y, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
            // Check if any buttons need to be redrawn highlighted (inefficient but cleaner code)
            if (mousePos.x < x + DEFAULT_BUTTON_WIDTH && mousePos.x > x) {
                // Slot 1
                if(mousePos.y < DIFFICULT_EASY_Y + DEFAULT_BUTTON_HEIGHT && mousePos.y > DIFFICULT_EASY_Y) {
                    batch.draw(diffEasySelected, x, DIFFICULT_EASY_Y, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
                }

                // Slot 2
                if(mousePos.y < DIFFICULT_NORMAL_Y + DEFAULT_BUTTON_HEIGHT && mousePos.y > DIFFICULT_NORMAL_Y){
                    batch.draw(diffNormalSelected, x, DIFFICULT_NORMAL_Y, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
                }

                // Slot 3
                if (mousePos.y < DIFFICULT_HARD_Y + DIFFICULTY_HEIGHT && mousePos.y > DIFFICULT_HARD_Y) {
                    batch.draw(diffHardSelected, x, DIFFICULT_HARD_Y, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
                }
            }
            batch.end();
        }
    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        float x = screenWidth / 2 - BACK_BUTTON_WIDTH / 2;
        //Check if we need to display boats or difficulty selection
        if (!selectedBoat) {
            // Check which of the boat was pressed
            for (int i = 0; i < GameData.numberOfBoats; i++) {
                float boatX = boatSprites[i].getX() + boatSprites[i].getWidth() / 2 -
                        boatSprites[i].getWidth() / 2 * boatSprites[i].getScaleX();
                float boatY = boatSprites[i].getY() + boatSprites[i].getHeight() / 2 -
                        boatSprites[i].getHeight() / 2 * boatSprites[i].getScaleY();
                float boatWidth = boatSprites[i].getWidth() * boatSprites[i].getScaleX();
                float boatHeight = boatSprites[i].getHeight() * boatSprites[i].getScaleY();

                if (mousePos.x > boatX && mousePos.x < boatX + boatWidth &&
                        mousePos.y > boatY && mousePos.y < boatY + boatHeight) {
                    // Set the player's boat type based on the clicked boat
                    GameData.boatTypes[0] = i;

                    // Randomise the AI boats
                    //ArrayList<Integer> intList = new ArrayList<Integer>(){{add(0); add(1); add(2); add(3);}};
                    //intList.remove(new Integer(i));
                    //Collections.shuffle(intList);
                    //GameData.boatTypes[1] = intList.get(0);
                    //GameData.boatTypes[2] = intList.get(1);
                    //GameData.boatTypes[3] = intList.get(2);
                    ArrayList<Integer> intList = new ArrayList<Integer>();
                    for (int j = 0; j < GameData.numberOfBoats; j++) {
                        intList.add(j);
                    }
                    intList.remove(new Integer(i));
                    Collections.shuffle(intList);
                    for (int j = 1; j < GameData.numberOfBoats; j++) {
                        GameData.boatTypes[j] = intList.get(j - 1);
                    }
                    selectedBoat = true;
                }
            }
        } else {
            // Difficulty Buttons
            if (mousePos.x < x + DIFFICULTY_WIDTH && mousePos.x > x) {
                // Easy Difficulty
                if (mousePos.y < DIFFICULT_EASY_Y + DIFFICULTY_WIDTH && mousePos.y > DIFFICULT_EASY_Y) {
                    GameData.difficultySelected = 0;
                    selectedDifficulty = true;
                }
                // Normal Difficulty
                if (mousePos.y < DIFFICULT_NORMAL_Y + DIFFICULTY_WIDTH && mousePos.y > DIFFICULT_NORMAL_Y) {
                    GameData.difficultySelected = 1;
                    selectedDifficulty = true;
                }
                // Hard Difficulty
                if (mousePos.y < DIFFICULT_NORMAL_Y + DIFFICULTY_WIDTH && mousePos.y > DIFFICULT_NORMAL_Y) {
                    GameData.difficultySelected = 2;
                    selectedDifficulty = true;
                }
            }
            // Back Button
            if(mousePos.x < x + BACK_BUTTON_WIDTH && mousePos.x > x &&
                    mousePos.y < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT &&
                    mousePos.y  > BACK_BUTTON_Y) {
                GameData.boatTypes = new int[GameData.numberOfBoats];
                selectedBoat = false;
            }
        }


        // Set the game state to the game play state if the player has chosen the difficulty.
        if (selectedDifficulty) {
            // Change the music
            GameData.music.stop();
            GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Love_Drama.ogg"));

            GameData.choosingBoatState = false;
            GameData.gamePlayState = true;
            GameData.currentUI = new GamePlayUI();
        }
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {
    }

    //getters
    public Texture getBackground(){
        return this.background;
    }

    public Sprite getBackgroundSprite(){
        return this.background_sprite;
    }

    public Texture getBar(){
        return this.bar;
    }

    public Sprite[] getBarSprites(){
        return this.barSprites;
    }

    public Texture getBoatTexture(){
        return this.boatTexture;
    }

    public Sprite[] getBoatSprites(){
        return this.boatSprites;
    }

    public BitmapFont getLabel(){
        return this.label;
    }

    public float[] getCurrentStats(){
        return this.currentStats;
    }

    public ScrollingBackground getScrollingBackground(){
        return this.scrollingBackground;
    }
}
