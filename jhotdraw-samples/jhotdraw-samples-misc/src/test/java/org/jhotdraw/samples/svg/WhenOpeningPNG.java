package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.samples.svg.figures.SVGImageFigure;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WhenOpeningPNG extends Stage<WhenOpeningPNG> {
    @ExpectedScenarioState
    private Drawing drawing;

    @ExpectedScenarioState
    private File imageFile;

    public WhenOpeningPNG the_user_opens_the_png_file() {
        SVGImageFigure figure = new SVGImageFigure();
        try {
            figure.loadImage(imageFile);
            drawing.add(figure);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load PNG in When stage", e);
        }
        return this;
    }

    public WhenOpeningPNG the_user_edits_the_image_and_saves() {
        // Simulate an edit, change an attribute of the first figure
        Figure figure = drawing.getChildren().iterator().next();
        figure.set(AttributeKeys.TRANSFORM, new AffineTransform());

        // Simulate the save action to the existing file
        try {
            BufferedImage updatedImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(updatedImage, "png", imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}