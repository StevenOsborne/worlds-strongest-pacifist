package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.steven.osborne.test.game.WorldsStrongestPacifist;
import com.steven.osborne.test.game.component.InputComponent;
import com.steven.osborne.test.game.component.VelocityComponent;
import com.steven.osborne.test.game.input.ActionListener;
import com.steven.osborne.test.game.input.ControllerListener;
import com.steven.osborne.test.game.input.InputAction;
import org.libsdl.SDL;

public class InputSystem extends EntitySystem implements ActionListener, ControllerListener {

    private ImmutableArray<Entity> entities;

    private ComponentMapper<VelocityComponent> velocityComponentMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<InputComponent> inputComponentMapper = ComponentMapper.getFor(InputComponent.class);

    private WorldsStrongestPacifist worldsStrongestPacifist;

    public InputSystem(WorldsStrongestPacifist worldsStrongestPacifist) {
        this.worldsStrongestPacifist = worldsStrongestPacifist;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(InputComponent.class, VelocityComponent.class).get());
    }

    public void update(float deltaTime) {

    }

    @Override
    public boolean onActionDown(InputAction action) {
        for (Entity entity : entities) {
            VelocityComponent velocity = velocityComponentMapper.get(entity);

            if (action == InputAction.UP) {
                velocity.velocity.y = velocity.speed;
            } else if (action == InputAction.DOWN) {
                velocity.velocity.y = -velocity.speed;
            } else if (action == InputAction.LEFT) {
                velocity.velocity.x = -velocity.speed;
            } else if (action == InputAction.RIGHT) {
                velocity.velocity.x = velocity.speed;
            }
        }

        if (action == InputAction.MENU) {
            Gdx.app.exit();
        }
        return true;
    }

    @Override
    public boolean onActionUp(InputAction action) {
        for (Entity entity : entities) {
            VelocityComponent velocity = velocityComponentMapper.get(entity);

            if (action == InputAction.UP) {
                velocity.velocity.y = 0f;
            } else if (action == InputAction.DOWN) {
                velocity.velocity.y = 0f;
            } else if (action == InputAction.LEFT) {
                velocity.velocity.x = 0f;
            } else if (action == InputAction.RIGHT) {
                velocity.velocity.x = 0f;
            }
        }
        return true;
    }

    @Override
    public boolean onControllerAxisInput(int axisIndex, float value) {
        if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX || axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY) {
            for (Entity entity : entities) {
                InputComponent inputComponent = inputComponentMapper.get(entity);
                VelocityComponent velocity = velocityComponentMapper.get(entity);
                if (value < inputComponent.getControllerDeadZone() && value > -inputComponent.getControllerDeadZone()) {
                    value = 0f;
                }

                if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTX) {
                    velocity.velocity.x = value * velocity.speed;
                }
                if (axisIndex == SDL.SDL_CONTROLLER_AXIS_LEFTY) {
                    velocity.velocity.y = -value * velocity.speed;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onControllerButtonInput(int buttonIndex) {
        if (buttonIndex == SDL.SDL_CONTROLLER_BUTTON_X) {
            worldsStrongestPacifist.resetScreen();
        }
        return false;
    }
}
