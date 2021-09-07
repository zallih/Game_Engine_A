package com.ltztec.main;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.ltztec.entities.Enemy;
import com.ltztec.entities.Entity;
import com.ltztec.entities.Player;
import com.ltztec.entities.Shoot;
import com.ltztec.graficos.Spritesheet;
import com.ltztec.graficos.UI;
import com.ltztec.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true; 
	public final static int WIDTH = 360;
	public  final static int HEIGHT = 280;
	public static final int SCALE = 2;
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;

	public static BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Shoot> bullets;
	
	public static World world;
	
	public static Spritesheet spritesheet;
	
	public static Player player;
	
	public static Random rand;
	
	public static String gameState = "MENU"; 
	private boolean showMessageGameOver = true;
	
	private boolean restartGame;
	
	private int framesGameOver = 0;
	
	public static boolean saveGame = false;

	
	public static Menu menu;
	public static Pause pause;
	public static UI ui;
	
	public Game() {
		Sound.musicBackground.play();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//inicialiazando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Shoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(10,0,32,32, spritesheet.getSprite(0, 0, 32, 32)); 
		entities.add(player);
		world = new World("/level1.png");
		
		menu = new Menu();
		pause = new Pause();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[]args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		Game.player.updateCamera();
		if(gameState == "NORMAL") {
			
			if(Game.saveGame == true) {
				Game.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.CUR_LEVEL};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Salvo!");
			}
			
			this.restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			
			if(enemies.size()==0) {
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL=1;
				}
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restarGame(newWorld);
			}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 20) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) {
					this.showMessageGameOver = false;
				}else {
					this.showMessageGameOver = true;
				}
			}
			
			if(restartGame == true) {
				this.restartGame = false;
				Game.gameState = "NORMAL";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restarGame(newWorld);
			}
		}else if(gameState == "MENU") {
			menu.tick();
		}else if(gameState == "PAUSE") {
			if(Game.saveGame == true) {
				Game.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.CUR_LEVEL, (int) player.life, (int) enemies.size()};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Salvo!");
				
				
			}
			pause.tick();
		}
	}
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//Graphics2D g2 = (Graphics2D) g;
		world.render(g);
		
		//ordem entities
		Collections.sort(entities, Entity.nodeSorter);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 15));
		g.setColor(Color.white);
		g.drawString("Munição: " + player.ammo, 625,30);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0, 225));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g2.setFont(new Font("arial", Font.BOLD, 50));
			g2.setColor(Color.white);
			g2.drawString("Game over", (WIDTH*SCALE) / 2 - 110,(HEIGHT*SCALE) /2);
			g2.setFont(new Font("arial", Font.BOLD, 20));
			if(this.showMessageGameOver)
				g2.drawString(">Precione Enter para reiniciar<", (WIDTH*SCALE) / 2 - 120,(HEIGHT*SCALE) /2 + 50);		
		}else if(gameState == "MENU") {
			menu.render(g);
		}
		else if(gameState == "PAUSE") {
			pause.render(g);
		}
		 
		bs.show();
		

		
	} 
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning){
			long now = System.nanoTime();
			delta += (now-lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: "+frames);
				frames = 0;
				timer+=1000;
			}
		
		}
		stop();
	}

	


	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
			e.getKeyCode() == KeyEvent.VK_D) {
			
			player.right = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
				e.getKeyCode() == KeyEvent.VK_A) {
				player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP  ||
				e.getKeyCode() == KeyEvent.VK_W) {
				player.up = true;
				if(gameState == "MENU") {
					menu.up = true;
				}
				if(gameState == "PAUSE") {
					pause.up = true;
				}
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				player.down = true;
				if(gameState == "MENU") {
					menu.down = true;
				}if(gameState == "PAUSE") {
					pause.down = true;
				}
			}
	
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
			if(gameState == "PAUSE") {
				pause.enter = true;
			}
		}
	}	


	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
				
				player.right = false;
				
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || 
					e.getKeyCode() == KeyEvent.VK_A) {
					player.left = false;
		}
			
		if(e.getKeyCode() == KeyEvent.VK_UP  ||
					e.getKeyCode() == KeyEvent.VK_W) {
					player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
						e.getKeyCode() == KeyEvent.VK_S) {
						player.down = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "PAUSE";
		}
		
		if(e.getKeyCode() == KeyEvent.VK_B) {
			if(gameState == "NORMAL") {
				Game.saveGame = true;	
			}
		
		}
		
		
	}
	
	public void keyTyped(KeyEvent e) {
		
		
	}

	
	public void mouseClicked(MouseEvent e) {
		
		
	}


	public void mousePressed(MouseEvent e) {
		 player.mouseShoot = true;
		 player.mx = (e.getX() / 3);
		 player.my = (e.getY() / 3);
			
	}


	public void mouseReleased(MouseEvent e) {
	
		
	}

	
	public void mouseEntered(MouseEvent e) {
		
		
	}


	public void mouseExited(MouseEvent e) {
		
		
	}
}
