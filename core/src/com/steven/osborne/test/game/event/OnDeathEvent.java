package com.steven.osborne.test.game.event;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public interface OnDeathEvent {
    void execute(Engine engine, Entity entity);
}
