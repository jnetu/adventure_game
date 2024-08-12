package com.jnetu.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;

public class UI {
	
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(8, 8,50, 7);
		g.setColor(Color.GREEN);
		g.fillRect(8, 8, (int)((double)Game.player.life / 100 * 50), 7);
	}
	
	public void drawStrings(Graphics g) {
		
		g.setFont(Game.font.deriveFont(35f));
		g.setColor(Color.BLACK);
		String formattedAmmo = String.format("%03d", Game.player.ammo);
		
		g.drawString("Ammo: " + formattedAmmo, Game.WIDTH * Game.SCALE - 200, (int)(Game.HEIGHT * 0.1));
		g.setColor(Color.WHITE);
		g.setFont(Game.font.deriveFont(36f));
		g.drawString("Ammo: " + formattedAmmo, Game.WIDTH * Game.SCALE - 210, (int)(Game.HEIGHT * 0.1) - 4);
		
	}
}
