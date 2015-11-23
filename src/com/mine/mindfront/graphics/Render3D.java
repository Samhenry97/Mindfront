package com.mine.mindfront.graphics;

import com.mine.mindfront.Game;
import com.mine.mindfront.input.Controller;

public class Render3D extends Render {
	
	public double[] zBuffer;
	public double renderDistance = 3000;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
	}

	public void floor(Game game) {
		
		double floorPos = 8;
		double ceilPos = 8;
		
		double forward = game.controls.z;
		double right = game.controls.x;
		double up = game.controls.y;
		double walking = Math.sin(game.time / 6.0) * 0.5;
		
		double rotation = game.controls.rotation;
		double cosine = Math.cos(rotation);
		double sine = Math.sin(rotation);
		
		if(!Controller.walk) {
			walking = 0;
		}
		for (int y = 0; y < height; y++) {
			double ceiling = (y + -height / 2.0) / height;
			
			double z = (floorPos + up + walking) / ceiling;
			
			if(ceiling < 0) {
				z = (ceilPos - up - walking) / -ceiling;
			}
			
			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right);
				int yPix = (int) (yy + forward);
				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * Texture.floor.width];
				
				if(z > 500) {
					pixels[x + y * width] = 0;
				}
			}
		}
	}
	
	public void renderDistanceLimiter() {
		for(int i = 0; i < width  * height; i++) {
			int brightness = (int) (renderDistance / zBuffer[i]);
			
			if(brightness < 0) brightness = 0;
			if(brightness > 255) brightness = 255;
			
			int r = (pixels[i] >> 16) & 0xff;
			int g = (pixels[i] >> 8) & 0xff;
			int b = pixels[i] & 0xff;
			
			r = r * brightness >>> 8;
			g = g * brightness >>> 8;
			b = b * brightness >>> 8;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}

}
