package com.ltztec.entities;

import java.awt.image.BufferedImage;

public class Weapon extends Entity{

	public Weapon(int x, int y, int width, int height,  BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		depth  = 0;
		
	}

}
