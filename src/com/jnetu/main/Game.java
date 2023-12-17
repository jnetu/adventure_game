package com.jnetu.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.jnetu.entities.Entity;
import com.jnetu.entities.Player;
import com.jnetu.graphics.Spritesheet;

public class Game extends Canvas implements Runnable {
	
	
	private final int WIDTH = 640;
	private final int HEIGHT = 448;
	private final int SCALE = 2;
	public static JFrame frame;
	
	private boolean running;
	private Thread thread;
	
	private BufferedImage image;
	
	public Spritesheet spritesheet;
	public List<Entity> entities;
	
	
	public Game(){
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE)); //set canvas dimensions
		frame = new JFrame("adventure game");
		frame.add(this); //add Canvas
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);//null --> center
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //break application on exit
		frame.setVisible(true);
		
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB); //layer background
		
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet.png");
		
		Player player = new Player(0,0,16,16,spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
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
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		//draw on background image
		Graphics g = image.getGraphics();
		g.setColor(new Color(100,100,100));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//render entities
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		
		//draw background
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE,HEIGHT*SCALE,null);
		
		bs.show();
		
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double fps = 60.0;
		double ns = 1000000000 / fps;
		double delta = 0;
		int frames = 0;//see frames per second
		double timer = System.currentTimeMillis(); //see frames per second
		for(;;) {
			long now = System.nanoTime();	
			delta+= (now - lastTime)/ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) { //see frames per second
				System.out.println(frames);//see fps
				frames = 0;
				timer+=1000;
			}
			
		}
	}

}
