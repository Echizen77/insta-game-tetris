package com.github.badoualy.badoualyve.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.github.badoualy.badoualyve.ui.WantedGame;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.hideStatusBar = false;
        config.useImmersiveMode = true;
        initialize(new WantedGame(), config);
    }
}
