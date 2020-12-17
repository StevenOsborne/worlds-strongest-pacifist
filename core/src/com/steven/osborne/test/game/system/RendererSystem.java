package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.steven.osborne.test.game.gameobject.component.PositionComponent;
import com.steven.osborne.test.game.gameobject.component.SpriteComponent;

import static com.steven.osborne.test.game.screen.GameScreen.PIXELS_TO_METERS;

public class RendererSystem extends EntitySystem {
    private static final float HORIZONTAL_BOUNDARY = 18f;
    private static final float VERTICAL_BOUNDARY = 32f;

    private ImmutableArray<Entity> entities;

    private ComponentMapper<SpriteComponent> textureComponentMapper = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera; //TODO - Should this be passed in?
    private Vector3 backgroundColour = new Vector3(0,0,0);

    public RendererSystem(OrthographicCamera camera) {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(SpriteComponent.class, PositionComponent.class).get());
    }

    public void update(float deltaTime) {
        Gdx.gl.glClearColor(backgroundColour.x, backgroundColour.y, backgroundColour.z, 1);//TODO - Should background be an image?
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : entities) {
            SpriteComponent texture = textureComponentMapper.get(entity);
            PositionComponent position = positionComponentMapper.get(entity);

            if (texture.isVisible()) {
                batch.draw(texture.getTexture(),
                        position.getX(),
                        position.getY(),
                        texture.getTexture().getWidth() * PIXELS_TO_METERS,
                        texture.getTexture().getHeight() * PIXELS_TO_METERS);
            }
        }
        batch.end();

        createBoundary();
    }

    private void createBoundary() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rectLine(VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, 0.1f);//RIGHT
        shapeRenderer.rectLine(-VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, -VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, 0.1f);//LEFT
        shapeRenderer.rectLine(-VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, VERTICAL_BOUNDARY, HORIZONTAL_BOUNDARY, 0.1f);//TOP
        shapeRenderer.rectLine(-VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, VERTICAL_BOUNDARY, -HORIZONTAL_BOUNDARY, 0.1f);//BOTTOM
        shapeRenderer.end();
    }

    public void setBackgroundColour(Vector3 backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    public void dispose() {
        batch.dispose();
    }
}
