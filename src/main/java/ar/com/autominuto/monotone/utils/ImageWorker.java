package ar.com.autominuto.monotone.utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class ImageWorker {

	public ImageWorker() {

	}

	public InputStream cropAndResize(InputStream image, Integer targetWidth, Integer targetHeight) throws IOException {
//		  InputStream in = new ByteArrayInputStream(image);
		Boolean higherQuality = true;
		BufferedImage originalImage = ImageIO.read(image);
		
		int oWidth = originalImage.getWidth();
		int oHeight = originalImage.getHeight();
		
		//Ask for landscape or portrait then crop a square image
		// in the middle taking the shortest distance from the side
		if (oWidth > oHeight) {
			originalImage = originalImage.getSubimage((oWidth - oHeight) / 2, 0, oHeight, oHeight);
		} else {
			originalImage = originalImage.getSubimage(0, (oHeight - oWidth) / 2, oWidth, oWidth);
		}

		int type = (originalImage.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) originalImage;

		if (ret.getHeight() < targetHeight || ret.getWidth() < targetWidth) {
			higherQuality = false;
		}
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = originalImage.getWidth();
			h = originalImage.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ret, "png", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		return is;
	}

	public InputStream addWatermark(InputStream originalImage) {
		try {
			BufferedImage image = ImageIO.read(originalImage);
			InputStream watermarkIs = getClass().getClassLoader().getResourceAsStream("watermark.png");

			InputStream watermarkRezise = this.cropAndResize(watermarkIs, getPercent(image.getWidth(), 20f),
					getPercent(image.getHeight(), 20f));
			BufferedImage overlayRezise = ImageIO.read(watermarkRezise);

			BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_ARGB);

			// paint both images, preserving the alpha channels
			Graphics g = combined.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.drawImage(overlayRezise, 0, 0, null);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(combined, "png", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			return is;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Integer getPercent(int value, float percentage) {
		return Integer.valueOf((int) (value * (percentage / 100f)));
	}

}
