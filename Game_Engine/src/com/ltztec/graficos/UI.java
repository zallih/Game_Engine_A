package com.ltztec.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.ltztec.entities.Npc;
import com.ltztec.main.Game;

public class UI {


	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(10, 10, 50, 8);
		g.setColor(Color.green);
		g.fillRect(10, 10, (int)((Game.player.life/Game.player.maxLife)*50), 8);
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD,8));
		g.drawString((int)Game.player.life+"/"+Game.player.maxLife, 18, 17);
	
	
		if(Game.npc.showMassage == true) {
			
			g.setColor(Color.white);
			g.fillRect(90, 240, 220, 30);
			
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 9));
			g.drawString(Game.npc.frases[Game.npc.fraseIndex].substring(0, Game.npc.curIndexMsg), 103, 259);
		}
	}
}
