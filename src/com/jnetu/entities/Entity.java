package com.jnetu.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;
import com.jnetu.world.Camera;

public class Entity {

	public static BufferedImage LIFE_EN = Game.spritesheet.getSprite(16, 32, 16, 16);
	public static BufferedImage FLOWER_EN = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage GUN_EN = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(0, 48, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(128, 48, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(48, 32, 16, 16);
	public static BufferedImage GUN_UP = Game.spritesheet.getSprite(48 + 16, 32, 16, 16);
	public static BufferedImage GUN_DOWN = Game.spritesheet.getSprite(48 + 32, 32, 16, 16);

	public double x;
	public double y;
	public int width;
	public int height;
	private int maskx, masky, maskw, maskh;

	private BufferedImage sprite;

	public Entity(double d, double e, int width, int height, BufferedImage sprite) {
		this.x = d;
		this.y = e;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.maskx = 0;
		this.masky = 0;
		this.maskh = height;
		this.maskw = width;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, (int) (this.getX() - Camera.x), (int) (this.getY() - Camera.y), null);
	}

	public void tick() {

	}

	public boolean isCollinding(Entity en1, Entity en2) {
		Rectangle r1 = new Rectangle((int) (en1.x + en1.maskx), (int) (en1.y + en1.masky), en1.maskw, en1.maskh);
		Rectangle r2 = new Rectangle((int) (en2.x + en2.maskx), (int) (en2.y + en2.masky), en2.maskw, en2.maskh);
		return r1.intersects(r2);
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
