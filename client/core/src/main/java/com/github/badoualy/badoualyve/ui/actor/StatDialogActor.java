package com.github.badoualy.badoualyve.ui.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.badoualy.badoualyve.model.Player;
import com.github.badoualy.badoualyve.ui.AssetsUtils;
import com.github.badoualy.badoualyve.ui.WantedGame;

import java.util.ArrayList;
import java.util.List;

import static com.github.badoualy.badoualyve.ui.WantedGame.gdxUtils;

public class StatDialogActor extends Group {

    private Player player;

    private Image background;
    private BitmapFont font, smallFont;

    private String title;
    private List<String> stats = new ArrayList<String>();

    private float titleWidth;

    public StatDialogActor(Player player) {
        this.player = player;
        background = new Image(gdxUtils().getTexture(AssetsUtils.BG_STATS));
        font = WantedGame.gdxUtils().getDefaultFont();
        smallFont = WantedGame.gdxUtils().getDefaultFontSmall();
        setSize(background.getWidth(), background.getHeight());

        // Compute title width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, player.getName());
        titleWidth = layout.width;

        addActor(background);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stats.clear();
        stats.add("Power: " + player.getPower());
        stats.add("Defense: " + player.getDef());
        stats.add("Magic: " + player.getMagic());
        stats.add("Speed: " + player.getSpeed());
        stats.add("Stamina: " + player.getStamina());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (title != null) {
            font.draw(batch, title, getX() + getWidth() / 2 - titleWidth / 2, getTop() - 25);
        }

        for (int i = 0; i < stats.size(); i++) {
            smallFont.draw(batch, stats.get(i), getX() + 70, getY() + 150 - (i * smallFont.getLineHeight()));
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

        // Compute title width
        GlyphLayout layout = new GlyphLayout(); // Don't do this every frame! Store it as member
        layout.setText(font, player.getName());
        titleWidth = layout.width;
    }
}
