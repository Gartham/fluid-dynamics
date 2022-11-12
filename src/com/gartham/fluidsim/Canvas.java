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
			System.out.println(vec);
		}

		public void push(Vector vec) {
			// Relative values.
			xfrac += vec.getX();
			yfrac += vec.getY();
		}

		@Override
		public String toString() {
			return "Particle @ (" + xfrac + ", " + yfrac + ") out of (" + Integer.MAX_VALUE + ", " + Integer.MAX_VALUE
					+ ")";
		}

	}

	public static void main(String[] args) {
		Canvas can = new Canvas();
		Particle particulate = can.new Particle(0xFF0000ff, 0, 0);

		FlowField field = new FlowField(5, 5);
		for (int i = 0; i < field.getWidth(); i++)
			for (int j = 0; j < field.getHeight(); j++)
				field.set(i, j, new Vector(i, j));

		particulate.flow(field);
	}

}
