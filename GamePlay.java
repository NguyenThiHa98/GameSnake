package snakeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePlay extends JPanel implements Runnable, KeyListener{
	public static final int wight = 400;
	public static final int height = 400;
	
	private Graphics2D g2d;
	private BufferedImage image;
	
	private Thread thread;
	private boolean running;
	private long time;
	
	private final int sizeS = 10;
	Entity head, apple;
	ArrayList<Entity> snake;
	private int score;
	private int level;
	private boolean gameover;
	
	private int dx, dy;
	private boolean up, down, right, left, start;
	
	public GamePlay() {
		setPreferredSize(new Dimension(wight, height));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	private void setGame(int s) {
		time = 1000/s;
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		if(k == KeyEvent.VK_UP) {
			up = true;
		}
		
		if(k == KeyEvent.VK_DOWN) {
			down = true;
		}
		if(k == KeyEvent.VK_LEFT) {
			left = true;
		}
		if(k == KeyEvent.VK_RIGHT) {
			right = true;
		}
		if(k == KeyEvent.VK_ENTER) {
			start = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		if(k == KeyEvent.VK_UP) {
			up = false;
		}
		
		if(k == KeyEvent.VK_DOWN) {
			down = false;
		}
		if(k == KeyEvent.VK_LEFT) {
			left = false;
		}
		if(k == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if(k == KeyEvent.VK_ENTER) {
			start = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void run() {
		if(running) {
			return;
		}
		init();
		long startTime;
		long elapsed;
		long wait;
		while(running) {
			startTime = System.nanoTime();
			update();
			requestPaint();
			
			elapsed = System.nanoTime() - startTime;
			wait = time - elapsed/1000000;
			if(wait > 0) {
				try {
					Thread.sleep(wait);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void init() {
		image = new BufferedImage(wight, height, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUpLevel();
		gameover = false;
		level = 1;
		setGame(level*10);
	}
	
	private void setUpLevel() {
		snake = new ArrayList<Entity>();
		head = new Entity(sizeS);
		head.setPosition(wight/2, height/2);
		snake.add(head);
		
		for (int i = 0; i < 3; i++) {
			Entity e = new Entity(sizeS);
			e.setPosition(head.getX() + (i*sizeS), head.getY());
			snake.add(e);
		}
		apple = new Entity(sizeS);
		setApple();
		score = 0;
	}
	public void setApple() {
		int x = (int) (Math.random()*(wight - sizeS));
		int y = (int) (Math.random()*(height - sizeS));
		x = x - (x%sizeS);
		y = y - (y%sizeS);
		
		
		apple.setPosition(x, y);
	}
	private void requestPaint() {
		paint(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	private void update() {
		if(gameover) {
			if(start) {
				setUpLevel();
			}
			return;
		}
		if(up && dy == 0) {
			dy = -sizeS;
			dx = 0;
		}
		if(down && dy == 0) {
			dy = sizeS;
			dx = 0;
		}
		if(left && dx == 0) {
			dy = 0;
			dx = - sizeS;
		}
		if(right && dx == 0) {
			dy = 0;
			dx = sizeS;
		}
		if(dx != 0 || dy != 0) {
			for (int i = snake.size() - 1 ;  i > 0; i --) {
				snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
			}
			head.move(dx, dy);
			
		}
		
		for( Entity e : snake) {
			if(e.isCollsion(head)) {
				gameover = true;
				break;
			}
		}
		
		if(apple.isCollsion(head)) {
			score++;
			setApple();
			
			Entity e = new Entity(sizeS);
			e.setPosition(-100, -100);
			snake.add(e);
			if(score % 10 == 0) {
				level++;
				if(level > 10) {
					level = 10;
				}
				setGame(level*10);
			}
		}
		
		if(head.getX() < 0) {
			head.setX(wight);
		}
		if(head.getY() < 0) {
			head.setY(height);
		}
		if(head.getX() > wight) {
			head.setX(0);
		}
		if(head.getY() > height) {
			head.setX(0);
		}
		
	}
	public void paint(Graphics2D g2d) {
		g2d.clearRect(0, 0, wight, height);
		
		g2d.setColor(Color.BLUE);
		for(Entity e : snake) {
			e.paint(g2d);
		}
		
		g2d.setColor(Color.RED);
		apple.paint(g2d);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score: "+score, 10, 10);
	}

}
