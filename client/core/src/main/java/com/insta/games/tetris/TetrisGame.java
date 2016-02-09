package com.insta.games.tetris;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class TetrisGame extends ApplicationAdapter {

    private com.insta.games.tetris.WorldController worldController;
    protected com.insta.games.tetris.WorldRenderer worldRenderer;

    protected final float gameWidth = 600f;
    private final float gameHeight = 640f;


    private boolean paused;
    protected int screenWidth;
    protected int screenHeight;

    @Override
    public void create() {

        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        GameWorld gameWorld = new GameWorld();

        worldController = new com.insta.games.tetris.WorldController(this, gameWorld);
        worldRenderer = new com.insta.games.tetris.WorldRenderer(gameWorld, worldController, gameWidth, gameHeight);

        paused = false;

    }

    @Override
    public synchronized void render() {
        super.render();

        if (!paused) {
            worldController.update();
        }

        worldRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        worldRenderer.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
        paused = true;
    }

    @Override
    public void resume() {
        super.resume();
        paused = false;
    }

    @Override
    public void dispose() {
        super.dispose();
        worldRenderer.dispose();
        worldController.dispose();
    }

}
