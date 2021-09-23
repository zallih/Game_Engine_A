package com.ltztec.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ltztec.main.Game;
import com.ltztec.main.Sound;
import com.ltztec.world.Camera;
import com.ltztec.world.World;

public class Player extends Entity{

	public boolean right, left, up, down; 
	public double speed = 1.2;
	
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = down_dir;
	
	private int frames = 0, maxFrames = 8, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage playerDamageUp;
	private BufferedImage playerDamageDown;
	private BufferedImage playerDamageLeft;
	private BufferedImage playerDamageRight;

	
	public double life = 100,  
			      maxLife =100;
	
	public int ammo = 0;
	
	public boolean isDamage = false;
	private int damageFrames = 0;
	
	private boolean haveGun = false;
	
	public boolean shoot = false, mouseShoot = false;
	
	public int mx, my;


	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		
		playerDamageUp = Game.spritesheet.getSprite(64, 96, 32, 32);
		playerDamageDown = Game.spritesheet.getSprite(64, 0, 32, 32);
		playerDamageLeft = Game.spritesheet.getSprite(64, 32, 32, 32);
		playerDamageRight = Game.spritesheet.getSprite(64, 64, 32, 32);
		
		for(int i = 0; i<4;i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 , 128 + (i*32), 32, 32);	
		}
		for(int i = 0; i<4;i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(0 , 0+ (i*32), 32, 32);	
		}
		for(int i = 0; i<4;i++) {
			upPlayer[i] = Game.spritesheet.getSprite(0, 128 + (i*32), 32, 32);	
		}
		for(int i = 0; i<4;i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32, 0 + (i*32), 32, 32);
			
		}
	}


	public void tick() {
		depth = 1;
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		} else if(left && World.isFree((int)(x-speed),this.getY())){
			moved = true;
			dir = left_dir;
			x-=speed;
		}else if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		}else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
		}
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		this.checkLifePack();
		this.checkWeapon();
		this.checkAmmo();
		
		if(isDamage) {
			this.damageFrames++;
			if(this.damageFrames == 5) {
				this.damageFrames = 0;
				this.isDamage = false;
			}
		}
		
		if(shoot ) {
			//atirar
			shoot =false;
			if(haveGun==true && ammo > 0) {
				ammo--;
				int dx = 0;
				int px = 0;
				int py = 6;
				if(dir == right_dir || dir == down_dir) {
					px = 15;
					py = 20;
					dx =1;
				}else {
					py = 20;
					px = -3;
					dx = -1;
				}
				Shoot shoot = new Shoot(this.getX()+px, this.getY()+py, 4, 4, null, dx, 0);
				Game.bullets.add(shoot);
			}
		}
		
		/*
		if (mouseShoot) {
			// atirar com mouse
			mouseShoot = false;
			if (haveGun == true && ammo > 0) {
				ammo--;
				int px, py = 8;
				double angle = 0;

				if (dir == right_dir || dir == down_dir) {
					px = 16;

					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));

				} else {
					px = -3;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));

				}
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				Shoot shoot = new Shoot(this.getX() + px, this.getY() + py, 4, 4, null, dx, dy);
				Game.bullets.add(shoot);
			}
		}
		 */
		
		if(life<=0) {
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
		
		
	}
	
	public void updateCamera() {
		Camera.x =Camera.clamp( this.getX() - (Game.WIDTH/2), 0, World.WIDTH*32 - Game.WIDTH);
		Camera.y = Camera.clamp( this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*32 - Game.HEIGHT);
	}
	
	public void checkAmmo() {
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Ammo) {
				if(Entity.isColliding(this, atual)) {
					ammo+=5;
					System.out.print("Munição: " + ammo);
					Game.entities.remove(atual);
				}
			}
		}
		
	}
	public void checkWeapon() {
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColliding(this, atual)) {
					haveGun = true;
					System.out.print("Arma coletada");
					Game.entities.remove(atual);
				}
			}
		}
		
	}
	
	
		
	public void checkLifePack() {
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof HeartLife) {
				if(Entity.isColliding(this, atual)) {
					life+=10;
					if(life>100)
						life=100;
					Game.entities.remove(atual);
				}
			}
		}
		
	}
	

	public void render(Graphics g) {
		if(isDamage == false) {	
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(this.haveGun == true) {
					//direita
					g.drawImage(Entity.GUN_RIGHT,this.getX()+ 8 - Camera.x, this.getY()+20 - Camera.y, null);
				}
			}else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(this.haveGun == true) {
					//esqueda
					g.drawImage(Entity.GUN_LEFT,this.getX()-6 - Camera.x, this.getY()+20 - Camera.y, null);
				}
			}else if(dir == up_dir) {
				g.drawImage(upPlayer[index],this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(this.haveGun == true) {
					//cima
				
				}
			} else if(dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(this.haveGun == true) {
					//direita
					g.drawImage(Entity.GUN_RIGHT,this.getX()+5 - Camera.x, this.getY()+21 - Camera.y, null);
				}
			}  
		}else {
			Sound.hit.play();
			if(dir == right_dir) {
				g.drawImage(playerDamageRight, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else if(dir == left_dir) {
				g.drawImage(playerDamageLeft, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}else if(dir == up_dir) {
				g.drawImage(playerDamageUp,this.getX() - Camera.x, this.getY() - Camera.y, null);
			} else if(dir == down_dir) {
				g.drawImage(playerDamageDown, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}  
			if(haveGun == true) {
				if(dir == right_dir) {
					g.drawImage(Entity.GUN_RIGHT_DAMAGE,this.getX()+ 8 - Camera.x, this.getY()+20 - Camera.y, null);
				}else if(dir == left_dir){
					g.drawImage(Entity.GUN_LEFT_DAMAGE,this.getX()-6 - Camera.x, this.getY()+20 - Camera.y, null);
				}else if(dir == down_dir){
					g.drawImage(Entity.GUN_RIGHT_DAMAGE,this.getX()+5 - Camera.x, this.getY()+21 - Camera.y, null);
				}
			}
		}
		
	}
}

