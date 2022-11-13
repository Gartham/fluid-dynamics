package com.gartham.fluidsim;

public class Canvas {
	public class Particle implements Flowable {
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

	}

	public static void main(String[] args) throws InterruptedException {
		Canvas can = new Canvas();
		Particle particulate = can.new Particle(0xFF0000ff, 0, 0);

		FlowField field = new FlowField(5, 5);
		for (int i = 0; i < field.getWidth(); i++)
			for (int j = 0; j < field.getHeight(); j++)
				field.set(i, j, new Vector(i, j));

		while (true) {
			Thread.sleep(1000);
			particulate.flow(field);
		}
	}

}
