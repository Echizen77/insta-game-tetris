package com.github.badoualy.badoualyve.ui.stage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.actor.FightButton;
import com.github.badoualy.badoualyve.ui.actor.StatDialogActor;

import static com.github.badoualy.badoualyve.ui.WantedGame.*;

public class HomeStage extends Stage {

    // References to super attributes
    private OrthographicCamera cam;
    private Batch batch;

    // Actors
    private Image background;
    private StatDialogActor dialogStat;
    private FightButton btFight;

    public HomeStage() {
        initViewport();
        initActors();
    }

    private void initViewport() {
        getViewport().setWorldSize(V_WIDTH, V_HEIGHT);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT); // false for yDown => (0, 0) is bottom-left corner
        getViewport().setCamera(cam); // Set stage default camera

        batch = getBatch();
    }

    private void initActors() {
        background = gdxUtils().createImageFromTexture(AssetsUtils.BG_HOME);

        // Create a dialog in the upper-right corner
        dialogStat = new StatDialogActor(player());
        dialogStat.setPosition(WIDTH - dialogStat.getWidth(), HEIGHT - dialogStat.getHeight());
        dialogStat.setTitle(player().getName());

        // Our match-making button
        btFight = new FightButton(player().getName());
        btFight.setPosition(10, getHeight() - btFight.getHeight() - 10);

        // The order matters! It defines the order of acting/drawing
        addActor(background);
        addActor(dialogStat);
        addActor(btFight);
    }

    @Override
    public void draw() {
        // If you need to do some operations, like update the camera position, do it here

        // Important, will draw all your actors
        super.draw();
    }

    @Override
    public void act(float delta) {
        // Compute, update values from your world, etc...
        // Be careful whether you act before or after your actors (thus the super call)

        // This will call Actor#act for all attached actors
        super.act(delta);
    }
}
