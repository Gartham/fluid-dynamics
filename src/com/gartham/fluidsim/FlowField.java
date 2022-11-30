package com.gartham.fluidsim;

import java.util.Arrays;

public class FlowField implements Flowable {
	private final Vector[][] grid;

	public int getWidth() {
		return grid.length;
	}

	public int getHeight() {
		return grid[0].length;
	}

	public FlowField(Vector[]... grid) {
		this.grid = grid;
	}

	public FlowField(int width, int height) {
		grid = new Vector[width][height];
		for (Vector[] v : grid)
			for (int i = 0; i < v.length; i++)
				v[i] = Vector.ZERO;
	}

	/**
	 * Gets the {@link Vector} at the given position in this field.
	 * 
	 * @param x The lengthwise/horizontal (width) position along the flow field,
	 *          starting from the left (<code>x=0</code> is the left).
	 * @param y The vertical (height) position along the flow field, starting from
	 *          the top (<code>y=0</code> is the top).
	 * @return The vector at that position.
	 * @throws ArrayIndexOutOfBoundsException If the indices are out of bounds for
	 *                                        this {@link FlowField}.
	 */
	public Vector get(int x, int y) throws ArrayIndexOutOfBoundsException {
		return grid[x][y];
	}

	public void set(int x, int y, Vector vec) throws ArrayIndexOutOfBoundsException, NullPointerException {
		if (vec == null)
			throw null;
		grid[x][y] = vec;
	}

//	/**
//	 * Calculates the new flow vector at the specified X,Y position. This should
//	 * only be called on real, grid coordinates (no negatives X,Y or X,Y larger than
//	 * the grid dimensions).
//	 * 
//	 * @param x
//	 * @param y
//	 */
//	private Vector fp(int x, int y) {
//		Vector v = grid[x][y];
//
//		var vec1 = new Vector(1, 1).times(f(x - 1, y - 1).add(f(x + 1, y + 1)).dot(new Vector(1, 1)));
//		var vec2 = new Vector(1, -1).times(f(x - 1, y + 1).add(f(x + 1, y - 1)).dot(new Vector(1, -1)));
//		var vec3 = new Vector(2, -2).do
//	}

	private Vector grad2(int x, int y) {
		Vector top = f(x, y + 1), tr = f(x + 1, y + 1), right = f(x + 1, y), br = f(x + 1, y - 1), bot = f(x, y - 1),
				bl = f(x - 1, y - 1), left = f(x - 1, y), tl = f(x - 1, y + 1);
		var xgrad = -4 * f(x, y).getX() - 2 * top.getX() + tr.getX() + tr.getY() + 2 * right.getX() + br.getX()
				- br.getY() - 2 * bot.getX() + bl.getX() + bl.getY() + 2 * left.getX() + tl.getX() - tl.getY();
		var ygrad = -4 * f(x, y).getY() + 2 * top.getY() + tr.getX() + tr.getY() - 2 * right.getY() + br.getY()
				- br.getX() + 2 * bot.getY() + bl.getX() + bl.getY() - 2 * left.getY() + tl.getY() - tl.getX();

		return new Vector(xgrad, ygrad);
	}

	/**
	 * Returns the gradient of the vector-divergence of the vector at the given
	 * cell.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Vector grad(int x, int y) {

		// We want to calculate the gradient, so we need to take a 3x3 convolution over
		// the cells around this cell. (This is necessary because the gradient
		// inherently involves multiple timesteps, and the effect of the vector at this
		// coordinate in the flow field cascades to neighboring cells over time.

		// Point divergences for the points around the center (x,y) coordinate.
		var tlpdiv = pointdiv(x - 1, y);
		var trpdiv = pointdiv(x, y);
		var blpdiv = pointdiv(x - 1, y - 1);
		var brpdiv = pointdiv(x, y - 1);

		var hg = trpdiv.add(brpdiv).subtract(tlpdiv).subtract(blpdiv);
		var vg = tlpdiv.add(trpdiv).subtract(blpdiv).subtract(brpdiv);

//		return new Vector(hg.mag(), vg.mag());
		return hg.add(vg);
	}

	/**
	 * Returns the divergence at a certain corner between square cells on the
	 * flowfield. The x and y coordinates supplied to this method are the x and y
	 * coordinates of the square cell that is to the bottom left of the point. This
	 * method essentially returns the divergence of the top right corner of the
	 * specified cell. (The choice of which cell in relation to the point whose
	 * divergence is desired, to use the input coordinates of, is arbitrary.)
	 * 
	 * @param blx
	 * @param bly
	 * @return
	 */
	private Vector pointdiv(int blx, int bly) {
		return f(blx, bly).negXY().add(f(blx + 1, bly).negY()).add(f(blx, bly + 1).negX()).add(f(blx + 1, bly + 1));
	}

	/**
	 * Returns the vector at location X,Y. If X,Y is negative or is larger than its
	 * respective grid dimension, it is "wrapped around" the grid.
	 * 
	 * @param x
	 * @param y
	 */
	private Vector f(int x, int y) {
		if (x < 0) {
			x %= grid.length;
			x += grid.length;
		}
		if (y < 0) {
			y %= grid[0].length;
			y += grid[0].length;
		}
		if (x >= grid.length)
			x %= grid.length;
		if (y >= grid[0].length)
			y %= grid[0].length;

		return grid[x][y];
	}

	@Override
	public void flow(FlowField field) {
		Vector[][] newGrid = new Vector[grid.length][grid[0].length];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				newGrid[i][j] = grid[i][j].add(grad2(i, j).times(.125));
		System.arraycopy(newGrid, 0, grid, 0, newGrid.length);
	}

	/**
	 * Flows this field over itself <code>count</code> times.
	 * 
	 * @param count
	 */
	public void flow(int count) {
		for (; count > 0; count--)
			flow(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (var v : grid)
			sb.append(Arrays.toString(v)).append('\n');
		return sb.toString();
	}

}
