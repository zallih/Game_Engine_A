package com.ltztec.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ltztec.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 288, 32, 32);
	
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(32, 288, 32, 32);
	
	
	public static BufferedImage TILE_FLOWER = Game.spritesheet.getSprite(0, 320, 32, 32);
	
	public static BufferedImage TILE_FLOWER1 = Game.spritesheet.getSprite(32, 320, 32, 32);

	public static BufferedImage TILE_FLOWER2 = Game.spritesheet.getSprite(0, 352, 32, 32);

	public static BufferedImage TILE_FLOWER3 = Game.spritesheet.getSprite(32, 352, 32, 32);

	public static BufferedImage TILE_FLOWER4 = Game.spritesheet.getSprite(64, 320, 32, 32);

	
	//public static BufferedImage TILE_HOUSE= Game.spritesheet.getSprite(0, 416, 127, 127);
	
	public static BufferedImage TILE_HOUSE2 = Game.spritesheet.getSprite(0, 288, 32, 32);
	

	public static BufferedImage TILE_LIFE = Game.spritesheet.getSprite(0, 384, 32, 32);

	
	private BufferedImage sprite;
	private int x,y;
	
	
	public Tile(int x, int y,  BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}


	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
