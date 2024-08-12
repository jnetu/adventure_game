package com.jnetu.entities;

import java.awt.image.BufferedImage;

public class Flower extends Entity {

	public int amountAmmo;

	public Flower(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		amountAmmo = 6;
	}

}
