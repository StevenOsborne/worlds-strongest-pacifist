package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.steven.osborne.test.game.gameobject.component.CameraFollowComponent;
import com.steven.osborne.test.game.gameobject.component.PositionComponent;

public class CameraSystem extends IteratingSystem {

    private static final float CAMERA_FRAME_SIZE_HORIZONTAL = 10f;
    private static final float CAMERA_FRAME_SIZE_VERTICAL = 10f;

    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<CameraFollowComponent> cameraComponentMapper = ComponentMapper.getFor(CameraFollowComponent.class);

    private OrthographicCamera camera;

    public CameraSystem(OrthographicCamera camera) {
        super(Family.all(PositionComponent.class, CameraFollowComponent.class).get());

        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionComponentMapper.get(entity);


//        camera.position.set(position.getX(), position.getY(), 0); //Camera just follows player

        camera.position.x = Math.min(Math.max(camera.position.x, position.getX() - CAMERA_FRAME_SIZE_HORIZONTAL), position.getX() + CAMERA_FRAME_SIZE_HORIZONTAL); //TODO - This is cool, but not right at all
        camera.position.y = Math.min(Math.max(camera.position.y, position.getY() - CAMERA_FRAME_SIZE_VERTICAL), position.getY() + CAMERA_FRAME_SIZE_VERTICAL);
        camera.update();
    }
}
