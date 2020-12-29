package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.steven.osborne.test.game.event.OnDeathEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OnDeathComponent implements Component {
    private OnDeathEvent onDeathEvent;
}
