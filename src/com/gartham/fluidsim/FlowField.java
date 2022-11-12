package com.gartham.fluidsim;

public class FlowField {
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
	Vector get(int x, int y) throws ArrayIndexOutOfBoundsException {
		return grid[x][y];
	}

}
