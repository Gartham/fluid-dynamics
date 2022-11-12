package com.gartham.fluidsim;

public class Vector {
	private final double x, y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double dot(Vector other) {
		return x * other.x + y * other.y;
	}

	public double mag() {
		return x == 0 ? y : y == 0 ? x : Math.sqrt(dot(this));
	}

}
