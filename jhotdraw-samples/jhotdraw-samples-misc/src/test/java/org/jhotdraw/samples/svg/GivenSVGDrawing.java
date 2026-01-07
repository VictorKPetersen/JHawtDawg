package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.samples.svg.figures.SVGImageFigure;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GivenSVGDrawing extends Stage<GivenSVGDrawing> {
    @ProvidedScenarioState
    private SVGView view;

    @ProvidedScenarioState
    private Drawing drawing;

    @ProvidedScenarioState
    private File imageFile;
    public GivenSVGDrawing() {
    }
    public GivenSVGDrawing an_empty_svg_view() {
        view = new SVGView();
        view.init();
        drawing = view.getDrawing();
        return this;
    }

    public GivenSVGDrawing a_valid_png_file() throws IOException {
        imageFile = new File("test_image.png");
        // Create a 1x1 pixel image in memory
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // Save it to the disk
        ImageIO.write(img, "png", imageFile);
        // Cleanup after the test
        imageFile.deleteOnExit();

        return this;
    }

    public GivenSVGDrawing a_png_is_already_on_the_canvas() throws IOException {
        // Create a valid file
        imageFile = new File("existing_work.png");
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(img, "png", imageFile);
        // Add to the drawing
        SVGImageFigure figure = new SVGImageFigure();
        figure.loadImage(imageFile);
        drawing.add(figure);

        return this;
    }
}
