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
			int posx = (int) ((double) xfrac / Integer.MAX_VALUE * (field.getWidth() - field.getWidth() / 2));
			int posy = (int) ((double) yfrac / Integer.MAX_VALUE * (field.getHeight() - field.getHeight() / 2));
			
			var vec = field.get(posx, posy);
		}

	}

}
