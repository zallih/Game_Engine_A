package com.ltztec.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.ltztec.entities.Ammo;
import com.ltztec.entities.Enemy;
import com.ltztec.entities.Entity;
import com.ltztec.entities.Fence;
import com.ltztec.entities.HeartLife;
import com.ltztec.entities.House;
import com.ltztec.entities.Particle;
import com.ltztec.entities.Player;
import com.ltztec.entities.Weapon;
import com.ltztec.graficos.Spritesheet;
import com.ltztec.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static House[] housetile;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 32;
	public static int HOUSE_SIZE = 127;
	
	
	
	
	public World (String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*32, yy*32,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000) {
						 //Floor/Chão
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*32, yy*32,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFBFB) {
						//Wall/Parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32, yy*32,Tile.TILE_WALL);
					}else if(pixelAtual == 0xFF42231D) {
						//Fence cerca
						Fence en = new Fence(xx*32,yy*32,32,32,Entity.FENCE_EN);
						Game.entities.add(en);
						en.setMask(0, 0, 32, 15);

					}else if(pixelAtual == 0xFF31529C) {
						//Player
						Game.player.setX(xx*32);
						Game.player.setY(yy*32);
						
					}else if(pixelAtual == 0xFF519F96) {
						//NPC
						Game.npc.setX(xx*32);
						Game.npc.setY(yy*32);
						
					}else if(pixelAtual == 0xFF00FF00) {
						//Enemy
						Enemy en = new Enemy(xx*32,yy*32,32,32,Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}else if(pixelAtual == 0xFF778284) {
						//Flower flor1
						tiles[xx + (yy * WIDTH)] = new FlowerTile(xx*32, yy*32,Tile.TILE_FLOWER);
					}else if(pixelAtual == 0xFFD86D2F) {
						//Flower flor2
						tiles[xx + (yy * WIDTH)] = new FlowerTile(xx*32, yy*32,Tile.TILE_FLOWER1);
					}else if(pixelAtual == 0xFFFF7F7F) {
						//Flower flor3
						tiles[xx + (yy * WIDTH)] = new FlowerTile(xx*32, yy*32,Tile.TILE_FLOWER2);
					}else if(pixelAtual == 0xFF00BAFF) {
						//Flower flor4
						tiles[xx + (yy * WIDTH)] = new FlowerTile(xx*32, yy*32,Tile.TILE_FLOWER3);
					} else if(pixelAtual == 0xFFD4322B) {
						// Flower flor5
						tiles[xx + (yy * WIDTH)] = new FlowerTile(xx*32, yy*32,Tile.TILE_FLOWER4);
					}else if(pixelAtual == 0xFFFF0054) {
						//House casa
						House en = new House(xx*32,yy*32,127,127,Entity.HOUSE_EN);
						Game.entities.add(en);
					}else if(pixelAtual == 0xFFE100FF) {
						//pack life 
						HeartLife pack = new HeartLife(xx*32,yy*32,32,32,Entity.LIFE_EN);
						Game.entities.add(pack);
						pack.setMask(0, 0, 16, 16);
					}else if(pixelAtual == 0xFF0F9BFF) {
						//weapon arma
						Weapon en = new Weapon(xx*32,yy*32,32,32,Entity.WEAPON_EN);
						Game.entities.add(en);
						en.setMask(0, 0, 16, 16);
					}else if(pixelAtual == 0xFFFFFF00) {
						//Ammo munição
						Ammo en = new Ammo(xx*32,yy*32,32,32,Entity.AMMO_EN);
						Game.entities.add(en);
						en.setMask(0, 0, 16, 8);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			}
	}
	
	public static void generateParticle(int amount, int x, int y) {
		for(int i = 0; i < amount; i++) {
			Game.entities.add(new Particle(x,y,1,1,null));
		}
	}
	
	public static boolean isFreeDynamic(int xnext,int ynext, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+height-1) / TILE_SIZE;
		
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;
	
		return !((tiles[x1+(y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2+(y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3+(y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4+(y4*World.WIDTH)] instanceof WallTile) );
	}
	
	
	public static boolean isFree(int xnext,int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
	
		return !((tiles[x1+(y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2+(y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3+(y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4+(y4*World.WIDTH)] instanceof WallTile) );
	}
	
	public static void restarGame(String level) {
		Game.enemies.clear();
		Game.entities.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(10,0,32,32, Game.spritesheet.getSprite(0, 0, 32, 32)); 
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	public void render(Graphics g) { 

		int xstart = Camera.x >> 6;
		int ystart = Camera.y >> 6;

		int xfinal = xstart + (Game.WIDTH >> 2);
		int yfinal = ystart + (Game.HEIGHT >> 2);
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
	
	
	public static void renderMiniMap() {
		for(int i = 0; i < Game.miniMapPixels.length; i++) {
			Game.miniMapPixels[i] = 0;
		}
		for(int xx = 0; xx < WIDTH; xx++) {
			for(int yy = 0; yy < HEIGHT; yy++) {
				if(tiles[xx + (yy * WIDTH)] instanceof WallTile) {
					Game.miniMapPixels[xx + (yy * WIDTH)] = 0xff778284;
				}
			}
		}
		int xPlayer = Game.player.getX()/32;
		int yPlayer = Game.player.getY()/32;
		
		Game.miniMapPixels[xPlayer + (yPlayer * WIDTH)] = 0xff007F7F;
	}
	
}

