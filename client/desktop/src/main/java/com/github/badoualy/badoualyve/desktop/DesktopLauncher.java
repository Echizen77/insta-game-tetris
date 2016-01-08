package com.github.badoualy.badoualyve.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.badoualy.badoualyve.BadOuAlyveGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "WANTED: Bad-ou-Alyve";
        new LwjglApplication(new BadOuAlyveGame(), config);
    }
}
