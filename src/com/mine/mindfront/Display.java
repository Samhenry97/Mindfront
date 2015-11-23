package com.mine.mindfront;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.mine.mindfront.graphics.Screen;
import com.mine.mindfront.input.Controller;
import com.mine.mindfront.input.InputHandler;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final String TITLE = "Mindfront";
	
	private Thread thread;
	private boolean running = false;
	private BufferedImage img;
	private Screen screen;
	private int[] pixels;
	private Game game;
	private InputHandler input;
	private int newX = 0;
	private int newY = 0;
	private int oldX = 0;
	private int oldY = 0;
	private int fps;
	
	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		screen = new Screen(WIDTH, HEIGHT);
		game = new Game();
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}
	
	private void start() {
		if(running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	@SuppressWarnings("unused")
	private void stop() {
		if(!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void run() {
		requestFocus();
		int frames = 0;
		double unProcessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1/60.0;
		int tickCount = 0;
		boolean ticked = false;
		while(running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unProcessedSeconds += passedTime/1000000000.0;
			
			while(unProcessedSeconds > secondsPerTick) {
				tick();
				unProcessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if(tickCount % 60 == 0) {
					fps = frames;
					previousTime += 1000;
					frames = 0;
				}
			}
			if(ticked) {
				render();
				frames++;
			}
			render();
			frames++;
			
			newX = InputHandler.mouseX;
			newY = InputHandler.mouseY;
			
			if(newX > oldX) {
				Controller.turnRight = true;
			} else if(newX < oldX) {
				Controller.turnLeft = true;
			} else {
				Controller.turnLeft = false;
				Controller.turnRight = false;
			}
			if(oldY == 5);
			oldX = newX;
			oldY = newY;
		}
	}
	
	private void tick() {
		game.tick(input.key);
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.render(game);
		
		for(int i = 0; i < WIDTH * HEIGHT; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
		g.setFont(new Font("Verdana", Font.ITALIC, 50));
		g.setColor(Color.YELLOW);
		g.drawString(fps + " fps", 20, 50);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.pack();
		frame.setTitle(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.pack();
		game.start();
	}
	
}
