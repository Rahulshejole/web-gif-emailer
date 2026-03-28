package com.mycompany.gifemailer;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GifGenerator {

    public void createGifFromImages(String[] imagePaths, String outputGifPath, int defaultDelay, boolean shouldLoop, String style, boolean isPreview) throws IOException {
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        
        try (OutputStream os = Files.newOutputStream(Paths.get(outputGifPath))) {
            encoder.start(os);
            encoder.setRepeat(shouldLoop ? 0 : -1);
            
            // 1. Load Images
            List<BufferedImage> originalFrames = new ArrayList<>();
            for (String path : imagePaths) {
                BufferedImage img = ImageIO.read(new File(path));
                // If preview, resize to 250px width to save memory/speed
                if (isPreview) {
                    img = resizeImage(img, 250);
                }
                originalFrames.add(img);
            }

            // 2. Apply Style Logic
            List<BufferedImage> finalFrames = new ArrayList<>();
            int delay = defaultDelay;

            switch (style.toLowerCase()) {
                case "fast":
                    delay = 150;
                    finalFrames.addAll(originalFrames);
                    break;
                case "slow":
                    delay = 1000;
                    finalFrames.addAll(originalFrames);
                    break;
                case "boomerang":
                    delay = 300;
                    finalFrames.addAll(originalFrames);
                    for (int i = originalFrames.size() - 2; i > 0; i--) {
                        finalFrames.add(originalFrames.get(i));
                    }
                    break;
                case "reverse":
                    delay = 500;
                    List<BufferedImage> rev = new ArrayList<>(originalFrames);
                    Collections.reverse(rev);
                    finalFrames.addAll(rev);
                    break;
                case "slideshow":
                    delay = 2000; // Very slow
                    finalFrames.addAll(originalFrames);
                    break;
                case "stutter":
                    delay = 200;
                    for (BufferedImage img : originalFrames) {
                        finalFrames.add(img); // A
                        finalFrames.add(img); // A
                        finalFrames.add(img); // A
                    }
                    break;
                case "flash":
                    delay = 300;
                    BufferedImage whiteFrame = createColorFrame(originalFrames.get(0).getWidth(), originalFrames.get(0).getHeight(), Color.WHITE);
                    for (BufferedImage img : originalFrames) {
                        finalFrames.add(img);
                        finalFrames.add(whiteFrame);
                    }
                    break;
                case "blink":
                    delay = 400;
                    BufferedImage blackFrame = createColorFrame(originalFrames.get(0).getWidth(), originalFrames.get(0).getHeight(), Color.BLACK);
                    for (BufferedImage img : originalFrames) {
                        finalFrames.add(img);
                        finalFrames.add(blackFrame);
                    }
                    break;
                case "rewind":
                    // Forward normal, backward FAST
                    finalFrames.addAll(originalFrames);
                    // We need dynamic delay for this, but GIF lib uses one delay per set. 
                    // Simulating by repeating forward frames and single backward frames is tricky.
                    // Simplified: Just add backward frames.
                    List<BufferedImage> back = new ArrayList<>(originalFrames);
                    Collections.reverse(back);
                    finalFrames.addAll(back);
                    delay = 100; // Fast overall
                    break;
                case "chaos":
                    delay = 250;
                    finalFrames.addAll(originalFrames);
                    finalFrames.addAll(originalFrames);
                    Collections.shuffle(finalFrames);
                    break;
                case "standard":
                default:
                    delay = 500;
                    finalFrames.addAll(originalFrames);
                    break;
            }

            encoder.setDelay(delay);

            // 3. Write Frames
            for (BufferedImage frame : finalFrames) {
                encoder.addFrame(frame);
            }
            
            encoder.finish();
        }
    }

    // Helper: Resize for Previews
    private BufferedImage resizeImage(BufferedImage original, int targetWidth) {
        int targetHeight = (int) (original.getHeight() * ((double) targetWidth / original.getWidth()));
        Image resultingImage = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
        BufferedImage output = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = output.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return output;
    }

    // Helper: Create Blank Frames
    private BufferedImage createColorFrame(int width, int height, Color color) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return img;
    }
}