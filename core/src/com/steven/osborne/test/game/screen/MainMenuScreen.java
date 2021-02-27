package com.steven.osborne.test.game.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.steven.osborne.test.game.WorldsStrongestPacifist;

public class MainMenuScreen extends ScreenAdapter {

    private WorldsStrongestPacifist worldsStrongestPacifist;
    private Engine engine;

    public MainMenuScreen(WorldsStrongestPacifist worldsStrongestPacifist, Engine engine) {
        this.worldsStrongestPacifist = worldsStrongestPacifist;
        this.engine = engine;
    }

    @Override
    public void show () {
        worldsStrongestPacifist.switchScreen(ScreenName.GAME);
    }

    @Override
    public void render(float delta) {
//        engine.update(delta);
    }
}
