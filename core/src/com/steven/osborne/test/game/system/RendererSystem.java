package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.steven.osborne.test.game.component.*;

import static com.steven.osborne.test.game.WorldsStrongestPacifist.PIXELS_TO_METERS;

public class RendererSystem extends EntitySystem {
    private static final float HORIZONTAL_BOUNDARY = 18f;
    private static final float VERTICAL_BOUNDARY = 32f;

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> explosions;
    private ImmutableArray<Entity> uiEntities;

    private ComponentMapper<SpriteComponent> textureComponentMapper = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<ExplosionComponent> explosionComponentMapper = ComponentMapper.getFor(ExplosionComponent.class);
    private ComponentMapper<UiComponent> uiComponentMapper = ComponentMapper.getFor(UiComponent.class);

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private OrthographicCamera guiCamera;
    private Vector3 backgroundColour = new Vector3(0,0,0);

    public RendererSystem(OrthographicCamera camera, OrthographicCamera guiCamera) {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        this.camera = camera;
        this.guiCamera = guiCamera;

        //TODO - Look into https://github.com/libgdx/libgdx/wiki/Distance-field-fonts for font scaling
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = new Color(0, 1, 0.2f, 1);
        parameter.size = 28;
        parameter.borderColor = Color.WHITE;
        parameter.borderWidth = 2f;
        parameter.borderGamma = 0.5f;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class).get());
        explosions = engine.getEntitiesFor(Family.all(ExplosionComponent.class, PositionComponent.class).get());
        uiEntities = engine.getEntitiesFor(Family.all(UiComponent.class, PositionComponent.class).get());
    }

    public void update(float deltaTime) {
        Gdx.gl.glClearColor(backgroundColour.x, backgroundColour.y, backgroundColour.z, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderEntities();
        renderBoundary();
        renderExplosions();
        renderGui();
    }

    private void renderEntities() {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : entities) {
            SpriteComponent texture = textureComponentMapper.get(entity);
            PositionComponent position = positionComponentMapper.get(entity);

            float width = texture.getTexture().getWidth() * PIXELS_TO_METERS;
            float height = texture.getTexture().getHeight() * PIXELS_TO_METERS;

            if (texture.isVisible()) {
                batch.draw(texture.getTexture(),
                        position.getX() - (width / 2),
                        position.getY() - (height / 2),
                        width,
                        height);
            }
        }
        batch.end();
    }

    private void renderGui() {
        batch.setProjectionMatrix(guiCamera.combined);

        batch.begin();
        for (Entity entity : uiEntities) {
            UiComponent uiComponent = uiComponentMapper.get(entity);
            PositionComponent positionComponent = positionComponentMapper.get(entity);
            font.draw(batch,
                    uiComponent.getText(),
                    positionComponent.getX(),
                    positionComponent.getY());
        }
        batch.end();
    }

    private void renderBoundary() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rectLine(VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, 0.1f);//RIGHT
        shapeRenderer.rectLine(-VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, -VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, 0.1f);//LEFT
        shapeRenderer.rectLine(-VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, 0.1f);//TOP
        shapeRenderer.rectLine(-VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, 0.1f);//BOTTOM
        shapeRenderer.end();
    }

    private void renderExplosions() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity entity : explosions) {
            PositionComponent positionComponent = positionComponentMapper.get(entity);
            ExplosionComponent explosionComponent = explosionComponentMapper.get(entity);
            shapeRenderer.circle(positionComponent.getX(), positionComponent.getY(), explosionComponent.getRadius(), 30);
        }
        shapeRenderer.end();
    }

    public void setBackgroundColour(Vector3 backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public void dispose() {
        batch.dispose();
    }
}
