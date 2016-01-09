package com.github.badoualy.badoualyve.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public final class GdxUtils {

    /** Game's asset manager */
    public final AssetManager assetManager = new AssetManager();

    public GdxUtils() {

    }

    public void loadHomeAssets() {
        assetManager.load(AssetsUtils.BG_HOME, Texture.class);
        assetManager.load(AssetsUtils.BG_DIALOG, Texture.class);
        assetManager.load(AssetsUtils.BG_PANEL, Texture.class);
        assetManager.load(AssetsUtils.BG_STATS, Texture.class);
        assetManager.load(AssetsUtils.BG_BT_DUEL, Texture.class);
        assetManager.load(AssetsUtils.FONT, BitmapFont.class);
    }

    public Image createImageFromTexture(String name) {
        return new Image(getTexture(name));
    }

    /** Shortcut assetManager.get(name, Texture.class) */
    public Texture getTexture(String name) {
        return assetManager.get(name, Texture.class);
    }

    /** Shortcut assetManager.get(name, TextureAtlas.class) */
    public TextureAtlas getTextureAtlas(String name) {
        return assetManager.get(name, TextureAtlas.class);
    }

    /** Shortcut assetManager.get(name, BitmapFont.class) */
    public BitmapFont getFont(String name) {
        return assetManager.get(name, BitmapFont.class);
    }

    public BitmapFont getDefaultFont() {
        return getFont(AssetsUtils.FONT);
    }
}