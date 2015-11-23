package com.mine.mindfront.graphics;

public class Render {
	public final int width;
	public final int height;
	public final int[] pixels;

	public Render(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public void draw(Render render, int xOffs, int yOffs) {
		for(int y = 0; y < render.height; y++) {
			int yPix = y + yOffs;
			if(yPix < 0 || yPix >= height) {
				continue;
			}
			
			for(int x = 0; x < render.width; x++) {
				int xPix = x + xOffs;
				if(xPix < 0 || xPix >= width) {
					continue;
				}
				
				int alpha = render.pixels[x + y * render.width];
				if(alpha > 0) {
					pixels[xPix + yPix * width] = alpha;
				}
			}
		}
	}
	
}
