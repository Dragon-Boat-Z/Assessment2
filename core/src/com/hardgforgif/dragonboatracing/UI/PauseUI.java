package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class PauseUI extends UI {

    private static final int DEFAULT_BUTTON_WIDTH = 260;
    private static final int DEFAULT_BUTTON_HEIGHT = 146;

    private static final int RESUME_BUTTON_WIDTH = DEFAULT_BUTTON_WIDTH;
    private static final int RESUME_BUTTON_HEIGHT = DEFAULT_BUTTON_HEIGHT;
    private static final int RESUME_BUTTON_Y = 500;

    private static final int EXIT_BUTTON_WIDTH = DEFAULT_BUTTON_WIDTH;
    private static final int EXIT_BUTTON_HEIGHT = DEFAULT_BUTTON_HEIGHT;
    private static final int EXIT_BUTTON_Y = 90;

    private static final int SAVE_BUTTON_WIDTH = DEFAULT_BUTTON_WIDTH;
    private static final int SAVE_BUTTON_HEIGHT = DEFAULT_BUTTON_HEIGHT;
    private static final int SAVE_BUTTON_Y = 295;

    Texture resumeButtonActive, resumeButtonInactive, exitButtonActive, exitButtonInactive, saveButtonActive, saveButtonInactive;

    ScrollingBackground scrollingBackground;

    public PauseUI() {
        resumeButtonActive = new Texture("ResumeSelected.png");
        resumeButtonInactive = new Texture("ResumeUnselected.png");
        exitButtonActive = new Texture("ExitSelected.png");
        exitButtonInactive = new Texture("ExitUnselected.png");
        saveButtonActive = new Texture("SaveSelected.png");
        saveButtonInactive = new Texture("SaveUnselected.png");
        scrollingBackground = new ScrollingBackground();
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setTargetSpeed(ScrollingBackground.getDefaultSpeed()/3);
    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        // If the mouse is not hovered over the buttons, draw the unselected buttons:
        // Resume button
        float x = screenWidth / 2 - RESUME_BUTTON_WIDTH / 2;
        if (
                mousePos.x < x + RESUME_BUTTON_WIDTH && mousePos.x > x &&
                        mousePos.y < RESUME_BUTTON_Y + RESUME_BUTTON_HEIGHT &&
                        mousePos.y > RESUME_BUTTON_Y
        ) {
            batch.draw(resumeButtonActive, x, RESUME_BUTTON_Y, RESUME_BUTTON_WIDTH, RESUME_BUTTON_HEIGHT);
        } else {
            batch.draw(resumeButtonInactive, x, RESUME_BUTTON_Y, RESUME_BUTTON_WIDTH, RESUME_BUTTON_HEIGHT);
        }
        // Exit button
        x = screenWidth / 2 - EXIT_BUTTON_WIDTH / 2;
        if(
                mousePos.x < x + EXIT_BUTTON_WIDTH && mousePos.x > x &&
                        mousePos.y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                        mousePos.y  > EXIT_BUTTON_Y
        ) {
            batch.draw(exitButtonActive,x, EXIT_BUTTON_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
        } else {
            batch.draw(exitButtonInactive,x, EXIT_BUTTON_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
        }
        // Save button
        x = screenWidth / 2 - SAVE_BUTTON_WIDTH / 2;
        if(
                mousePos.x < x + SAVE_BUTTON_WIDTH && mousePos.x > x &&
                        mousePos.y < SAVE_BUTTON_Y + SAVE_BUTTON_HEIGHT &&
                        mousePos.y  > SAVE_BUTTON_Y
        ) {
            batch.draw(saveButtonActive,x, SAVE_BUTTON_Y,SAVE_BUTTON_WIDTH,SAVE_BUTTON_HEIGHT);
        } else {
            batch.draw(saveButtonInactive,x, SAVE_BUTTON_Y,SAVE_BUTTON_WIDTH,SAVE_BUTTON_HEIGHT);
        }
        batch.end();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 clickPos) {
    // If the play button is clicked
        float x = screenWidth / 2 - RESUME_BUTTON_WIDTH / 2;
        if (
                clickPos.x < x + RESUME_BUTTON_WIDTH && clickPos.x > x &&
                        // cur pos < top_height
                        clickPos.y < RESUME_BUTTON_Y + RESUME_BUTTON_HEIGHT &&
                        clickPos.y > RESUME_BUTTON_Y
        ) {
            // Switch to the previous state
            GameData.pauseState = false;
            //GameData.currentUI = GameData.previousState;
            switch (GameData.previousState) {
                case "GamePlayUI":
                    GameData.currentUI = new GamePlayUI();
                    GameData.gamePlayState = true;
                    break;
                case "ChoosingUI":
                    GameData.currentUI = new ChoosingUI();
                    GameData.choosingBoatState = true;
                    break;
                case "ResultsUI":
                    GameData.currentUI = new ResultsUI();
                    GameData.showResultsState = true;
                    break;
                case "MenuUI":
                    GameData.currentUI = new MenuUI();
                    GameData.mainMenuState = true;
                    break;
                case "GameOverUI":
                    GameData.currentUI = new GameOverUI();
                    GameData.GameOverState = true;
                    break;
            }
        }
        // If exit button is clicked
        x = screenWidth / 2 - EXIT_BUTTON_WIDTH / 2;
        if(
                clickPos.x < x + EXIT_BUTTON_WIDTH && clickPos.x > x &&
                        clickPos.y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                        clickPos.y  > EXIT_BUTTON_Y
        ) {
            // Close the game
            Gdx.app.exit();
        }

        // If save button is clicked
        x = screenWidth / 2 - SAVE_BUTTON_WIDTH / 2;
        if(
                clickPos.x < x + SAVE_BUTTON_WIDTH && clickPos.x > x &&
                        clickPos.y < SAVE_BUTTON_Y + SAVE_BUTTON_HEIGHT &&
                        clickPos.y  > SAVE_BUTTON_Y
        ) {
            // Go to save state
            GameData.saveState = true;
            GameData.pauseState = false;
            GameData.currentUI = new SaveUI();
        }
    }

    @Override
    public void playMusic() {
        super.playMusic();
    }
}
