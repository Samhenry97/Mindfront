package com.mine.mindfront.input;

public class Controller {

	public double x, z, y, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = true;
	public static boolean walk = false;

	public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean sprint) {
		double rotationSpeed = 0.01;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;
		double crouchHeight = 0.3;
		double xMove = 0;
		double zMove = 0;
		
		walk = false;
		
		if (forward) {
			zMove++;
			walk = true;
		}
		if (back) {
			zMove--;
			walk = true;
		}
		if (left) {
			xMove--;
			walk = true;
		}
		if (right) {
			xMove++;
			walk = true;
		}
		if (turnLeft) {
			rotationa -= rotationSpeed;
		}
		if (turnRight) {
			rotationa += rotationSpeed;
		}
		if(jump) {
			y += jumpHeight;
			sprint = false;
		}
		
		if(crouch) {
			y -= crouchHeight;
			sprint = false;
			walkSpeed = 0.3;
		}
		if(sprint) {
			walkSpeed = 0.9;
			walk = true;
		}
		
		
		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;

		x += xa;
		z += za;

		xa *= 0.1;
		za *= 0.1;

		rotation += rotationa;
		rotationa *= 0.5;
		
		y *= 0.9;
	}

}
