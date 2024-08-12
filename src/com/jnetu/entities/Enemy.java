package com.jnetu.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jnetu.graphics.Spritesheet;
import com.jnetu.main.Game;
import com.jnetu.world.Camera;
import com.jnetu.world.World;

public class Enemy extends Entity {

	private int speed = 1;
	private int maskx = 0, masky = 1, maskw = 10, maskh = 14;
	private BufferedImage[] rightEnemy, leftEnemy;

	private int frames = 0, maxFrames = 6;
	private int index = 0, maxIndex = 3;
	private boolean moved = false;
	private boolean movedRight = false;
	private boolean cooldown = false;
	private int intervaloAtaque = 1000;
	private long lastHurtTime;
	private boolean isDamage = false;
	private int damageFrames = 0, maxDamageFrames = 10;
	private int life = 3;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightEnemy = new BufferedImage[4];
		leftEnemy = new BufferedImage[4];
		Spritesheet spritesheet = Game.spritesheet;
		rightEnemy[0] = spritesheet.getSprite(0, 48, 16, 16);
		rightEnemy[1] = spritesheet.getSprite(16, 48, 16, 16);
		rightEnemy[2] = spritesheet.getSprite(32, 48, 16, 16);
		rightEnemy[3] = spritesheet.getSprite(48, 48, 16, 16);

		leftEnemy[0] = spritesheet.getSprite(64, 48, 16, 16);
		leftEnemy[1] = spritesheet.getSprite(80, 48, 16, 16);
		leftEnemy[2] = spritesheet.getSprite(96, 48, 16, 16);
		leftEnemy[3] = spritesheet.getSprite(112, 48, 16, 16);

		maskx = 0;
		masky = 1;
		maskw = 10;
		maskh = 14;
	}

	public void render(Graphics g) {

		if (movedRight) {
			g.drawImage(rightEnemy[index], (int) (this.getX() - Camera.x), (int) (this.getY() - Camera.y), null);

		} else {
			g.drawImage(leftEnemy[index], (int) (this.getX() - Camera.x) - 6, (int) (this.getY() - Camera.y), null);

		}
		if(isDamage) {
			g.drawImage(Entity.ENEMY_FEEDBACK, (int) (this.getX() - Camera.x), (int) (this.getY() - Camera.y), null);
		}

		g.setColor(new Color(255, 0, 0, 255));
		g.drawRect((int) x + maskx - Camera.x, (int) y + masky - Camera.y, maskw, maskh);

	}

	public void tick() {
		moved = false;
		if (isCollinding(Game.player, this) == false) {

			if (Game.random.nextInt(100) < 50) {
				if (x < Game.player.getX() && World.isFree((int) (x + speed), (int) y)
						&& !haveCollision((int) (x + speed), (int) y)) {
					x += speed;
					moved = true;
					movedRight = true;
				} else if (x > Game.player.getX() && World.isFree((int) (x - speed), (int) y)
						&& !haveCollision((int) (x - speed), (int) y)) {
					x -= speed;
					moved = true;
					movedRight = false;
				}

				if (y < Game.player.getY() && World.isFree((int) x, (int) (y + speed))
						&& !haveCollision((int) x, (int) (y + speed))) {
					y += speed;
					moved = true;
				} else if (y > Game.player.getY() && World.isFree((int) x, (int) (y - speed))
						&& !haveCollision((int) x, (int) (y - speed))) {
					y -= speed;
					moved = true;
				}
			}
		} else {
			if (!cooldown) {
				Game.player.hurt(10);
				cooldown = true;
				lastHurtTime = System.currentTimeMillis();
			}

		}

		if (cooldown) {
			if (System.currentTimeMillis() - lastHurtTime >= intervaloAtaque) {
				cooldown = false;
			}
		}

		if (moved) {
			frames++;
			if (frames > maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}

		if (collisionBullet()) {
			isDamage = true;
			life--;
		}
		if (life <= 0) {
			Game.enemies.remove(this);
			Game.entities.remove(this);
		}
		if (isDamage) {
			damageFrames++;
			if (damageFrames > maxDamageFrames) {
				isDamage = false;
				damageFrames = 0;
			}
		}

	}

	private boolean collisionBullet() {
		for (int i = 0; i < Game.shoots.size(); i++) {
			Shoot s = Game.shoots.get(i);
			if (isCollinding(this, s)) {
				Game.shoots.remove(i);
				return true;
			}

		}
		return false;

	}

//	public boolean collisionWithPlayer() {
//		Rectangle curEnemy = new Rectangle(this.x + maskx, this.y + masky, maskw, maskh);
//		Rectangle player = new Rectangle(Game.player.x, Game.player.y, 16, 16);
//
//		return curEnemy.intersects(player);
//	}

	public boolean haveCollision(int xnext, int ynext) {
		Rectangle curEnemy = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for (int i = 0; i < Game.enemies.size(); i++) {
			Enemy en = Game.enemies.get(i);
			if (en == this) {

				continue;
			}
			Rectangle targetEnemy = new Rectangle((int) en.getX() + maskx, (int) en.getY() + masky, maskw, maskh);
			if (targetEnemy.intersects(curEnemy)) {

				return true;

			}
		}
		return false;
	}
}
