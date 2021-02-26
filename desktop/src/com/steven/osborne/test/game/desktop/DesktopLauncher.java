package com.steven.osborne.test.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.steven.osborne.test.game.TestGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Test Game";
		config.width = 2560;
		config.height = 1440;
		config.fullscreen = false;

		config.backgroundFPS = 144;
		config.foregroundFPS = 144;
		new LwjglApplication(new TestGame(), config);
	}
}
