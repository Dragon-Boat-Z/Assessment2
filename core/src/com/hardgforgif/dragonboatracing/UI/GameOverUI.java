package com.hardgforgif.dragonboatracing.UI;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.core.Player;


public class GameOverUI extends UI{
    private Texture gameOverTexture;
    private Sprite gameOverSprite;
    private Texture victoryTexture;
    private Sprite victorySprite;
    private Texture firstPlaceTexture;
    private Sprite firstPlaceSprite;
    private Texture secondPlaceTexture;
    private Sprite secondPlaceSprite;
    private Texture thirdPlaceTexture;
    private Sprite thirdPlaceSprite;

    private ScrollingBackground scrollingBackground = new ScrollingBackground();


    public GameOverUI(){
        scrollingBackground.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        scrollingBackground.setSpeedFixed(true);
        scrollingBackground.setSpeed(ScrollingBackground.getDefaultSpeed());

        gameOverTexture = new Texture(Gdx.files.internal("gameOver.png"));
        victoryTexture = new Texture(Gdx.files.internal("victory.png"));
        firstPlaceTexture = new Texture(Gdx.files.internal("1stPlace.png"));
        secondPlaceTexture = new Texture(Gdx.files.internal("2ndPlace.png"));
        thirdPlaceTexture = new Texture(Gdx.files.internal("3rdPlace.png"));

        gameOverSprite = new Sprite(gameOverTexture);
        victorySprite = new Sprite(victoryTexture);
        firstPlaceSprite = new Sprite(firstPlaceTexture);
        secondPlaceSprite = new Sprite(secondPlaceTexture);
        thirdPlaceSprite = new Sprite(thirdPlaceTexture);

        int gameOverX = 400;
        int gameOverY = 200;
        gameOverSprite.setPosition(gameOverX, gameOverY);
        gameOverSprite.setSize(500, 500);

        victorySprite.setPosition(400, 200);
        victorySprite.setSize(500, 500);

        int placementX = 400;
        int placementY = 200;
        firstPlaceSprite.setPosition(placementX, placementY);
        firstPlaceSprite.setSize(500, 500);
        secondPlaceSprite.setPosition(placementX, placementY);
        secondPlaceSprite.setSize(500, 500);
        thirdPlaceSprite.setPosition(placementX, placementY);
        thirdPlaceSprite.setSize(500, 500);

    }

    @Override
    public void drawUI(Batch batch, Vector2 mousePos, float screenWidth, float delta) {
        batch.begin();
        scrollingBackground.updateAndRender(delta, batch);
        // If this was the last leg and the player won, show the victory screen
        if (GameData.currentLeg == 2 && GameData.standings[0] == 1)
            firstPlaceSprite.draw(batch);
        else if(GameData.currentLeg == 2 && GameData.standings[0] == 2) {
            secondPlaceSprite.draw(batch);
        }
        else if(GameData.currentLeg == 2 && GameData.standings[0] == 3) {
            thirdPlaceSprite.draw(batch);
        }
        // Otherwise, the game is over with a loss
        else
            gameOverSprite.draw(batch);
        batch.end();
        playMusic();
    }

    @Override
    public void drawPlayerUI(Batch batch, Player playerBoat) {

    }

    @Override
    public void getInput(float screenWidth, Vector2 mousePos) {
        // When the user clicks on the screen
        if(mousePos.x != 0f && mousePos.y != 0f) {
            // Reset the game, after which the game will return to the main menu state
            GameData.GameOverState = false;
            GameData.resetGameState = true;

            // Switch the music to the main menu music
            GameData.music.stop();
            GameData.music = Gdx.audio.newMusic(Gdx.files.internal("Vibing.ogg"));
        }
    }

    //getters
    public Texture getGameOverTexture(){
        return this.gameOverTexture;
    }

    public Sprite getGameOverSprite(){
        return this.gameOverSprite;
    }

    public Texture getVictoryTexture(){
        return this.victoryTexture;
    }

    public Sprite getVictorySprite(){
        return this.victorySprite;
    }

    public ScrollingBackground getScrollingBackground(){
        return this.scrollingBackground;
    }
}
