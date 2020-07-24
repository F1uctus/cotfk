package com.cotfk.ui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.HashMap;
import java.util.Objects;

public class ImageTools {
    public static BufferedImage flipX(BufferedImage image) {
        var newImage = deepCopy(image);
        var tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-newImage.getWidth(null), 0);
        var op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(newImage, null);
    }

    private static class ResizeArgs {
        BufferedImage image;
        int h;
        int w;

        public ResizeArgs(BufferedImage image, int h, int w) {
            this.image = image;
            this.h = h;
            this.w = w;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ResizeArgs that = (ResizeArgs) o;
            return h == that.h &&
                w == that.w &&
                image.equals(that.image);
        }

        @Override
        public int hashCode() {
            return Objects.hash(image, h, w);
        }
    }

    private static final HashMap<ResizeArgs, BufferedImage> resizeTileCache = new HashMap<>();

    public static BufferedImage resizeTile(BufferedImage tile, int h, int w) {
        var args = new ResizeArgs(tile, h, w);
        var cached = resizeTileCache.getOrDefault(args, null);
        if (cached != null) {
            return cached;
        }
        var generated = resize(tile, h, w);
        resizeTileCache.put(args, generated);
        return generated;
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    public static boolean areEqual(BufferedImage imgA, BufferedImage imgB) {
        if (imgA == null || imgB == null)
            return false;
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight())
            return false;

        int width = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
