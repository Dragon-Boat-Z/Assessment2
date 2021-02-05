package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;

public class GamePlayUI extends UI{
    private BitmapFont positionLabel;
    private BitmapFont robustnessLabel;
    private BitmapFont staminaLabel;
    private BitmapFont timerLabel;
    private BitmapFont legLabel;
    private BitmapFont speedLabel;
    private BitmapFont laneWarning;
    private Texture stamina;
    private Texture robustness;
    private Texture speed;
    private Texture robustnessINV;
    private Sprite spBar;
    private Sprite rBar;
    private Sprite sBar;
    private Sprite riBar;

    public GamePlayUI() {
        positionLabel = new BitmapFont();
        positionLabel.getData().setScale(1.4f);
        positionLabel.setColor(Color.BLACK);

        robustnessLabel = new BitmapFont();
        staminaLabel = new BitmapFont();
        speedLabel = new BitmapFont();

        timerLabel = new BitmapFont();
        timerLabel.getData().setScale(1.4f);
        timerLabel.setColor(Color.BLACK);

        legLabel = new BitmapFont();
        legLabel.getData().setScale(1.4f);
        legLabel.setColor(Color.BLACK);

        laneWarning = new BitmapFont();
        laneWarning.getData().setScale(3.0f);
        laneWarning.setColor(Color.RED);

        stamina = new Texture(Gdx.files.internal("Stamina_bar.png"));
        robustness  = new Texture(Gdx.files.internal("Robustness_bar.png"));
        robustnessINV = new Texture(Gdx.files.internal("Robustness_INV_bar.png"));
        speed = new Texture(Gdx.files.internal("Speed_bar.png"));
        rBar = new Sprite(robustness);
        riBar = new Sprite(robustnessINV);
        sBar = new Sprite(stamina);
        spBar = new Sprite(speed);
        sBar.setPosition(10 ,120);
        rBar.setPosition(10,60);
        riBar.setPosition(10, 60);
        spBar.setPosition(10,180);

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {

    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {
        // Set the robustness and stamina bars size based on the player boat
        sBar.setSize(playerBoat.getStamina(), 30);
        rBar.setSize(playerBoat.getRobustness(),30);
        riBar.setSize(playerBoat.getRobustness(), 30);
        spBar.setSize(playerBoat.getCurrentSpeed(),30);

        batch.begin();
        // Draw the robustness and stamina bars
        if(playerBoat.isInvulnerable()) {
            //Invulnerable robustness bar.
            riBar.draw(batch);
        }
        else {
            //Normal robustness bar.
            rBar.draw(batch);
        }
        sBar.draw(batch);
        spBar.draw(batch);
        robustnessLabel.draw(batch, "Robustness", 10, 110);
        staminaLabel.draw(batch, "Stamina", 10,170);
        speedLabel.draw(batch, "Speed", 10, 230);

        // Draw the position label, the timer and the leg label
        positionLabel.draw(batch, GameData.standings[0] + "/7", 1225, 700);
        timerLabel.draw(batch, String.valueOf(Math.round(GameData.currentTimer * 10.0) / 10.0), 10, 700);
        legLabel.draw(batch, "Leg: " + (GameData.currentLeg + 1), 10, 650);

        // Draw the lane waning if needed
        if(GameData.playerWarning) {
            laneWarning.draw(batch, "Warning! Not in lane", 420,400);
        }
        batch.end();

        playMusic();
    }
    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {

    }

    //getters
    public BitmapFont getPositionLabel(){
        return this.positionLabel;
    }

    public BitmapFont getRobustnessLabel(){
        return this.robustnessLabel;
    }

    public BitmapFont getStaminaLabel(){
        return this.staminaLabel;
    }

    public BitmapFont getTimerLabel(){
        return this.timerLabel;
    }

    public BitmapFont getLegLabel(){
        return this.legLabel;
    }

    public Texture getStamina(){
        return this.stamina;
    }

    public Texture getRobustness(){
        return this.robustness;
    }

    public Sprite getRBar(){
        return this.rBar;
    }

    public Sprite getSBar(){
        return this.sBar;
    }
}
