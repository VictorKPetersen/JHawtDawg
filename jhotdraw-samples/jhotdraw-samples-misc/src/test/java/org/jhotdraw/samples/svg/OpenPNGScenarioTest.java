package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

import java.io.IOException;

public class OpenPNGScenarioTest
        extends ScenarioTest<GivenSVGDrawing, WhenOpeningPNG, ThenImageIsDisplayed> {

    @Test
    public void opening_a_png_adds_it_to_the_view_drawing() throws IOException {
        given().an_empty_svg_view()
                .and().a_valid_png_file();

        when().the_user_opens_the_png_file();

        then().the_drawing_should_contain_the_image();
    }

    @Test
    public void edits_to_existing_png_should_be_saved_to_the_same_file() throws IOException {
        given().an_empty_svg_view()
                .and().a_png_is_already_on_the_canvas();

        when().the_user_edits_the_image_and_saves();

        then().the_file_on_disk_is_updated();
    }
}
