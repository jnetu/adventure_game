package com.jnetu.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jnetu.graphics.Spritesheet;

public class Player extends Entity{

	public boolean right, left, up, down;
	public float speed = 1;
	
	private int frames = 0,maxFrames = 5;
	private int index  = 0,maxIndex = 1;
	private boolean moved = false;
	
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private int rightDirection = 0,leftDirection = 1,upDirection = 2, downDirection = 3;
	private int curDirection = rightDirection;
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		Spritesheet spritesheet = new Spritesheet("/spritesheet.png");
		leftPlayer[0] = spritesheet.getSprite(0, 0, 16, 16);
		leftPlayer[1] = spritesheet.getSprite(16, 0, 16, 16);
		rightPlayer[0] = spritesheet.getSprite(32, 0, 16, 16);
		rightPlayer[1] = spritesheet.getSprite(48, 0, 16, 16);
		downPlayer[0] = spritesheet.getSprite(48 + 16, 0, 16, 16);
		downPlayer[1] = spritesheet.getSprite(48 + 32, 0, 16, 16);
		upPlayer[0] = spritesheet.getSprite(48 + 48, 0, 16, 16);
		upPlayer[1] = spritesheet.getSprite(48 + 48 + 16, 0, 16, 16);
		
	}
	
	public void tick() {
		moved=false;
		if(right) {
			moved = true;
			curDirection = rightDirection;
			x+=speed;
		}
		if(left) {
			moved = true;
			curDirection = leftDirection;
			x-=speed;
		}
		if(up) {
			moved = true;
			curDirection = upDirection;
			y-=speed;
		}
		if(down) {
			moved = true;
			curDirection = downDirection;
			y+=speed;
		}
		if(moved) {
			frames++;
			if(frames > maxFrames) {
				frames = 0;
				index++;
				if(index>maxIndex) {
					index = 0;
				}
			}
		}
	}
	
	public void render(Graphics g) {
		if(curDirection == rightDirection) {
			g.drawImage(rightPlayer[index], x, y, null);
		}else if(curDirection == leftDirection) {
			g.drawImage(leftPlayer[index], x, y, null);
		}
		if(curDirection == upDirection) {
			g.drawImage(upPlayer[index], x, y, null);
		}else if(curDirection == downDirection) {
			g.drawImage(downPlayer[index], x, y, null);
		}
	}
}
