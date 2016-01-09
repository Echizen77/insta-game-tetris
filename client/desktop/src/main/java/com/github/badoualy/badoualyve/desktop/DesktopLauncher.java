package com.github.badoualy.badoualyve.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.badoualy.badoualyve.ui.WantedGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = WantedGame.TITLE;
        config.width = WantedGame.V_WIDTH;
        config.height = WantedGame.V_HEIGHT;
        config.resizable = false;
        config.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new WantedGame(), config);
    }
}
