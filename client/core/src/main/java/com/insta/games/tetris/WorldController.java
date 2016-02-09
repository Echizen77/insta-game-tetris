package com.insta.games.tetris;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Random;

import com.insta.games.tetris.objects.Tetromino;

/**
 * Created by Julien on 4/2/16.
 */
public class WorldController {

    private final TetrisGame game;
    private String TAG = WorldController.class.getName();

    private GameWorld gameWorld;
    public boolean tetrominoSpawned = false;
    private Tetromino tetromino;
    public GameState gameState;
    public Tetromino nextTetromino;
    private int levelRowsRemoved;
    public Stage windowStage;
    private Preferences prefs;


    public WorldController(TetrisGame game, GameWorld gameWorld) {
        this.game = game;
        this.gameWorld = gameWorld;
        init();
    }

    private void init() {
        //TODO:
        prefs = Gdx.app.getPreferences("Nitris");
        gameState = GameState.Login;
        tetromino = new Tetromino(gameWorld, this);
        nextTetromino = new Tetromino(gameWorld, this);
        levelRowsRemoved = 0;
        windowStage = new Stage();
    }


    public void update() {
        switch (gameState) {
            case Login:
                checkMenuControls();
                break;
            case Running:
                checkRows();
                boolean moved = false;
                if (!tetrominoSpawned) spawnTetromino();
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    tetromino.rotate();
                    moved = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    tetromino.move(-1, 0);
                    moved = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    tetromino.move(1, 0);
                    moved = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    //tetromino.move(0, 1);
                    tetromino.fall(true);
                    moved = true;
                }
                if (Gdx.input.justTouched() && game.worldRenderer != null) {
                    int gx = Gdx.input.getX();
                    int gy = Gdx.input.getY();
                    if (gx > game.worldRenderer.leftArrowScreenX &&
                            gx < game.worldRenderer.leftArrowScreenX + game.worldRenderer.controlScreenWidth &&
                            gy > game.worldRenderer.leftArrowScreenY &&
                            gy < game.worldRenderer.leftArrowScreenY +  game.worldRenderer.controlScreenWidth) {
                        tetromino.move(-1, 0);
                        moved = true;
                    } else if (gx > game.worldRenderer.rightArrowScreenX &&
                            gx < game.worldRenderer.rightArrowScreenX +  game.worldRenderer.controlScreenWidth &&
                            gy > game.worldRenderer.rightArrowScreenY &&
                            gy < game.worldRenderer.rightArrowScreenY +  game.worldRenderer.controlScreenWidth) {
                        tetromino.move(1, 0);
                        moved = true;
                    } else if (gx > game.worldRenderer.rotateArrowScreenX &&
                            gx < game.worldRenderer.rotateArrowScreenY +  game.worldRenderer.controlScreenWidth &&
                            gy > game.worldRenderer.rotateArrowScreenY &&
                            gy < game.worldRenderer.rotateArrowScreenY +  game.worldRenderer.controlScreenWidth) {
                        tetromino.rotate();
                        moved = true;
                    }
                }
                if ((Gdx.input.justTouched() || Gdx.input.isTouched()) && game.worldRenderer != null) {
                    int gx = Gdx.input.getX();
                    int gy = Gdx.input.getY();
                    if (gx > game.worldRenderer.downArrowScreenX &&
                            gx < game.worldRenderer.downArrowScreenX +  game.worldRenderer.controlScreenWidth &&
                            gy > game.worldRenderer.downArrowScreenY &&
                            gy < game.worldRenderer.downArrowScreenY + game.worldRenderer.controlScreenWidth) {
                        tetromino.fall(true);
                        moved = true;
                    }
                }
                if (!moved)
                    tetromino.fall(false);
                gameWorld.update(tetromino);
                break;
            case GameOver:
                checkMenuControls();
                break;
        }
        windowStage.act();
    }

    private void checkMenuControls() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched()) {
            int gx = Gdx.input.getX();
            int gy = Gdx.input.getY();
            if (gx > game.worldRenderer.playScreenX &&
                    gx < game.worldRenderer.playScreenX + game.worldRenderer.controlScreenWidth &&
                    gy > game.worldRenderer.playScreenY &&
                    gy < game.worldRenderer.playScreenY + game.worldRenderer.controlScreenWidth) {
                gameWorld.reset();
                gameState = GameState.Running;

                // Using swipe gesture to move the tetris block
                SimpleDirectionGestureDetector simpleDirectionGestureDetector = new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {
                    @Override
                    public void onLeft() {
                        tetromino.move(-1, 0);
                    }

                    @Override
                    public void onRight() {
                        tetromino.move(1, 0);
                    }

                    @Override
                    public void onUp() {

                    }

                    @Override
                    public void onDown() {
                        tetromino.fall(true);
                    }

                    @Override
                    public void onTap() {
                        tetromino.rotate();
                    }
                });

                Gdx.input.setInputProcessor(simpleDirectionGestureDetector);
            }

        }
    }

    private void checkRows() {
        int rowsRemoved = 0;
        if (null == tetromino || System.currentTimeMillis() - tetromino.lastFallTime >= tetromino.delay) {
            boolean checkAgain = false;
            for (int i = 0; i < gameWorld.blocks.length; i++) {
                if (checkAgain) {
                    i -= 1;
                }
                boolean full = true;
                for (int j = 0; j < gameWorld.blocks[0].length; j++) {
                    if (!gameWorld.blocks[i][j]) {
                        full = false;
                    }
                }
                if (full) {
                    removeRow(i);
                    rowsRemoved++;
                    levelRowsRemoved++;
                    checkAgain = true;
                } else {
                    checkAgain = false;
                }
            }
        }

        if (rowsRemoved > 0) {
            play(Assets.instance.sounds.rowCleared);
        }

        switch (rowsRemoved) {
            case 1:
                gameWorld.score += 40 * (gameWorld.level + 1);
                break;
            case 2:
                gameWorld.score += 100 * (gameWorld.level + 1);
                break;
            case 3:
                gameWorld.score += 300 * (gameWorld.level + 1);
                break;
            case 4:
                gameWorld.score += 1200 * (gameWorld.level + 1);
                break;
        }
        // Au bout de 10 lignes supprimé, on lvl up
        if (levelRowsRemoved >= 10) {
            gameWorld.level++;
            levelRowsRemoved = 0;
            play(Assets.instance.sounds.levelUp);
        }
    }

    private void removeRow(int row) {
        for (int j = 0; j < gameWorld.blocks[0].length; j++) {
            gameWorld.blocks[row][j] = false;
            gameWorld.playfield[row][j] = 0;
        }
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < gameWorld.blocks[0].length; j++) {
                if (gameWorld.blocks[i - 1][j]) {
                    gameWorld.blocks[i - 1][j] = false;
                    gameWorld.blocks[i][j] = true;
                    gameWorld.playfield[i][j] = gameWorld.playfield[i - 1][j];
                    gameWorld.playfield[i - 1][j] = 0;
                }
            }
        }
    }

    public void gameOver() {
        gameState = GameState.GameOver;
        play(Assets.instance.sounds.gameOver);
    }

    private void spawnTetromino() {
        if (System.currentTimeMillis() - tetromino.lastFallTime >= tetromino.delay) {
            if (nextTetromino.type == 0) {
                tetromino.init(randInt(1, 7));
            } else {
                tetromino.init(nextTetromino.type);
            }
            nextTetromino.init(randInt(1, 7));
            tetrominoSpawned = true;
        }
    }

    private void play(Sound sound) {
        if (prefs.getBoolean("sound", false)) {
            sound.play();
        }
    }

    private static Random rand = new Random();

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see Random#nextInt(int)
     */
    // Retourne une valeur aleatoire entre min et max
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public void dispose() {
        gameWorld.dispose();
        if (windowStage != null) {
            windowStage.dispose();
        }
    }

    enum GameState {
        Login, Running, GameOver //,Intro, Options
    }


    /**
     * User input controller
     * overide the methods to using swipe gesture left, right, up, down and tap
     */
    public static class SimpleDirectionGestureDetector extends GestureDetector {
        public interface DirectionListener {
            void onLeft();

            void onRight();

            void onUp();

            void onDown();

            void onTap();
        }

        public SimpleDirectionGestureDetector(DirectionListener directionListener) {
            super(new DirectionGestureListener(directionListener));
        }

        private static class DirectionGestureListener implements GestureListener{
            DirectionListener directionListener;

            public DirectionGestureListener(DirectionListener directionListener){
                this.directionListener = directionListener;
            }

            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                directionListener.onTap();
                return true;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if(Math.abs(velocityX)>Math.abs(velocityY)){
                    if(velocityX>0){
                        directionListener.onRight();
                    }else{
                        directionListener.onLeft();
                    }
                }else{
                    if(velocityY>0){
                        directionListener.onDown();
                    }else{
                        directionListener.onUp();
                    }
                }
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

        }

    }

}
