package com.ltztec.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ltztec.main.Game;
import com.ltztec.world.Camera;
import com.ltztec.world.World;

public class Shoot extends Entity{

	private double dx;
	private double dy;
	private double spd = 5;
	
	private int life =60, curLife = 0;
	
	public Shoot(int x, int y, int width, int height, BufferedImage sprite,double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		if(World.isFreeDynamic((int)(x+(dx*spd)),(int)(y+(dy*spd)), life, curLife)) {
			x+=dx*spd;
			y+=dy*spd;
		}else {
			Game.bullets.remove(this);
			//World.generateParticle(2, (int)x, (int)y);
			return;
		}
			
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}

}
