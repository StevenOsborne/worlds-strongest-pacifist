package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class TextureComponent implements Component {
    private Texture texture;
    private boolean visible = true;
}
