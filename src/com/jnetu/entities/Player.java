package com.jnetu.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.jnetu.graphics.Spritesheet;
import com.jnetu.main.Game;
import com.jnetu.world.Camera;
import com.jnetu.world.World;

public class Player extends Entity {

	public boolean right, left, up, down;
	public float speed = 1.0f;

	private int frames = 0, maxFrames = 5;
	private int index = 0, maxIndex = 1;
	private boolean moved = false;

	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private int rightDirection = 0, leftDirection = 1, upDirection = 2, downDirection = 3;
	private int curDirection = rightDirection;

	public int life = 100;
	public int maxLife = 100;

	public int ammo = 0;

	public BufferedImage hitPlayer;
	public boolean isDamage = false;
	private int damageFrames = 0, maxDamageFrames = 10;
	private int gunType = 0; // 0 - without gun //1+ have gun

	public boolean shoot = false;
	public boolean mouseShoot = false;

	public int mousex, mousey;

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
		hitPlayer = spritesheet.getSprite(128, 0, 16, 16);

	}

	public void tick() {
		// Camera.x = this.getX() - Game.WIDTH/2;
		// Camera.y = this.getY() - Game.HEIGHT/2;
		Camera.x = Camera.clamp((int) (this.getX() - Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp((int) (this.getY() - Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);

		moved = false;

		if (right && World.isFree((int) (x + speed), (int) y)) {
			moved = true;
			curDirection = rightDirection;
			x += speed;
		}
		if (left && World.isFree((int) (x - speed), (int) y)) {
			moved = true;
			curDirection = leftDirection;
			x -= speed;
		}
		if (up && World.isFree((int) x, (int) (y - speed))) {
			moved = true;
			curDirection = upDirection;
			y -= speed;
		}
		if (down && World.isFree((int) x, (int) (y + speed))) {
			moved = true;
			curDirection = downDirection;
			y += speed;
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

		if (isDamage) {
			damageFrames++;
			if (damageFrames > maxDamageFrames) {
				isDamage = false;
				damageFrames = 0;
			}
		}

		if (shoot && gunType > 0 && ammo > 0) {
			shoot = false;
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 0;
			if (curDirection == rightDirection) {
				px = 16;
				py = 6;
				dx = 1;
				dy = 0;
			}
			if (curDirection == leftDirection) {
				px = -6;
				py = 6;
				dx = -1;
				dy = 0;
			}
			if (curDirection == upDirection) {
				px = 12;
				py = 4;
				dx = 0;
				dy = -1;
			}
			if (curDirection == downDirection) {
				py = 10;
				dx = 0;
				dy = 1;
			}
			Shoot s = new Shoot(x + px, y + py, 3, 3, null, dx, dy);
			Game.shoots.add(s);
			ammo--;
		} else {
			shoot = false;
		}

		if (mouseShoot && gunType > 0 && ammo > 0) {
			mouseShoot = false;

			int px = 2, py = 2;
			double angle = 0;
			
				if (curDirection == rightDirection) {
					px = 18;
					py = 7;
				} else if(curDirection == leftDirection) {
					px = -13;
					py = 7;
				}else if(curDirection == upDirection) {
					px = 13;
					py = 5;
				}else if(curDirection == downDirection) {
					px = -3;
					py = 14;
				}
			
			angle = Math.atan2(mousey - this.getY() + Camera.y, mousex - this.getX() + Camera.x);

			double dx = Math.cos(angle);
			double dy = Math.sin(angle);

			Shoot s = new Shoot(x + px, y + py, 3, 3, null, dx, dy);
			Game.shoots.add(s);
			ammo--;
		} else {
			mouseShoot = false;
		}

		checkCollisionItems();
	}

	public void render(Graphics g) {
		int dx = (int) (x - Camera.x);
		int dy = (int) (y - Camera.y);
		if (isDamage) {
			g.drawImage(hitPlayer, dx, dy, null);
			return;
		}
		if (curDirection == rightDirection) {
			g.drawImage(rightPlayer[index], dx, dy, null);
			if (gunType > 0) {
				g.drawImage(Entity.GUN_RIGHT, dx + 9, dy + 2, null);
			}
		} else if (curDirection == leftDirection) {
			g.drawImage(leftPlayer[index], dx, dy, null);
			if (gunType > 0) {
				g.drawImage(Entity.GUN_LEFT, dx - 11, dy + 2, null);
			}
		}
		if (curDirection == upDirection) {
			if (gunType > 0) {
				g.drawImage(Entity.GUN_UP, dx + 7, dy - 2, null);
			}
			g.drawImage(upPlayer[index], dx, dy, null);

		} else if (curDirection == downDirection) {
			g.drawImage(downPlayer[index], dx, dy, null);
			if (gunType > 0) {
				g.drawImage(Entity.GUN_DOWN, dx - 7, dy + 4, null);
			}
		}
	}

	public void hurt(int amount) {
		if (life <= 0 || life - amount <= 0) {
			if (Game.DEBUG) {
				return;
			}

			Game.Reset();

		}
		isDamage = true;
		life -= amount;
	}

	public void cure(int amount) {
		if (life >= maxLife || life + amount >= maxLife) {
			life = maxLife;
			return;
		}
		life += amount;
	}

	public void addAmmo(int amount) {
		ammo += amount;
	}

	private void checkCollisionItems() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity en = Game.entities.get(i);
			if (isCollinding(en, this)) {
				if (en instanceof Life) {
					cure(((Life) en).healthPoint);
					Game.entities.remove(i);
				} else if (en instanceof Flower) {
					addAmmo(((Flower) en).amountAmmo);
					Game.entities.remove(i);
				} else if (en instanceof Gun) {
					// more gun make more ids
					gunType = 1;
					Game.entities.remove(i);
					ammo += 10;
				}
			}
		}
	}

}
