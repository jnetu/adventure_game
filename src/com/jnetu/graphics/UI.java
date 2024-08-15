package com.jnetu.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.jnetu.main.Game;
import com.jnetu.main.ScreenDimensions;

public class UI {
	int larguraBarraDeVida = 200;
	int alturaBarraDeVida = 20;
	
	public void render(Graphics g) {
		drawStrings(g);
		
		
	}
	
	public void drawStrings(Graphics g) {
		
		// Obter dimensões da tela
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    
	    // Definir largura e altura da tela do jogo
	    int gameWidth = Game.WIDTH * Game.SCALE;
	    int gameHeight = Game.HEIGHT * Game.SCALE;
	    
	    
	    // Calcular a escala para manter a proporção correta
	    double scaleX = (double) screenWidth / gameWidth;
	    double scaleY = (double) screenHeight / gameHeight;
	    double scale = Math.min(scaleX, scaleY);

	    // Calcular o offset da tela
	    int offsetX = (int) ((screenWidth - gameWidth * scale) / 2);
	    int offsetY = (int) ((screenHeight - gameHeight * scale) / 2);
	    
	    // Calcular o centro da tela
	    int centerX = (int) (gameWidth * scale) / 2 + offsetX;
	    int centerY = (int) (gameHeight * scale) / 2 + offsetY;
	    
	    g.setFont(Game.font.deriveFont(36f));
		g.setColor(Color.WHITE);
		
		
		String formattedAmmo = String.format("%03d", Game.player.ammo);
		g.setColor(Color.BLACK);
		g.drawString("Ammo: " + formattedAmmo, offsetX, 100);
		g.setColor(Color.WHITE);
		g.setFont(Game.font.deriveFont(36f));
		g.drawString("Ammo: " + formattedAmmo, offsetX + 10, 91);
		
		
		
		
		//BARRA DE VIDA
		g.setColor(Color.BLACK);
		g.fillRect(centerX, 8 - 2,larguraBarraDeVida + 2, alturaBarraDeVida + 4);
		g.setColor(Color.GREEN);
		g.fillRect(offsetX, 8, (int)((double)Game.player.life / 100 * larguraBarraDeVida), alturaBarraDeVida);
		//END BARRA DE VIDA
		
			
	}
}
