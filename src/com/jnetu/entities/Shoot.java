package com.jnetu.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;
import com.jnetu.world.Camera;

public class Shoot extends Entity {

	private double dx;
	private double dy;
	private double speed;
	private long creationTime;

	public Shoot(double x, double y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.speed = 4;
		this.dx = dx;
		this.dy = dy;
		this.creationTime = System.currentTimeMillis();
	}

	public void tick() {
		x += dx * speed;
		y += dy * speed;
		checkForDestruction(1000);
	}

	private void checkForDestruction(int millisecondsToDestroy) {
		if (System.currentTimeMillis() - creationTime >= millisecondsToDestroy) {
			Game.shoots.remove(this);
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval((int)x - Camera.x, (int)y - Camera.y, 3, 3);
	}
}
