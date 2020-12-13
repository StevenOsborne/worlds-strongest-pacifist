package com.steven.osborne.test.game;

import com.badlogic.gdx.Game;
import com.steven.osborne.test.game.screen.GameScreen;

public class TestGame extends Game {
	
	@Override
	public void create () {
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
