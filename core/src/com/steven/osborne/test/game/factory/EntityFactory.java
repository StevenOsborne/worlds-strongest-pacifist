package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;

public interface EntityFactory {
    void create(Engine engine, int count);
}
