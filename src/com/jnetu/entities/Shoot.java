package com.jnetu.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;
import com.jnetu.world.Camera;

public class Shoot extends Entity {

	private int dx;
	private int dy;
	private double speed;
	private long creationTime;

	public Shoot(int x, int y, int width, int height, BufferedImage sprite, int dx, int dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
		this.speed = 4;
		creationTime = System.currentTimeMillis();
	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;
		timeToDestroyObject(1000);
	}

	private void timeToDestroyObject(int millisecondsToDestroy) {

		if (System.currentTimeMillis() - creationTime >= millisecondsToDestroy) {
			Game.shoots.remove(this);
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(x - Camera.x, y - Camera.y, 3, 3);
	}
}
