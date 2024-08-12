package com.jnetu.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

public class Menu {
	public String[] options = { "Novo jogo", "Carregar", "Configuracoes", "Sair" };
	public int curOption = 0;
	public int maxOptions = options.length - 1;
	public boolean down = false;
	public boolean up = false;
	public boolean enter = false;

	public boolean pause = false;


	public void tick() {
		if (up) {
			up = false;
			curOption--;
			if (curOption < 0) {
				curOption = maxOptions;
			}
		}
		if (down) {
			down = false;
			curOption++;
			if (curOption > maxOptions) {
				curOption = 0;
			}
		}

		if (enter) {
			enter = false;
			
			if (options[curOption] == "Novo jogo") {
				pause = false;
				Game.gameState = "RUN";
				
			}
			if(options[curOption] == "Sair") {
				System.exit(1);
			}

		}

	}

	public void render(Graphics g) {
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

	    // Calcular o centro da tela
	    int offsetX = (int) ((screenWidth - gameWidth * scale) / 2);
	    int offsetY = (int) ((screenHeight - gameHeight * scale) / 2);
	    
	    // Ajustar as posições de desenho com base na escala e offset
	    int menuWidth = 200; // Largura do menu
	    int menuHeight = 120; // Altura do menu
	    int startX = screenWidth / 2 - menuWidth / 2;
	    int startY = screenHeight / 2 - menuHeight / 2;

	    // Configurar o background
	    g.setColor(Color.BLACK);
	    g.fillRect(0, 0, screenWidth, screenHeight);

	    // Configurar a cor e a fonte
	    g.setColor(Color.WHITE);
	    g.setFont(Game.font);

	    // Desenhar o título
	    String title = "Adventure Game";
	    FontMetrics metrics = g.getFontMetrics();
	    int titleWidth = metrics.stringWidth(title);
	    g.drawString(title, screenWidth / 2 - titleWidth / 2, startY - 40);

	    // Desenhar as opções do menu
	    String[] options = {"Novo Jogo", "Carregar", "Configuracoes", "Sair"};
	    int optionHeight = metrics.getHeight() + 10; // Altura da opção com margem
	    for (int i = 0; i < options.length; i++) {
	        String option = options[i];
	        int optionWidth = metrics.stringWidth(option);
	        g.drawString(option, screenWidth / 2 - optionWidth / 2, startY + i * optionHeight);
	    }

	    // Desenhar o marcador de seleção
	    String selectedOption = options[curOption];
	    int markerWidth = metrics.stringWidth(">");
	    int markerHeight = metrics.getHeight();
	    g.drawString(">", screenWidth / 2 - menuWidth / 2 - markerWidth - 10, startY + curOption * optionHeight);
	}
}
