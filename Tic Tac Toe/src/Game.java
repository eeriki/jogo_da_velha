/*
 * 
 * Author: Guilherme Grillo
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener{

	
	public static final int WIDTH = 300, HEIGHT = 400;
	public int PLAYER = 1,OPONENTE = -1,CURRENT = PLAYER;
	
	public BufferedImage PLAYER_SPRITE,OPONENTE_SPRITE;
	public int[][] TABULEIRO = new int[3][3];

	public boolean pressed = false;
	public int mx, my;
	
	public String modo = "coop";
	
	//*/if coop mode/*//
	public boolean p1vic = false;
	public boolean p2vic = false;
	public int p1 = 0, p2 = 0;
	public boolean empate = false;
	//*//*//
	
	public boolean vitoria = false, derrota = false;
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.addMouseListener(this);
		try {
			PLAYER_SPRITE = ImageIO.read(getClass().getResource("/player.png"));
			OPONENTE_SPRITE = ImageIO.read(getClass().getResource("/oponente.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resetTabuleiro();
	}
	
	public void resetTabuleiro() {
		for(int xx = 0; xx < TABULEIRO.length; xx++) {
			for(int yy = 0; yy < TABULEIRO.length; yy++) {
				TABULEIRO[xx][yy] = 0;
			}
		}
	}

	public void tick() {
		if(CURRENT == PLAYER) {
			if(pressed) {
				pressed = false;
				mx /= 100;
				my /= 100;
				if(TABULEIRO[mx][my] == 0) {
					TABULEIRO[mx][my] = PLAYER;
					CURRENT = OPONENTE;
				}
				if(
				   //horizontal//
				   TABULEIRO[0][0] == PLAYER && TABULEIRO[1][0] == PLAYER && TABULEIRO[2][0] == PLAYER ||
				   TABULEIRO[0][1] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[2][1] == PLAYER ||
				   TABULEIRO[0][2] == PLAYER && TABULEIRO[1][2] == PLAYER && TABULEIRO[2][2] == PLAYER ||
				   //vertical//
				   TABULEIRO[0][0] == PLAYER && TABULEIRO[0][1] == PLAYER && TABULEIRO[0][2] == PLAYER ||
				   TABULEIRO[1][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[1][2] == PLAYER ||
				   TABULEIRO[2][0] == PLAYER && TABULEIRO[2][1] == PLAYER && TABULEIRO[2][2] == PLAYER ||
				   //diagonal//
				   TABULEIRO[0][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[2][2] == PLAYER ||
				   TABULEIRO[2][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[0][2] == PLAYER) {
					if(modo == "coop") {
						p1vic = true;
						if(p1vic) {
							p1+=1;
							resetTabuleiro();
						}
					}else if(modo == "solo"){
						vitoria = true;
						if(vitoria) {
							p1+=1;
							resetTabuleiro();
						}
					}
				}
				//*/
			}
		}
		
		else if(CURRENT == OPONENTE){
			if(modo == "coop") {
				if(pressed) {
					pressed = false;
					mx /= 100;
					my /= 100;
					if(TABULEIRO[mx][my] == 0) {
						TABULEIRO[mx][my] = OPONENTE;
						CURRENT = PLAYER;
					}
				}
			} else if(modo == "solo"){
				
			}
			if(
			   //horizontal//
			   TABULEIRO[0][0] == OPONENTE && TABULEIRO[1][0] == OPONENTE && TABULEIRO[2][0] == OPONENTE ||
			   TABULEIRO[0][1] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO[2][1] == OPONENTE ||
			   TABULEIRO[0][2] == OPONENTE && TABULEIRO[1][2] == OPONENTE && TABULEIRO[2][2] == OPONENTE ||
			   //vertical//
			   TABULEIRO[0][0] == OPONENTE && TABULEIRO[0][1] == OPONENTE && TABULEIRO[0][2] == OPONENTE ||
			   TABULEIRO[1][0] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO[1][2] == OPONENTE ||
			   TABULEIRO[2][0] == OPONENTE && TABULEIRO[2][1] == OPONENTE && TABULEIRO[2][2] == OPONENTE ||
			   //diagonal//
			   TABULEIRO[0][0] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO[2][2] == OPONENTE ||
			   TABULEIRO[2][0] == OPONENTE && TABULEIRO[1][1] == OPONENTE && TABULEIRO[0][2] == OPONENTE) {
				if(modo == "coop") {
					p2vic = true;
					if(p2vic) {
						p2+=1;
						resetTabuleiro();
					}
				}else if(modo == "solo"){
					derrota = true;
					if(derrota) {
						p2+=1;
						resetTabuleiro();
					}
				}
			}
		}
		
		if(
		   //horizontal//
		   TABULEIRO[0][0] != 0 && TABULEIRO[1][0] != 0 && TABULEIRO[2][0] != 0 &&
		   TABULEIRO[0][1] != 0 && TABULEIRO[1][1] != 0 && TABULEIRO[2][1] != 0 &&
		   TABULEIRO[0][2] != 0 && TABULEIRO[1][2] != 0 && TABULEIRO[2][2] != 0 &&
		   //vertical//
		   TABULEIRO[0][0] != 0 && TABULEIRO[0][1] != 0 && TABULEIRO[0][2] != 0 &&
		   TABULEIRO[1][0] != 0 && TABULEIRO[1][1] != 0 && TABULEIRO[1][2] != 0 &&
		   TABULEIRO[2][0] != 0 && TABULEIRO[2][1] != 0 && TABULEIRO[2][2] != 0 &&
		   //diagonal//
		   TABULEIRO[0][0] != 0 && TABULEIRO[1][1] != 0 && TABULEIRO[2][2] != 0 &&
		   TABULEIRO[2][0] != 0 && TABULEIRO[1][1] != 0 && TABULEIRO[0][2] != 0)
		{
			empate = true;
			if(empate) {
				empate = false;
			}
			resetTabuleiro();
		}
		
	}

	
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH,HEIGHT);
		
		for(int xx = 0; xx < TABULEIRO.length; xx++) {
			for(int yy = 0; yy < TABULEIRO.length; yy++) {
				g.setColor(Color.black);
				g.drawRect(xx*100, yy*100, 100,100);
				if(TABULEIRO[xx][yy] == PLAYER) {
					g.drawImage(PLAYER_SPRITE,xx*100 + 25, yy*100 + 25, 50, 50,null);
				}else if(TABULEIRO[xx][yy] == OPONENTE) {
					g.drawImage(OPONENTE_SPRITE,xx*100 + 25, yy*100 + 25, 50, 50,null);
				}
			}
		}
		
		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("X : "+p1, 25, 365);
		
		g.setColor(Color.black);
		g.drawString("||", 139, 360);
		
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		g.drawString("O : "+p2, 170, 365);
			
		g.dispose();
		bs.show();
	}

	
	
	public static void main(String args[]) {
		Game game = new Game();
		JFrame frame = new JFrame("Tic Tac Toe");
		frame.add(game);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		new Thread(game).start();
	}
	
	@Override
	public void run() {
		
		while(true) {
			tick();
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		mx = e.getX();
		my = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
