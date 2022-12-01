package com.gartham.fluidsim;

import java.util.List;

import com.gartham.fluidsim.Canvas.Particle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JFXRenderer extends Application {

	private static final int IMAGE_WIDTH = 300, IMAGE_HEIGHT = IMAGE_WIDTH, VIEWPORT_SCALE_FACTOR = 2;

	// Shading density thresholds:

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		WritableImage img = new WritableImage(IMAGE_WIDTH, IMAGE_HEIGHT);
		ImageView view = new ImageView(img);
		view.setFitWidth(IMAGE_WIDTH * VIEWPORT_SCALE_FACTOR);
		view.setFitHeight(IMAGE_HEIGHT * VIEWPORT_SCALE_FACTOR);
		Scene s = new Scene(new Pane(view));

		Canvas c = new Canvas();
		for (int i = 0; i < 100; i++)
			c.new Particle(0xFF, (int) (Math.random() * Integer.MAX_VALUE), (int) (Math.random() * Integer.MAX_VALUE));

		FlowField field = new FlowField(50, 50);
		for (int i = 0; i < 5; i++)
			field.set((int) (Math.random() * 50), (int) (Math.random() * 50),
					new Vector(Math.random() * 20 - 10, Math.random() * 20 - 10));

		write(c, img.getPixelWriter(), field);

		Thread looper = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				c.flow(field);
				write(c, img.getPixelWriter(), field);
				field.flow(field);

			}
		});

		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void write(Canvas c, PixelWriter writer, FlowField field) {

		for (int i = 0; i < field.getWidth(); i++)
			for (int j = 0; j < field.getHeight(); j++) {
				
			}

		var particlemap = c.particlemap(IMAGE_WIDTH, IMAGE_HEIGHT);
		for (int i = 0; i < particlemap.length; i++)
			for (int j = 0; j < particlemap[i].length; j++) {
				List<Particle> list = particlemap[i][j];
				if (list == null)
					writer.setColor(i, j, Color.TRANSPARENT);
				else
					writer.setColor(i, j, new Color((list.get(0).getColor() >>> 24) / 256d,
							(list.get(0).getColor() >>> 16 & 0xff) / 256d, (list.get(0).getColor() >>> 8 & 0xff) / 256d,
							((list.get(0).getColor() & 0xff)) / 256d));
			}
	}

}
