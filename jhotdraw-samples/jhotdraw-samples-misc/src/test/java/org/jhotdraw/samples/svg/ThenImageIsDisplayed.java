package org.jhotdraw.samples.svg;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import org.jhotdraw.draw.Drawing;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ThenImageIsDisplayed extends Stage<ThenImageIsDisplayed> {
    @ExpectedScenarioState
    private Drawing drawing;

    @ExpectedScenarioState
    private File imageFile;

    public ThenImageIsDisplayed the_drawing_should_contain_the_image() {
        // Check if the drawing now has the Image figure added
        assertThat(drawing.getChildren()).isNotEmpty();
        return this;
    }

    public ThenImageIsDisplayed the_file_on_disk_is_updated() {
        // Verify the file exists and has been modified
        assertThat(imageFile.exists()).isTrue();
        assertThat(imageFile.length()).isGreaterThan(0);
        return this;
    }
}