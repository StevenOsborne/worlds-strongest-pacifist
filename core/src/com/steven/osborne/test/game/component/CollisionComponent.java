package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class CollisionComponent implements Component {
    private boolean isStatic;
    private String tag;
    private List<String> destroyTags;
    private List<Entity> collidingWith;

    public List<String> getDestroyTags() {
        return destroyTags != null ? destroyTags : Collections.emptyList();
    }

    public List<Entity> getCollidingWith() {
        if (collidingWith == null) {
            collidingWith = new ArrayList<>();
        }
        return collidingWith;
    }
}
