package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;

public interface EntityFactory {
    void create(Engine engine, Vector2 position);
    void dispose();
}
