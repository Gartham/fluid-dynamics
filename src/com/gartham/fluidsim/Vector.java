package com.gartham.fluidsim;

public class Vector {
	private final double x, y;

	public static final Vector ZERO = new Vector(0, 0);

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

	public Vector norm() {
		double mag = mag();
		return mag != 1 ? new Vector(x / mag, y / mag) : this;
	}

	@Override
	public String toString() {
		return "Vector <" + x + ", " + y + '>';
	}

	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y);
	}

	/**
	 * Performs <code>this - other</code>. Subtracts the specified,
	 * <code>other</code> vector, from this one and returns the result.
	 * 
	 * @param other
	 * @return
	 */
	public Vector subtract(Vector other) {
		return new Vector(x - other.x, y - other.y);
	}

	public Vector times(double scale) {
		return new Vector(x * scale, y * scale);
	}

	public Vector negX() {
		return new Vector(-x, y);
	}

	public Vector negY() {
		return new Vector(x, -y);
	}

	public Vector negXY() {
		return new Vector(-x, -y);
	}

}
