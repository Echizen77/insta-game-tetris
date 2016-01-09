package com.github.badoualy.badoualyve.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/** Generic screen with a stage that fixes FPS at which the stage is rendered */
public class FixedFpsScreen extends ScreenAdapter {

    protected final Stage stage;
    protected final float fps;

    private float accumulator;

    public FixedFpsScreen(final Stage stage, final int fps) {
        this.stage = stage;
        this.fps = 1.0f / fps;
        accumulator = fps;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Max frame time
        if (delta > 0.25f)
            delta = 0.25f;

        accumulator += delta;

        // We update simulation as much as needed
        while (accumulator >= fps) {
            stage.act(); // Update

            accumulator -= fps;
        }

        // We still draw once
        stage.draw(); // Draw
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}