package com.jnetu.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()]; //total pixels
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0,map.getWidth(), map.getHeight(), pixels, 0,map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx ++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int curPixel = pixels[xx+(yy*map.getWidth())];
					if(curPixel == 0xFF000000) {//black ->floor
						tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if(curPixel == 0xFFFFFFFF) {//white -> wall
						tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_WALL);
					}else if(curPixel == 0xFF0000FF) {//blue -> player
						tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else {
						//default floor
						tiles[xx+(yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy< HEIGHT; yy++) {
				Tile tile = tiles[xx+(yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
