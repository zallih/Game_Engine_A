package com.ltztec.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
	}
}
