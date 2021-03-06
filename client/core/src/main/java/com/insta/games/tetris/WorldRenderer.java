package com.insta.games.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.insta.games.tetris.objects.Tetromino;

/**
 * Created by Julien on 4/2/16.
 */
public class WorldRenderer implements Disposable {

    private final float gameWidth;
    private final float gameHeight;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    private GameWorld gameWorld;
    private ShapeRenderer shapeRenderer;

    public static final int FIELD_MARGIN_LEFT = 1;
    public static final int FIELD_MARGIN_TOP = 25;

    public static final int SCORE_MARGIN_LEFT = 5;
    public static final int SCORE_MARGIN_TOP = 5;

    public static final int BLOCK_WIDTH = 30;
    public static final int CONTROL_WIDTH = 100;
    private BitmapFont gameOverFont;

    private static final String GAME_OVER = "GAME OVER";
    private static final String SCORE = "Score";
    private static final String LEVEL = "Level";
    private FrameBuffer frameBuffer;
    private SpriteBatch fbBatch;
    private int playfieldWidth;
    private int playfieldHeight;
    private int playfieldCenterX;
    private int playfieldCenterY;
    private int marginRightCenterX;
    private int marginBottomCenterY;
    private BitmapFont scoreFont;
    private BitmapFont levelFont;
    public int playX;
    public int playY;
    private GlyphLayout gameOverGlyphLayout;
    private GlyphLayout scoreGlyphLayout;
    private Vector3 worldVector;
    private Vector3 screenVector;
    public float controlScreenWidth;
    public float controlScreenHeight;
    public float playScreenX;
    public float playScreenY;
    public float leftArrowScreenX;
    public float leftArrowScreenY;
    public float rightArrowScreenX;
    public float rightArrowScreenY;
    public float downArrowScreenX;
    public float downArrowScreenY;
    public float rotateArrowScreenX;
    public float rotateArrowScreenY;

    public WorldRenderer(GameWorld gameWorld, WorldController worldController, float gameWidth, float gameHeight) {
        this.gameWorld = gameWorld;
        this.worldController = worldController;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        init();
    }

    private void init() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, gameWidth, gameHeight);

        worldVector = new Vector3();
        screenVector = new Vector3();

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        Assets.instance.init(new AssetManager());

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("tetris/zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.flip = true;
        gameOverFont = generator.generateFont(parameter);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("tetris/kenvector_future.ttf"));
        parameter.size = 24;
        parameter.flip = true;
        scoreFont = generator.generateFont(parameter);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("tetris/kenvector_future.ttf"));
        parameter.size = 10;
        parameter.flip = true;
        levelFont = generator.generateFont(parameter);

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) gameWidth, (int) gameHeight, false);
        fbBatch = new SpriteBatch();

        playfieldWidth = BLOCK_WIDTH * 10;
        playfieldHeight = BLOCK_WIDTH * 20;

        playfieldCenterX = FIELD_MARGIN_LEFT + playfieldWidth / 2;
        playfieldCenterY = FIELD_MARGIN_TOP + playfieldHeight / 2;

        marginRightCenterX = (int) (FIELD_MARGIN_LEFT + playfieldWidth + ((gameWidth - playfieldWidth - FIELD_MARGIN_LEFT) / 2));
        marginBottomCenterY = (int) (gameHeight - ((gameHeight - (FIELD_MARGIN_TOP + playfieldHeight)) / 2));

        playX = (int) (gameWidth - (gameWidth / 4) - (CONTROL_WIDTH / 2));
        playY = marginBottomCenterY - (CONTROL_WIDTH);

        gameOverGlyphLayout = new GlyphLayout();
        scoreGlyphLayout = new GlyphLayout();
    }

    public void render() {
        renderWorld();
        camera.update();
    }

    private synchronized void renderWorld() {

        // playfield
        frameBuffer.begin();

        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.setColor(0 / 255f, 0 / 255f, 0 / 255f, 1f);
        shapeRenderer.rect(FIELD_MARGIN_LEFT, FIELD_MARGIN_TOP, playfieldWidth, playfieldHeight, Color.BLACK, Color.BLACK,Color.BLACK,Color.BLACK);
        shapeRenderer.end();

        batch.begin();

        if (worldController.gameState == WorldController.GameState.Login){
            batch.draw(new Texture(Gdx.files.internal("tetris/images/main_screen.png")), 0, 0, gameWidth, gameHeight);
            renderNextTetromino();
        }
        else if(worldController.gameState == WorldController.GameState.Running){
            batch.draw(new Texture(Gdx.files.internal("tetris/images/game_screen.png")), 0, 0, gameWidth, gameHeight);
            renderNextTetromino();
        }
        else if (worldController.gameState == WorldController.GameState.GameOver){
            gameOverGlyphLayout.setText(gameOverFont, GAME_OVER);
            float textWidth = gameOverGlyphLayout.width;
            float textHeight = gameOverGlyphLayout.height;
            float textX = playfieldCenterX - textWidth / 2;
            float textY = playfieldCenterY - textHeight / 2;
            gameOverFont.setColor(Color.BLACK);
            gameOverFont.draw(batch, GAME_OVER, textX + 2, textY - 2);
            gameOverFont.setColor(Color.RED);
            gameOverFont.draw(batch, GAME_OVER, textX, textY);
        }
        if(worldController.gameState == WorldController.GameState.Running || worldController.gameState == WorldController.GameState.GameOver){
            renderPlayfield();
            renderScore();
        }

        batch.end();
        frameBuffer.end();
        fbBatch.begin();
        fbBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
        fbBatch.end();

        if (worldController.windowStage != null) {
            worldController.windowStage.draw();
        }
    }

    // Rendu du block du score et du level
    private void renderScore() {
        scoreGlyphLayout.setText(scoreFont, SCORE);
        float textHeight = scoreGlyphLayout.height;
        float textX = SCORE_MARGIN_LEFT;
        float textY = SCORE_MARGIN_TOP;
        scoreFont.setColor(Color.WHITE);
        scoreFont.draw(batch, SCORE, textX, textY);

        String score = String.valueOf(gameWorld.score);
        scoreGlyphLayout.setText(scoreFont, score);
        if (gameWorld.score < 10)
            textX = FIELD_MARGIN_LEFT + playfieldWidth - 15;
        else  if (gameWorld.score < 100)
            textX = FIELD_MARGIN_LEFT + playfieldWidth - 41;
        else  if (gameWorld.score < 1000)
            textX = FIELD_MARGIN_LEFT + playfieldWidth - 50;
        else  if (gameWorld.score < 10000)
            textX = FIELD_MARGIN_LEFT + playfieldWidth - 70;
        else  if (gameWorld.score < 100000)
            textX = FIELD_MARGIN_LEFT + playfieldWidth - 90;
        scoreFont.draw(batch, score, textX, textY);

        scoreGlyphLayout.setText(levelFont, LEVEL);
        textX = playfieldWidth + 8;
        textY += textHeight * 10f;
        levelFont.draw(batch, LEVEL, textX, textY);

        String level = String.valueOf(gameWorld.level);
        scoreGlyphLayout.setText(levelFont, level);
        textX += 15;
        textY += 15;
        levelFont.draw(batch, level, textX, textY);
    }

    // Rendu du block de la prochaine piece
    private void renderNextTetromino() {
        if (worldController.nextTetromino != null && worldController.nextTetromino.type != 0) {
            for (int i = 0; i < worldController.nextTetromino.grid.length; i++) {
                for (int j = 0; j < worldController.nextTetromino.grid[0].length; j++) {
                    if (worldController.nextTetromino.grid[i][j]) {
                        int x, y;
                        if (worldController.nextTetromino.type == 2){
                            x = playfieldWidth - 18 + ((worldController.nextTetromino.grid[0].length / 2) * BLOCK_WIDTH/2) + (j * BLOCK_WIDTH/2);
                            y = FIELD_MARGIN_TOP + 25 + (i * BLOCK_WIDTH/2);
                        }
                        else{
                            x = playfieldWidth - 10 + ((worldController.nextTetromino.grid[0].length / 2) * BLOCK_WIDTH/2) + (j * BLOCK_WIDTH/2);
                            y = FIELD_MARGIN_TOP + 40 + (i * BLOCK_WIDTH/2);
                        }
                        drawBlock(worldController.nextTetromino.type, x, y, true);
                    }
                }
            }
        }
    }

    // Rendu de la partie jouable
    public synchronized void renderPlayfield() {
        for (int i = 2; i < gameWorld.playfield.length; i++) {
            for (int j = 0; j < gameWorld.playfield[0].length; j++) {
                if (gameWorld.playfield[i][j] > 0) {
                    int x = WorldRenderer.FIELD_MARGIN_LEFT + j * WorldRenderer.BLOCK_WIDTH;
                    int y = WorldRenderer.FIELD_MARGIN_TOP + (i - 2) * WorldRenderer.BLOCK_WIDTH;
                    drawBlock(gameWorld.playfield[i][j], x, y);
                }
            }
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")

    private void drawBlock(int type, int x, int y) {
        drawBlock(type, x ,y, false);
    }

    private void drawBlock(int type, int x, int y, boolean next) {
        switch (type) {
            case Tetromino.I: batch.draw(Assets.instance.tetromino.elementCyanSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
            case Tetromino.O:
                batch.draw(Assets.instance.tetromino.elementYellowSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
            case Tetromino.T:
                batch.draw(Assets.instance.tetromino.elementPurpleSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
            case Tetromino.S:
                batch.draw(Assets.instance.tetromino.elementGreenSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
            case Tetromino.Z:
                batch.draw(Assets.instance.tetromino.elementRedSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
            case Tetromino.J:
                batch.draw(Assets.instance.tetromino.elementBlueSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
            case Tetromino.L:
                batch.draw(Assets.instance.tetromino.elementOrangeSquare, x, y, !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 , !next ? WorldRenderer.BLOCK_WIDTH : WorldRenderer.BLOCK_WIDTH/2 );
                break;
        }
    }

    private float toScreenX(int worldX) {
        worldVector.x = worldX;
        worldVector.y = 0;
        worldVector.z = 0;
        screenVector = camera.project(worldVector);
        return screenVector.x;
    }

    private float toScreenY(int worldY) {
        worldVector.x = 0;
        worldVector.y = gameHeight - worldY;
        worldVector.z = 0;
        screenVector = camera.project(worldVector);
        return screenVector.y;
    }

    public void resize(int width, int height) {
        controlScreenWidth = toScreenX(CONTROL_WIDTH);
        controlScreenHeight = toScreenY(CONTROL_WIDTH);
        playScreenX = toScreenX(playX);
        playScreenY = toScreenY(playY);
    }

    @Override
    public void dispose() {
        if (null != frameBuffer)
            frameBuffer.dispose();
        if (null != batch)
            batch.dispose();
        if (null != fbBatch)
            fbBatch.dispose();

    }
}
