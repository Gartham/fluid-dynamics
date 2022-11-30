package com.gartham.fluidsim;

import java.util.HashSet;
import java.util.Set;

public class Canvas implements Flowable {

	private final Set<Particle> particles = new HashSet<>();

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

			System.out.println(vec + " APPLIED TO " + this);
			push(vec);
		}

		public void push(Vector vec) {
			// Relative values.
			xfrac += vec.getX();
			yfrac += vec.getY();
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

	public static void main(String[] args) throws InterruptedException {
		Canvas can = new Canvas();
		Particle particulate = can.new Particle(0xFF0000ff, 0, 0);

		FlowField field = new FlowField(1000, 1000);
		for (int i = 0; i < field.getWidth(); i++)
			for (int j = 0; j < field.getHeight(); j++)
				field.set(i, j, new Vector(i * 200, j * 15000));

		while (true) {
			Thread.sleep(1000);
			particulate.flow(field);
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
