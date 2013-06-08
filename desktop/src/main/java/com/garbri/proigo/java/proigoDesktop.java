package com.garbri.proigo.java;

import com.garbri.proigo.core.AdventureBall;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class proigoDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.width = 1400;
		config.height = 900;
		new LwjglApplication(new AdventureBall(), config);
	}
}
