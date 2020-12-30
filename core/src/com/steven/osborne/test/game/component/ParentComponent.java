package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParentComponent implements Component {
    public enum Mode {
        CHILD_KILL_PARENT,
        PARENT_KILL_CHILD
    }
    private Entity parent;
    private Mode mode;
}
