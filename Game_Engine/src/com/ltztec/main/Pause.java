package com.ltztec.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Pause {
	public String[] options = {"continuar", "salvar", "sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter;
	
	public void tick(){
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		if(down) {
			down = false; 
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		if(enter) {
			enter=false;
			if(options[currentOption] == "continuar") {
				Game.gameState = "NORMAL";
			}else if(options[currentOption] == "salvar") {
				Game.saveGame = true;
			}else if(options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.magenta);
		g.setFont(new Font("arial", Font.BOLD, 50));
		g.drawString("My Game",  (Game.WIDTH*Game.SCALE) / 2 - 80,(Game.HEIGHT*Game.SCALE) / 2 - 200);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 25));
		g.drawString("Continuar",  (Game.WIDTH*Game.SCALE) / 2 - 40, 180);
		g.drawString("Salvar",  (Game.WIDTH*Game.SCALE) / 2 - 40, 260);
		g.drawString("Sair",  (Game.WIDTH*Game.SCALE) / 2 - 40, 340);

		if(options[currentOption] == "continuar") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 65, 180);
		}else if(options[currentOption] == "salvar") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 65, 260);
		}else if(options[currentOption] == "sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 65, 340);
		}
	}
}
