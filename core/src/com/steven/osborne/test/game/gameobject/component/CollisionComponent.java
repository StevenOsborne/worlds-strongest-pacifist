package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
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
    private List<String> collideTags;

    public List<String> getDestroyTags() {
        return destroyTags != null ? destroyTags : Collections.emptyList();
    }

    public List<String> getCollideTags() {
        return collideTags != null ? collideTags : Collections.emptyList();
    }
}
