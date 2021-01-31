package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class SaveUI extends UI {

    private static final int DEFAULT_BUTTON_WIDTH = 260;
    private static final int DEFAULT_BUTTON_HEIGHT = 146;

    private static final int BACK_BUTTON_WIDTH = DEFAULT_BUTTON_WIDTH;
    private static final int BACK_BUTTON_HEIGHT = DEFAULT_BUTTON_HEIGHT;
    private static final int BACK_BUTTON_Y = 90;

    private static final int SLOT_BUTTON_WIDTH = DEFAULT_BUTTON_WIDTH - 50;
    private static final int SLOT_BUTTON_HEIGHT = DEFAULT_BUTTON_HEIGHT - 50;
    private static final int SLOT_1_Y = 520;
    private static final int SLOT_2_Y = 400;
    private static final int SLOT_3_Y = 280;


    Texture backButtonActive, backButtonInactive, slot1Active, slot1Inactive, slot2Active, slot2Inactive, slot3Active, slot3Inactive;
    ScrollingBackground scrollingBackground;

    public SaveUI() {
        backButtonActive = new Texture("BackSelected.png");
        backButtonInactive = new Texture("BackUnselected.png");
        slot1Active = new Texture("Slot1Selected.png");
        slot1Inactive = new Texture("Slot1Unselected.png");
        slot2Active = new Texture("Slot2Selected.png");
        slot2Inactive = new Texture("Slot2Unselected.png");
        slot3Active = new Texture("Slot3Selected.png");
        slot3Inactive = new Texture("Slot3Unselected.png");

        scrollingBackground = new ScrollingBackground();
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.getDefaultSpeed()/3);
    }
    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        // If the mouse is not hovered over the buttons, draw the unselected buttons:
        // Back button
        float x = screenWidth / 2 - BACK_BUTTON_WIDTH / 2;
        if (
                mousePos.x < x + BACK_BUTTON_WIDTH && mousePos.x > x &&
                        mousePos.y < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT &&
                        mousePos.y > BACK_BUTTON_Y
        ) {
            batch.draw(backButtonActive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        } else {
            batch.draw(backButtonInactive, x, BACK_BUTTON_Y, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        }
        // Save slots
        x = screenWidth / 2 - SLOT_BUTTON_WIDTH / 2;
        // First draw all inactive buttons
        batch.draw(slot1Inactive, x, SLOT_1_Y, SLOT_BUTTON_WIDTH, SLOT_BUTTON_HEIGHT);
        batch.draw(slot2Inactive, x, SLOT_2_Y, SLOT_BUTTON_WIDTH, SLOT_BUTTON_HEIGHT);
        batch.draw(slot3Inactive, x, SLOT_3_Y, SLOT_BUTTON_WIDTH, SLOT_BUTTON_HEIGHT);
        // Check if any buttons need to be redrawn highlighted (inefficient but cleaner code)
        if (mousePos.x < x + SLOT_BUTTON_WIDTH && mousePos.x > x) {
            // Slot 1
            if(mousePos.y < SLOT_1_Y + SLOT_BUTTON_HEIGHT && mousePos.y > SLOT_1_Y) {
                batch.draw(slot1Active, x, SLOT_1_Y, SLOT_BUTTON_WIDTH, SLOT_BUTTON_HEIGHT);
            }

            // Slot 2
            if(mousePos.y < SLOT_2_Y + SLOT_BUTTON_HEIGHT && mousePos.y > SLOT_2_Y){
                batch.draw(slot2Active, x, SLOT_2_Y, SLOT_BUTTON_WIDTH, SLOT_BUTTON_HEIGHT);
            }

            // Slot 3
            if (mousePos.y < SLOT_3_Y + SLOT_BUTTON_HEIGHT && mousePos.y > SLOT_3_Y) {
                batch.draw(slot3Active, x, SLOT_3_Y, SLOT_BUTTON_WIDTH, SLOT_BUTTON_HEIGHT);
            }
        }
        batch.end();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 clickPos) {
        // If back button is clicked
        float x = screenWidth / 2 - BACK_BUTTON_WIDTH / 2;
        if(
                clickPos.x < x + BACK_BUTTON_WIDTH && clickPos.x > x &&
                        clickPos.y < BACK_BUTTON_Y + BACK_BUTTON_HEIGHT &&
                        clickPos.y  > BACK_BUTTON_Y
        ) {
            // Go back to pause state
            GameData.saveState = false;
            GameData.pauseState = true;
            GameData.currentUI = new PauseUI();
        }

        if (clickPos.x < x + SLOT_BUTTON_WIDTH && clickPos.x > x) {
            // Slot 1
            if(clickPos.y < SLOT_1_Y + SLOT_BUTTON_HEIGHT && clickPos.y > SLOT_1_Y) {
                // Save in slot 1
            }

            // Slot 2
            if(clickPos.y < SLOT_2_Y + SLOT_BUTTON_HEIGHT && clickPos.y > SLOT_2_Y){
                // Save in slot 2
            }

            // Slot 3
            if (clickPos.y < SLOT_3_Y + SLOT_BUTTON_HEIGHT && clickPos.y > SLOT_3_Y) {
                // Save in slot 3
            }
        }
    }

    @Override
    public void playMusic() {
        super.playMusic();
    }
}
