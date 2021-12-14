package com.ltztec.entities;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.ltztec.main.Game;

public class Npc extends Entity	{

	
	public String[] frases = new String[8];
	public boolean showMassage = false;
	public int curIndexMsg = 0;
	public int fraseIndex = 0;
	
	public int time = 0;
	public int maxTime = 10;
	
	
	
	public Npc(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Oii, Seja muito bem-vindo(a) ao meu jogo!  ";
		frases[1] = "Voc� tem que destruir os inimigos!  ";
		frases[2] = "Para isso voc� tem que...  ";
		frases[3] = "pegar a arma e as muni��es...  ";
		frases[4] = "para atirar aperte espa�o...  ";
		frases[5] = "Quando voc� derrota todos os inimigos...  ";
		frases[6] = "voc� passa de fase...  ";
		frases[7] = "Divirta-se!  ";
		
	}
	
	public void tick() {
		
		int xPlayer = Game.player.getX();
 		int yPlayer = Game.player.getY();
		
		int xNpc = (int) x;
		int yNpc = (int) y;
	
		if( Math.abs(xPlayer - xNpc) < 40 &&  Math.abs(yPlayer - yNpc) < 40) {
			showMassage = true;	
		}else {
			fraseIndex = 0;
			showMassage = false;	
		}
		
		this.time++;
		
		if(showMassage) {
			if(time >= maxTime) {
				time = 0;
				if(curIndexMsg < frases[fraseIndex].length() - 1) {
					curIndexMsg++;
				}else {
					if(fraseIndex < frases.length - 1) {
						fraseIndex++;
						curIndexMsg = 0;
					}
				}
			}
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		
	}

}
