package com.steven.osborne.test.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.steven.osborne.test.game.TestGame;
import com.steven.osborne.test.game.gameobject.component.PositionComponent;
import com.steven.osborne.test.game.gameobject.component.TextureComponent;
import com.steven.osborne.test.game.system.RendererSystem;

public class GameScreen implements Screen {

    private Engine engine;

    public GameScreen(TestGame testGame) {
        engine = new Engine();

        initialiseEntities(engine);
        initialiseSystems(engine);
    }

    private void initialiseEntities(Engine engine) {//Should this live in its own class?
        Entity player = new Entity();
        player.add(new TextureComponent(new Texture("player.png")));//TODO - This should use a texture atlas
        player.add(new PositionComponent(400f, 240f));
        engine.addEntity(player);
    }

    private void initialiseSystems(Engine engine) {//Should this live in its own class?
        RendererSystem renderer = new RendererSystem();
        renderer.setBackgroundColour(new Vector3(0, 0.05f, 0.1f));
        engine.addSystem(renderer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
