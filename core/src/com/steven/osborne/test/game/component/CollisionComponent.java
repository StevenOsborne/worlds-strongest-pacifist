package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class CollisionComponent implements Component {
    private boolean isStatic;
    private String tag;
    private List<String> destroyTags;
    private Entity collidingWith;
    private List<String> collideTags;

    public List<String> getDestroyTags() {
        return destroyTags != null ? destroyTags : Collections.emptyList();
    }
}
