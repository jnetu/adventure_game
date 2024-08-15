package com.jnetu.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import com.jnetu.world.Camera;
import com.jnetu.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 256;
	public static final int HEIGHT = 256;
	public static final int SCALE = 1;
	public static int actualLevel = 1, maxLevel = 2;

	public static JFrame frame;

	private Thread thread;

	private BufferedImage image;

	public static boolean DEBUG = false;

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
	// MENU,GAMEOVER,RUN
	public static String gameState = "MENU";
	public boolean pressReset = false;
	private GraphicsDevice graphicsDevice;
	

	public Menu menu;
	
	
	
	private void setFullScreen(JFrame frame) {
	    if (graphicsDevice.isFullScreenSupported()) {
	        graphicsDevice.setFullScreenWindow(frame);
	    } else {
	        System.err.println("Fullscreen mode not supported");
	    }
	}
	
	private void exitFullScreen(JFrame frame) {
	    if (graphicsDevice != null && graphicsDevice.getFullScreenWindow() == frame) {
	        graphicsDevice.setFullScreenWindow(null);
	        frame.setUndecorated(false);
	        frame.setResizable(true);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	    }
	}
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		//this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); // set canvas dimensions
		this.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
		frame = new JFrame("adventure game");
		frame.add(this); // add Canvas
		frame.setUndecorated(true);
		
		graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		setFullScreen(frame);
		//exitFullScreen(frame);
		
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);// null --> center
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // break application on exit
		frame.setVisible(true);
		
		
		
		random = new Random();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // layer background
		spritesheet = new Spritesheet("/spritesheet.png");

		enemies = new ArrayList<Enemy>();
		entities = new ArrayList<Entity>();
		shoots = new ArrayList<Shoot>();
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/map1.png");

		ui = new UI();

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(30f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		menu = new Menu();
		
		Sound.music.loop();
	}

	public static void Reset() {
		enemies = new ArrayList<Enemy>();
		entities = new ArrayList<Entity>();
		shoots = new ArrayList<Shoot>();
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/map1.png");
		gameState = "RUN";
		Sound.music.reset();
		Sound.music.loop();
	}

	public static void LoadWorld(String level) {
		enemies = new ArrayList<Enemy>();
		entities = new ArrayList<Entity>();
		shoots = new ArrayList<Shoot>();
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 0, 16, 16));
		entities.add(player);
		world = new World("/" + level);
		gameState = "RUN";
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
		if (gameState == "RUN") {

			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			for (int i = 0; i < shoots.size(); i++) {
				Shoot s = shoots.get(i);
				s.tick();
			}

			// rule complete
			if (enemies.size() <= 0) {
				actualLevel++;
				if (actualLevel > maxLevel) {
					actualLevel = 1;
				}
				String levelFormat = "map" + actualLevel + ".png";
				LoadWorld(levelFormat);
			}
			pressReset = false;
		} else if (gameState == "GAMEOVER") {
			Sound.music.stop();
			if (pressReset) {
				pressReset = false;
				
				Reset();
			}

		} else if (gameState == "MENU") {
			menu.tick();
		}

	}

//	public void render() {
//		BufferStrategy bs = this.getBufferStrategy();
//		if (bs == null) {
//			this.createBufferStrategy(3);
//			return;
//		}
//		// draw on background image
//		Graphics g = image.getGraphics();
//		g.setColor(new Color(100, 100, 100));
//		g.fillRect(0, 0, WIDTH, HEIGHT);
//
//		// render world
//		world.render(g);
//
//		// render entities
//		for (int i = 0; i < entities.size(); i++) {
//			Entity e = entities.get(i);
//			e.render(g);
//		}
//		for (int i = 0; i < shoots.size(); i++) {
//			Shoot s = shoots.get(i);
//			s.render(g);
//		}
//
//		ui.render(g);
//
//		// draw background
//		g.dispose();
//		g = bs.getDrawGraphics();
//		//g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
//		g.drawImage(image, 0, 0, 
//				Toolkit.getDefaultToolkit().getScreenSize().width, 
//				Toolkit.getDefaultToolkit().getScreenSize().height, 
//				null);
//		
//		
//		ui.drawStrings(g);
//
//		if (gameState == "GAMEOVER") {
//			g.setColor(Color.BLACK);
//			g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
//			g.setColor(Color.WHITE);
//			FontMetrics metrics = g.getFontMetrics(font);
//
//			Rectangle rect = new Rectangle(WIDTH * SCALE, HEIGHT * SCALE);
//			g.drawString("Game Over", 200, 310);
//
//			g.drawString("Press R to Restart", 130, 410);
//		} else if (gameState == "MENU") {
//			menu.render(g);
//		}
//
//		bs.show();
//
//	}
	
	public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        
        
        Graphics g = image.getGraphics();
        g.setColor(new Color(100, 100, 100));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        world.render(g);

        for (Entity e : entities) {
            e.render(g);
        }
        for (Shoot s : shoots) {
            s.render(g);
        }
        
        
        g.dispose();
        g = bs.getDrawGraphics();
        

        ScreenDimensions screenDimensions = new ScreenDimensions(WIDTH, HEIGHT);
       
        
        g.drawImage(image, screenDimensions.getXOffset(), screenDimensions.getYOffset(), 
        		screenDimensions.getNewWidth(), 
        		screenDimensions.getNewHeight(), null);
        
        
        ui.render(g);
        
        if (gameState.equals("GAMEOVER")) {
            renderGameOver(g, screenDimensions.getXOffset(), screenDimensions.getYOffset(), 
            		screenDimensions.getNewWidth(), 
            		screenDimensions.getNewHeight());
            
        } else if (gameState.equals("MENU")) {
            //menu.render(g, xOffset, yOffset, newWidth, newHeight);
        	menu.render(g);
        }

        bs.show();
    }
	
	private void renderGameOver(Graphics g, int xOffset, int yOffset, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(xOffset, yOffset, width, height);
        g.setColor(Color.WHITE);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        String gameOverText = "Game Over";
        String restartText = "Press R to Restart";

        int gameOverX = xOffset + (width - metrics.stringWidth(gameOverText)) / 2;
        int gameOverY = yOffset + height / 2 - metrics.getHeight() / 2;
        int restartX = xOffset + (width - metrics.stringWidth(restartText)) / 2;
        int restartY = gameOverY + metrics.getHeight() + 20;

        g.drawString(gameOverText, gameOverX, gameOverY);
        g.drawString(restartText, restartX, restartY);
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
			if (gameState == "MENU") {
				menu.up = true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			System.out.println("baixo");
			player.down = true;
			if (gameState == "MENU") {
				menu.down = true;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_X) {
			System.out.println("shoot");
			player.shoot = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_CONTROL
				|| e.getKeyCode() == KeyEvent.VK_ENTER) {
			pressReset = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_CONTROL
				|| e.getKeyCode() == KeyEvent.VK_ENTER) {
			menu.enter = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (gameState == "MENU") {
				
				if(menu.pause) {
					gameState = "RUN";
					menu.pause = false;
				}
				
			} else {

				gameState = "MENU";
				menu.pause = true;
			}
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
			if (gameState == "MENU") {
				menu.up = false;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			System.out.println("baixo");
			player.down = false;
			if (gameState == "MENU") {
				menu.down = false;
			}
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
	    if(gameState == "MENU") {
	    	return;
	    }
	    player.mouseShoot = true;
	    
//	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//	    int screenWidth = screenSize.width;
//	    int screenHeight = screenSize.height;
//
//	    int newWidth = screenWidth;
//	    int newHeight = (screenWidth * HEIGHT) / WIDTH;
//
//	    if (newHeight > screenHeight) {
//	        newHeight = screenHeight;
//	        newWidth = (screenHeight * WIDTH) / HEIGHT;
//	    }
//
//	    int xOffset = (screenWidth - newWidth) / 2;
//	    int yOffset = (screenHeight - newHeight) / 2;
//
//	    int scaledMouseX = (e.getX() - xOffset) * WIDTH / newWidth;
//	    int scaledMouseY = (e.getY() - yOffset) * HEIGHT / newHeight;
//
//	    player.mousex = scaledMouseX;
//	    player.mousey = scaledMouseY;
//
//	    double angle = Math.atan2(scaledMouseY - player.getY() + Camera.y, 
//	                              scaledMouseX - player.getX() + Camera.x);
	    
	    
	    ScreenDimensions screenDimensions = new ScreenDimensions(WIDTH, HEIGHT);

	    int scaledMouseX = screenDimensions.scaleX(e.getX(), WIDTH);
	    int scaledMouseY = screenDimensions.scaleY(e.getY(), HEIGHT);

	    player.mousex = scaledMouseX;
	    player.mousey = scaledMouseY;

	    double angle = Math.atan2(scaledMouseY - player.getY() + Camera.y, 
	                              scaledMouseX - player.getX() + Camera.x);

	    
	    // Debugging
	    System.out.println("Mouse Original: (" + e.getX() + ", " + e.getY() + ")");
	    System.out.println("Mouse Scaled: (" + scaledMouseX + ", " + scaledMouseY + ")");
	    System.out.println("Angle: " + Math.toDegrees(angle));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
