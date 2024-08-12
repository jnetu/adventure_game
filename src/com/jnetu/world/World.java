package com.jnetu.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.jnetu.entities.Enemy;
import com.jnetu.entities.Entity;
import com.jnetu.entities.Flower;
import com.jnetu.entities.Gun;
import com.jnetu.entities.Life;
import com.jnetu.main.Game;

public class World {

	private static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 16;

	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()]; // total pixels
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int curPixel = pixels[xx + (yy * map.getWidth())];

					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					if (curPixel == 0xFF000000) {// black ->floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
					} else if (curPixel == 0xFFFFFFFF) {// white -> wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
					} else if (curPixel == 0xFF0000FF) {// blue -> player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (curPixel == 0xff0fff00) {// green -> life
						Game.entities.add(new Life(xx * 16, yy * 16, 16, 16, Entity.LIFE_EN));
					} else if (curPixel == 0xfffff300) {// yellow -> flower
						Game.entities.add(new Flower(xx * 16, yy * 16, 16, 16, Entity.FLOWER_EN));
					} else if (curPixel == 0xffff0000) {// red -> enemy
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en); // new ArrayList to check collisions with others enemies
					} else if (curPixel == 0xffff00af) {// pink -> gun
						Game.entities.add(new Gun(xx * 16, yy * 16, 16, 16, Entity.GUN_EN));
					}

				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x3 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y3 = ynext / TILE_SIZE;

		int x4 = xnext / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
		return !(tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile
				|| tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile
				|| tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile
				|| tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile

		);

	}

	public void render(Graphics g) {
		int xstart = Camera.x / 16;
		int ystart = Camera.y / 16;
		int xfinal = xstart + (Game.WIDTH / 16) + 1;
		int yfinal = ystart + (Game.HEIGHT / 16) + 1;

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
