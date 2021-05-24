/*
 * 
 * Author: Erik Gomes;
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener, KeyListener{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300, HEIGHT = 400;
	public int PLAYER = 1,OPONENTE = -1,CURRENT = PLAYER;
	
	public BufferedImage PLAYER_SPRITE,OPONENTE_SPRITE;
	public int[][] TABULEIRO = new int[3][3];

	public static boolean pressed = false;
	public static int mx;
	public static int my;
	
	public static String modo;
	public static String scene = "main";
	
	public int p1 = 0, p2 = 0;
	
	public static int turns = 0;
	
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.addMouseListener(this);
		this.addKeyListener(this);
		try {
			PLAYER_SPRITE = ImageIO.read(getClass().getResource("/player.png"));
			OPONENTE_SPRITE = ImageIO.read(getClass().getResource("/oponente.png"));
		} catch (IOException e) {
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
		CURRENT = PLAYER;
		
		if(scene == "main") {
			p1 = 0;
			p2 = 0;
		}
	}

	public void tick() {
		if(scene.equals("jogando")) {
			if(CURRENT == PLAYER) {
				if(pressed) {
					pressed = false;
					mx /= 100;
					my /= 100;
					if(TABULEIRO[mx][my] == 0) {
						TABULEIRO[mx][my] = PLAYER;
						CURRENT = OPONENTE;
					}
					
				}
			}
			
			else if(CURRENT == OPONENTE){
				if(modo == "duo") {
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
					//*ALGORÍTMO MINIMAX*//
					for(int xx = 0; xx < TABULEIRO.length; xx++) {
						for(int yy = 0; yy < TABULEIRO.length; yy++) {		
							if(TABULEIRO[xx][yy] == 0) {
								Node bestMove = getBestMove(xx, yy, 0, OPONENTE);
								
								TABULEIRO[bestMove.x][bestMove.y] = OPONENTE;
								
								CURRENT = PLAYER;
								
								return;
							}
						}
					}
				}
				
				///verificação do vencedor///
				if(checkVictory() == PLAYER) {
					resetTabuleiro();
					
					p1++;
					
				} else if(checkVictory() == OPONENTE) {
					resetTabuleiro();
					
					p2++;
					
				}else if(checkVictory() == 0) {
					resetTabuleiro();
					
					
				}
			}
		}
		else if(scene == "main") {
			if(pressed) {
				pressed = false;
				if(mx < WIDTH/2 - 6) {
					modo = "solo";
					scene = "jogando";
				}else {
					modo = "duo";
					scene = "jogando";
				}
			}
		}
	}
	
	public Node getBestMove(int x, int y, int depth, int turn) {
		if(checkVictory() == PLAYER) {
			return new Node(x,y,depth-10,depth);
		}else if(checkVictory() == OPONENTE) {
			return new Node(x,y,10-depth,depth);
		}else if(checkVictory() == -10) {
			return new Node(x,y,0,depth);
		}
		
		List<Node> nodes = new ArrayList<Node>();
		for(int xx = 0; xx < TABULEIRO.length; xx++) {
			for(int yy = 0; yy < TABULEIRO.length; yy++) {
				if(TABULEIRO[xx][yy] == 0) {
					Node node;
					if(turn == PLAYER) {
						TABULEIRO[xx][yy] = PLAYER;
						node = getBestMove(xx, yy, depth+1,OPONENTE);
						TABULEIRO[xx][yy] = 0;
					}else {
						TABULEIRO[xx][yy] = OPONENTE;
						node = getBestMove(xx, yy, depth+1, PLAYER);
						TABULEIRO[xx][yy] = 0;
					}
					nodes.add(node);
				}
			}
		}
		
		Node finalNode = nodes.get(0);
		for(int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if(turn == PLAYER) {
				if(n.score > finalNode.score) {
					finalNode = n;
				}
			}else{
				if(n.score < finalNode.score) {
					finalNode = n;
				}
			}
		}
		
		return finalNode;
	}
	
	
	public int checkVictory() {
		//*verificação do vencedor*//
		if(
		   TABULEIRO[0][0] == PLAYER && TABULEIRO[1][0] == PLAYER && TABULEIRO[2][0] == PLAYER)
			return PLAYER;
		   if(TABULEIRO[0][1] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[2][1] == PLAYER) 
			   return PLAYER;
		   if(TABULEIRO[0][2] == PLAYER && TABULEIRO[1][2] == PLAYER && TABULEIRO[2][2] == PLAYER )
			   return PLAYER;
		   if(TABULEIRO[0][0] == PLAYER && TABULEIRO[0][1] == PLAYER && TABULEIRO[0][2] == PLAYER )
			   return PLAYER;
		   if(TABULEIRO[1][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[1][2] == PLAYER )
			   return PLAYER;
		   if(TABULEIRO[2][0] == PLAYER && TABULEIRO[2][1] == PLAYER && TABULEIRO[2][2] == PLAYER )
			   return PLAYER;
		   if(TABULEIRO[0][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[2][2] == PLAYER )
			   return PLAYER;
		   if(TABULEIRO[2][0] == PLAYER && TABULEIRO[1][1] == PLAYER && TABULEIRO[0][2] == PLAYER) 
			
			return PLAYER;
			
		
		
		else if(
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
			
			return OPONENTE;
			
		}
		//*empate*//
		int cur = 0;
		for(int xx = 0; xx < TABULEIRO.length; xx++) {
			for(int yy = 0; yy < TABULEIRO.length; yy++) {
				if(TABULEIRO[xx][yy] != 0) {
					cur++;
				}
			}
		}
		if(cur == TABULEIRO.length *TABULEIRO[0].length) {
			return 0;
		}
		
		return -10;
		
	}
		
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		if(scene == "jogando") {
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
			//pontuação//
			g.setColor(Color.blue);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("X : "+p1, 25, 365);
			
			g.setColor(Color.black);
			g.drawString("||", 139, 360);
			
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("O : "+p2, 170, 365);
			////*////
		}
		if(scene == "main") {
			g.setColor(Color.white);
			g.fillRect(0, 0, WIDTH, HEIGHT);
	
			g.setFont(new Font("arial", Font.BOLD, 50));
			g.setColor(Color.red);
			g.drawString("Tic", 25, 100);
			g.setColor(Color.yellow);
			g.drawString("Tac", 95, 100);
			g.setColor(Color.blue);
			g.drawString("Toe", 175, 100);
			
			
			
			
			g.setFont(new Font("arial", Font.BOLD, 30));
			g.setColor(Color.blue);
			g.drawString("Solo", 45, 250);
			
			g.setColor(Color.black);
			for(int i = 0; i < 6; i++) {
				g.drawString("|", WIDTH/2 - 6, 130+i*50);
			}
			g.setColor(Color.red);
			g.drawString("Duo", 195, 250);
		}
		
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
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
		pressed = true;
		mx = e.getX();
		my = e.getY();
		}	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(scene == "jogando") {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				scene = "main";
				resetTabuleiro();
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
}
