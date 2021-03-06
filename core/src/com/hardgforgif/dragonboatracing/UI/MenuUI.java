package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class MenuUI extends UI {

    //Sets the dimensions for all the UI components
    private static final int LOGO_WIDTH = 400;
    private static final int LOGO_HEIGHT = 200;
    private static final int LOGO_Y = 480;

    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_Y = 340;

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 65;

    private static final int LOAD_BUTTON_WIDTH = 250;
    private static final int LOAD_BUTTON_HEIGHT = 120;
    private static final int LOAD_BUTTON_Y = 203;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;
    Texture loadButtonActive;
    Texture loadButtonInactive;

    ScrollingBackground scrollingBackground;


    public MenuUI(){
        scrollingBackground = new ScrollingBackground();
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.getDefaultSpeed());

        playButtonActive = new Texture("PlaySelected.png");
        playButtonInactive = new Texture("PlayUnselected.png");
        exitButtonActive = new Texture("ExitSelected.png");
        exitButtonInactive = new Texture("ExitUnselected.png");
        loadButtonActive = new Texture("LoadSelected.png");
        loadButtonInactive = new Texture("LoadUnselected.png");
        logo = new Texture("Title.png");
    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        batch.draw(logo, screenWidth / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

        // If the mouse is not hovered over the buttons, draw the unselected buttons
        float x = screenWidth / 2 - PLAY_BUTTON_WIDTH / 2;
        if (
                mousePos.x < x + PLAY_BUTTON_WIDTH && mousePos.x > x &&
                        // cur pos < top_height
                        mousePos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                        mousePos.y > PLAY_BUTTON_Y
        ) {
            batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        // Otherwise draw the selected buttons
        x = screenWidth / 2 - EXIT_BUTTON_WIDTH / 2;
        if (
                mousePos.x < x + EXIT_BUTTON_WIDTH && mousePos.x > x &&
                        mousePos.y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                        mousePos.y > EXIT_BUTTON_Y
        ) {
            batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        } else {
            batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        x = screenWidth / 2 - LOAD_BUTTON_WIDTH / 2;
        if (
                mousePos.x < x + LOAD_BUTTON_WIDTH && mousePos.x > x &&
                        mousePos.y < LOAD_BUTTON_Y + LOAD_BUTTON_HEIGHT &&
                        mousePos.y > LOAD_BUTTON_Y
        ) {
            batch.draw(loadButtonActive, x, LOAD_BUTTON_Y, LOAD_BUTTON_WIDTH, LOAD_BUTTON_HEIGHT);
        } else {
            batch.draw(loadButtonInactive, x, LOAD_BUTTON_Y, LOAD_BUTTON_WIDTH, LOAD_BUTTON_HEIGHT);
        }

        batch.end();

        playMusic();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 clickPos) {
        // If the play button is clicked
        float x = screenWidth / 2 - PLAY_BUTTON_WIDTH / 2;
        if (
                clickPos.x < x + PLAY_BUTTON_WIDTH && clickPos.x > x &&
                        // cur pos < top_height
                        clickPos.y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                        clickPos.y > PLAY_BUTTON_Y
        ) {
            // Switch to the choosing state
            GameData.mainMenuState = false;
            GameData.choosingBoatState = true;
            GameData.currentUI = new ChoosingUI();
        }

        // If the exit button is clicked, close the game
        x = screenWidth / 2 - EXIT_BUTTON_WIDTH / 2;
        if (clickPos.x < x + EXIT_BUTTON_WIDTH && clickPos.x > x &&
                clickPos.y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                clickPos.y > EXIT_BUTTON_Y
        ) {
            Gdx.app.exit();
        }

        // If the load button is clicked
        x = screenWidth / 2 - LOAD_BUTTON_WIDTH / 2;
        if (clickPos.x < x + LOAD_BUTTON_WIDTH && clickPos.x > x &&
                clickPos.y < LOAD_BUTTON_Y + LOAD_BUTTON_HEIGHT &&
                clickPos.y > LOAD_BUTTON_Y
        ) {
            // Load a save
            GameData.saveState = true;
            GameData.loadState = true;
            GameData.mainMenuState = false;
            GameData.currentUI = new SaveUI();
        }
    }

    //getters
    public Texture getPlayButtonActive(){
        return this.playButtonActive;
    }

    public Texture getPlayButtonInactive(){
        return this.playButtonInactive;
    }

    public Texture getExitButtonActive(){
        return this.exitButtonActive;
    }

    public Texture getExitButtonInactive(){
        return this.exitButtonInactive;
    }

    public Texture getLogo(){
        return this.logo;
    }

    public ScrollingBackground getScrollingBackground(){
        return this.scrollingBackground;
    }
}
