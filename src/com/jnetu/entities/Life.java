package com.jnetu.entities;

import java.awt.image.BufferedImage;

public class Life extends Entity{
	
	public int healthPoint;
	
	public Life(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		healthPoint = 40;
	}

}
