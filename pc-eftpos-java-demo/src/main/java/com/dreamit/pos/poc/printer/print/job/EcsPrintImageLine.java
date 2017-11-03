package com.dreamit.pos.poc.printer.print.job;

import com.dreamit.pos.poc.printer.constants.EcsPrintContants;
import com.dreamit.pos.poc.printer.port.PrinterPort;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sherman_sun on 30/10/17.
 */
public class EcsPrintImageLine extends EcsPrintLine{

	private Image image;


	public EcsPrintImageLine(InputStream inputStream) throws IOException {
		loadImage(inputStream);
	}

	public EcsPrintImageLine(File file) throws IOException {
		loadImage(file);
	}

	@Override
	public void print(PrinterPort printerPort) {

		specifyAlign(printerPort, this.align);
		BufferedImage bufferedImage = (BufferedImage) this.image;
		int [][] pixels = getPixels(bufferedImage);

		printImage(printerPort, pixels);

	}

	private int[][] getPixels(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[height][width];
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[row][col] = image.getRGB(col, row);
			}
		}

		return result;
	}

	private void printImage(PrinterPort printerPorts, int [][] pixels){

		printerPorts.writeBytes(EcsPrintContants.SET_LINE_SPACE_24);
		for (int y = 0; y < pixels.length; y+=24){

			printerPorts.writeBytes(EcsPrintContants.SELECT_BIT_IMAGE_MODE);
			printerPorts.writeBytes(new byte[]{(byte) (0x00ff & pixels[y].length), (byte) ((0xff00 & pixels[y].length) >> 8)});
			for (int x = 0; x < pixels[y].length; x++){
				printerPorts.writeBytes(collectSlice(y, x, pixels));
			}
			printerPorts.writeBytes(EcsPrintContants.LINE_FEED);
		}
		printerPorts.writeBytes(EcsPrintContants.SET_LINE_SPACE_30);
	}

	private void loadImage(InputStream inputStream) throws IOException {

		image = ImageIO.read(inputStream);

	}

	private void loadImage(File file) throws IOException {
		image = ImageIO.read(file);
	}

	private byte[] collectSlice(int y, int x, int[][] img) {
		byte[] slices = new byte[]{0, 0, 0};
		for (int yy = y, i = 0; yy < y + 24 && i < 3; yy += 8, i++) {// va a hacer 3 ciclos
			byte slice = 0;
			for (int b = 0; b < 8; b++) {
				int yyy = yy + b;
				if (yyy >= img.length) {
					continue;
				}
				int col = img[yyy][x];
				boolean v = shouldPrintColor(col);
				slice |= (byte) ((v ? 1 : 0) << (7 - b));
			}
			slices[i] = slice;
		}

		return slices;
	}

	private boolean shouldPrintColor(int color) {
		final int threshold = 127;
		int a, r, g, b, luminance;
		a = (color >> 24) & 0xff;
		if (a != 0xff) { // ignore pixels with alpha channel
			return false;
		}
		r = (color >> 16) & 0xff;
		g = (color >> 8) & 0xff;
		b = color & 0xff;

		luminance = (int) (0.299 * r + 0.587 * g + 0.114 * b);

		return luminance < threshold;
	}
}
