package com.jnetu.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.jnetu.entities.Enemy;
import com.jnetu.entities.Entity;
import com.jnetu.entities.Player;
import com.jnetu.entities.Shoot;
import com.jnetu.graphics.Spritesheet;
import com.jnetu.graphics.UI;
import com.jnetu.world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300;
	public static final int HEIGHT = 300;
	public static final int SCALE = 2;
	public static JFrame frame;

	private Thread thread;

	private BufferedImage image;

	public static boolean DEBUG = true;

	public static Spritesheet spritesheet;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shoot> shoots;

	public static Player player;

	public static World world;

	public static Random random;

	public UI ui;
	public static Font font;
	public InputStream fontStream = ClassLoader.getSystemClassLoader().getResourceAsStream("8bit.TTF");

	public Game() {
		addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); // set canvas dimensions
		frame = new JFrame("adventure game");
		frame.add(this); // add Canvas
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);// null --> center
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // break application on exit
		frame.setVisible(true);

		random = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // layer background
		enemies = new ArrayList<Enemy>();
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");
		ui = new UI();
		shoots = new ArrayList<Shoot>();

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(30f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void Reset() {
		enemies = new ArrayList<Enemy>();
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/map.png");
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
		for (int i = 0; i < shoots.size(); i++) {
			Shoot s = shoots.get(i);
			s.tick();
		}
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		// draw on background image
		Graphics g = image.getGraphics();
		g.setColor(new Color(100, 100, 100));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// render world
		world.render(g);

		// render entities
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for (int i = 0; i < shoots.size(); i++) {
			Shoot s = shoots.get(i);
			s.render(g);
		}

		ui.render(g);

		// draw background
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		ui.drawStrings(g);

		bs.show();

	}

	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double fps = 60.0;
		double ns = 1000000000 / fps;
		double delta = 0;
		int frames = 0;// see frames per second
		double timer = System.currentTimeMillis(); // see frames per second
		for (;;) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) { // see frames per second
				System.out.println(frames);// see fps
				frames = 0;
				timer += 1000;
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			System.out.println("direita");
			player.right = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			System.out.println("esquerda");
			player.left = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			System.out.println("cima");
			player.up = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			System.out.println("baixo");
			player.down = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_X) {
			System.out.println("shoot");
			player.shoot = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			System.out.println("direita");
			player.right = false;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			System.out.println("esquerda");
			player.left = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			System.out.println("cima");
			player.up = false;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			System.out.println("baixo");
			player.down = false;
		}

	}

}
