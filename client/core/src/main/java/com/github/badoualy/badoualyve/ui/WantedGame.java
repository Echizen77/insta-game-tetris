package com.github.badoualy.badoualyve.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.github.badoualy.badoualyve.model.Player;
import com.github.badoualy.badoualyve.ui.screen.FixedFpsScreen;
import com.github.badoualy.badoualyve.ui.stage.HomeStage;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WantedGame extends Game {

    public static final String TITLE = "WANTED: Bad-ou-Alyve";

    // Pixel-real size
    public static int WIDTH;
    public static int HEIGHT;

    // V_ for virtual, it's the camera size, not the real one
    public static int V_WIDTH = 1024; // Default value, need tweak on android
    public static final int V_HEIGHT = 720;

    private GdxUtils gdxUtils;
    private Player player;

    private FixedFpsScreen homeScreen;

    @Override
    public void create() {
        // We can't use only static method here, due to how android handles resources / static
        gdxUtils = new com.github.badoualy.badoualyve.ui.GdxUtils();
        Texture.setAssetManager(gdxUtils.assetManager);

        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        // Adjust width to keep good ratio on mobile screens
        V_WIDTH = (V_HEIGHT * WIDTH) / HEIGHT;

        gdxUtils.loadHomeAssets();
        player = new Player("Kirito", 10, 20, 30, 40, 100);

        displayHomeScreen();

        // TODO: remove this
        startDemo();
    }

    @Override
    public void dispose() {
        super.dispose();
        gdxUtils.assetManager.dispose();
    }

    private void displayHomeScreen() {
        gdxUtils.assetManager.finishLoading();

        homeScreen = new FixedFpsScreen(new HomeStage(), 30); // 30 is way more than enough for a home screen
        setScreen(homeScreen);
    }

    private void startDemo(){
        // TODO: this is just a demo
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                  .subscribeOn(Schedulers.computation())
                  .observeOn(Schedulers.computation())
                  .doOnNext(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          player.setPower(player.getPower() + 3);
                          player.setDef(player.getDef() + 2);
                          player.setSpeed(player.getSpeed() + 2);
                      }
                  }).subscribe();

        Observable.interval(500, TimeUnit.MILLISECONDS)
                  .subscribeOn(Schedulers.computation())
                  .observeOn(Schedulers.computation())
                  .doOnNext(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          player.setMagic(player.getMagic() + 1);
                          player.setStamina(player.getStamina() + 10);
                      }
                  }).subscribe();
    }

    public static GdxUtils gdxUtils() {
        return ((WantedGame) Gdx.app.getApplicationListener()).gdxUtils;
    }

    public static Player player() {
        return ((WantedGame) Gdx.app.getApplicationListener()).player;
    }
}
