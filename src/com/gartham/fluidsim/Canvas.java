package com.gartham.fluidsim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Canvas implements Flowable {

	private final Set<Particle> particles = new HashSet<>();

	/**
	 * <p>
	 * The width and height of the canvas, used <i>only</i> for flowing particles.
	 * The actual resolution of the particles positions is integer values, from
	 * {@link Integer#MIN_VALUE} (left/bottom of canvas) to
	 * {@link Integer#MAX_VALUE} (right/top of canvas), where 0,0 is right in the
	 * middle of the canvas. Whenever flowing is done on particles, the vectors in
	 * the flow field have normal values (like 10, or 5, not
	 * {@link Integer#MAX_VALUE} / 12, or something), so the canvas uses a faux
	 * width/height to scale the actual position increments of the particles.
	 * </p>
	 * <p>
	 * If the width of the canvas is 11, and a pixel at the very left (at position
	 * -5 on the canvas, but {@link Integer#MIN_VALUE} in particle-coordinates) is
	 * moved by a vector &lt;5,0&gt;, then the particle will be moved to the
	 * horizontal center of the canvas (at position 0 on the canvas, and position 0
	 * in particle-coordinates). If the particle is then moved by a vector
	 * &lt;3,0&gt;, then the particle will be at 3,0 in canvas coordinates and
	 * {@link Integer#MAX_VALUE}/5*3,0 in pixel coordinates.
	 * </p>
	 * <p>
	 * These values are used by the {@link Particle#push(Vector)} method.
	 * </p>
	 */
	private int width, height;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public class Particle implements Flowable {
		{
			particles.add(this);
		}
		private int color, xfrac, yfrac;

		public Particle(int color, int xfrac, int yfrac) {
			this.color = color;
			this.xfrac = xfrac;
			this.yfrac = yfrac;
		}

		@Override
		public void flow(FlowField field) {
			int posx = (int) (((double) xfrac / Integer.MAX_VALUE + 1) * field.getWidth() / 2);
			int posy = (int) (((double) yfrac / Integer.MAX_VALUE + 1) * field.getHeight() / 2);

			var vec = field.get(posx, posy);

			push(vec);
		}

		public void push(Vector vec) {
			// Relative values.
			xfrac = (int) Math.round((double) xfrac * width + vec.getX() / width * Integer.MAX_VALUE);
			yfrac = (int) Math.round((double) yfrac * height + vec.getY() / height * Integer.MAX_VALUE);
		}

		@Override
		public String toString() {
			return "Particle @ (" + xfrac + ", " + yfrac + ") out of (" + Integer.MAX_VALUE + ", " + Integer.MAX_VALUE
					+ ')';
		}

		/**
		 * Moves this particle by the ratios specified. A ratio of <code>1</code> moves
		 * the particle one full length (for the x-axis) and one full height (for the
		 * y-axis). The ratio properties specified in a call to this method represent
		 * the fractions of the canvas that the particle will move. For moving the
		 * particle in absolute units (<code>1/{@link Integer#MAX_VALUE}</code> of the
		 * canvas&#8211;sized steps), use {@link #moveByAbsolute(int, int)}.
		 * 
		 * 
		 * @param xRatio The proportion of the length of the canvas to move (rightwards,
		 *               along the x axis).
		 * @param yRatio The proportion of the height of the canvas to move (downwards,
		 *               along the y axis).
		 */
		public void moveByRelative(double xRatio, double yRatio) {
			xfrac += xRatio * Integer.MAX_VALUE;
			yfrac += yRatio * Integer.MAX_VALUE;
		}

		public void moveByAbsolute(int unitXSteps, int unitYSteps) {
			xfrac += unitXSteps;
			yfrac += unitYSteps;
		}

		public int getColor() {
			return color;
		}

	}

	/**
	 * <p>
	 * Returns a grid, of size <code>width x height</code>, where each cell contains
	 * a value representing the number of particles in its position.
	 * </p>
	 * <p>
	 * This method divides up the {@link Canvas} into <code>width x height</code>
	 * cells and then returns a 2D int array where each int in the array represents
	 * the number of particles at that cell.
	 * </p>
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public int[][] frequencymap(int width, int height) {
		var map = new int[width][height];
		for (Particle p : particles)
			map[p.xfrac / width][p.yfrac / height]++;
		return map;
	}

	public List<Particle>[][] particlemap(int width, int height) {
		@SuppressWarnings("unchecked")
		List<Particle>[][] map = (List<Particle>[][]) new List<?>[width][height];
		for (Particle p : particles) {
			int posx = (int) ((long) p.xfrac * width / Integer.MAX_VALUE),
					posy = (int) ((long) p.yfrac * height / Integer.MAX_VALUE);
			if (map[posx][posy] == null)
				map[posx][posy] = new ArrayList<>(1);
			map[posx][posy].add(p);
		}
		return map;
	}

	public static void main(String[] args) throws InterruptedException {
		Canvas can = new Canvas();
		Particle particulate = can.new Particle(0xFF0000ff, 0, 0);

		FlowField field = new FlowField(8, 5);
		field.set(4, 3, new Vector(Math.round(Math.random() * 10 - 5), Math.round(Math.random() * 10 - 5)));
		field.set(4, 2, new Vector(Math.round(Math.random() * 10 - 5), Math.round(Math.random() * 10 - 5)));
		field.set(3, 3, new Vector(Math.round(Math.random() * 10 - 5), Math.round(Math.random() * 10 - 5)));
		field.set(3, 2, new Vector(Math.round(Math.random() * 10 - 5), Math.round(Math.random() * 10 - 5)));
//		for (int i = 0; i < 2; i++)
//			for (int j = 0; j < 2; j++)
//				field.set(i, j, new Vector(Math.random()*3-1, Math.random()*3-1));

		while (true) {
			System.out.println(field + "\n\n\n");
			Thread.sleep(1000);
//			particulate.flow(field);
			long start = System.nanoTime();
			field.flow(50);
			System.out.println(System.nanoTime() - start + "ns");

		}
	}

	/**
	 * Gets the set of particles contained in this {@link Canvas}. The set is
	 * modifiable, but only {@link Particle}s in this canvas should be added to it.
	 * 
	 * @return The {@link Set} of particles in this {@link Canvas}.
	 */
	public Set<Particle> getParticles() {
		return particles;
	}

	@Override
	public void flow(FlowField field) {
		for (Particle p : particles)
			p.flow(field);
	}

}
