package com.github.badoualy.badoualyve.ui.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.WantedGame;

import static com.github.badoualy.badoualyve.ui.WantedGame.gdxUtils;


public class FightButton extends Group {

    private ImageButton btFight;
    private BitmapFont font;
    private Sound startSound;

    private String playerName;

    public FightButton(String playerName) {
        this.playerName = playerName;

        btFight = new ImageButton(new TextureRegionDrawable(new TextureRegion(gdxUtils().getTexture(AssetsUtils.BG_BT_DUEL))));
        font = WantedGame.gdxUtils().getDefaultFont();

        setSize(btFight.getWidth(), btFight.getHeight());
        addActor(btFight);

        startSound = Gdx.audio.newSound(Gdx.files.internal(AssetsUtils.SOUND_START));

        btFight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startSound.play();
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        font.draw(batch, playerName, getX() + 10, getY() + getHeight() / 2 + 10);
        font.draw(batch, "Click here to fight", getWidth() - 132, getY() + getHeight() / 2 + 10);
    }
}
