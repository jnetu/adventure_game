package com.jnetu.main;

import java.awt.Color;
import java.awt.Graphics;

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
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		g.setColor(Color.WHITE);
		g.drawString("adventure game", (int) (Game.WIDTH * 0.1), (int) (Game.HEIGHT * 0.2));
		
		if(pause) {
			g.drawString("Continuar", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 4);
			
		}else {
			g.drawString("Novo Jogo", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 4);
			
		}
		
		g.drawString("Carregar", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 4 + 30);
		g.drawString("Configuracoes", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 4 + 60);
		g.drawString("Sair", Game.WIDTH * Game.SCALE / 32, Game.HEIGHT * Game.SCALE / 4 + 90);
		if (options[curOption] == "Novo jogo") {
			g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE / 4);

		} else if (options[curOption] == "Carregar") {
			g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE / 4 + 30);

		} else if (options[curOption] == "Configuracoes") {
			g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE / 4 + 60);

		} else if (options[curOption] == "Sair") {
			g.drawString(">", Game.WIDTH * Game.SCALE / 128, Game.HEIGHT * Game.SCALE / 4 + 90);

		}
	}
}
