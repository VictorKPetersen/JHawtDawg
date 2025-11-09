package org.jhotdraw.samples.svg;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jhotdraw.gui.JFileURIChooser;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.io.InputFormat;
import org.mockito.Mockito;

import javax.swing.filechooser.FileFilter;
import java.net.URI;
import java.util.Arrays;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.IOException;

public class SVGViewTest {

    private SVGView svgView;
    private SVGView spyView;
    private JFileURIChooser mockChooser;
    private Drawing mockDrawing;
    private InputFormat mockFormat;
    private FileFilter mockFileFilter;
    private URI realUri;

    @Before
    public void setUp() throws Exception {
        // Initialize the class under test
        svgView = new SVGView();
        spyView = Mockito.spy(svgView);

        // Initialize all mocks
        mockChooser = mock(JFileURIChooser.class);
        mockDrawing = mock(Drawing.class);
        mockFormat = mock(InputFormat.class);
        mockFileFilter = mock(FileFilter.class);
        realUri = new URI("file:///test.svg");
    }

    @After
    public void tearDown() {
        Mockito.reset(mockChooser, mockFormat, mockFileFilter, spyView);
    }
    @Test
    public void testGetSelectedInputFormat_boundaryCase_nullChooser() {
        InputFormat actualFormat = svgView.getSelectedInputFormat(null, mockDrawing);

        assertNull("Should return null when the URIChooser is null", actualFormat);
    }

    @Test
    public void testGetSelectedInputFormat_boundaryCase_noMap() {
        InputFormat actualFormat = svgView.getSelectedInputFormat(mockChooser, mockDrawing);

        assertNull("Should return null when the client property map is null", actualFormat);
    }

    @Test
    public void testTryReadWithFormat_bestCase_successfulRead() throws Exception {
        doNothing().when(mockFormat).read(realUri, mockDrawing, true);

        boolean success = svgView.tryReadWithFormat(realUri, mockDrawing, mockFormat);

        assertTrue("Should return true on successful read", success);

        verify(mockFormat, times(1)).read(realUri, mockDrawing, true);
    }

    @Test
    public void testTryReadWithFormat_boundaryCase_nullFormat() {
        boolean success = svgView.tryReadWithFormat(realUri, mockDrawing, null);

        assertFalse("Should return false if InputFormat is null", success);
    }

    @Test
    public void testTryReadWithFormat_boundaryCase_readFails() throws Exception {
        doThrow(new IOException("Simulated Read Error")).when(mockFormat).read(realUri, mockDrawing, true);

        boolean success = svgView.tryReadWithFormat(realUri, mockDrawing, mockFormat);

        assertFalse("Should return false when InputFormat.read throws an exception", success);
        verify(mockFormat, times(1)).read(realUri, mockDrawing, true);
    }

    @Test
    public void testTryReadWithAllFormats_bestCase_fallbackSuccess() {
        InputFormat failedFormat1 = mock(InputFormat.class);
        InputFormat successfulFormat = mock(InputFormat.class);

        // Stub the drawing to return all formats (including the initial failed one)
        when(mockDrawing.getInputFormats()).thenReturn(
                Arrays.asList(mockFormat, failedFormat1, successfulFormat)
        );

        // Stub failedformat1 and successful to be false and true
        doReturn(false).when(spyView).tryReadWithFormat(realUri, mockDrawing, failedFormat1);
        doReturn(true).when(spyView).tryReadWithFormat(realUri, mockDrawing, successfulFormat);

        boolean success = spyView.tryReadWithAllFormats(realUri, mockDrawing, mockFormat);

        assertTrue("Should return true when a fallback format succeeds", success);
        // Verify the flow: it skipped mockFormat, tried failedFormat1, and then succeeded on successfulFormat
        verify(spyView, times(1)).tryReadWithFormat(realUri, mockDrawing, failedFormat1);
        verify(spyView, times(1)).tryReadWithFormat(realUri, mockDrawing, successfulFormat);
    }

    @Test
    public void testTryReadWithAllFormats_boundaryCase_allFallbacksFail() {
        InputFormat failedFormat1 = mock(InputFormat.class);
        InputFormat failedFormat2 = mock(InputFormat.class);

        // Stub the drawing to return all formats
        when(mockDrawing.getInputFormats()).thenReturn(
                Arrays.asList(mockFormat, failedFormat1, failedFormat2)
        );

        // Stub all nested calls to fail
        doReturn(false).when(spyView).tryReadWithFormat(realUri, mockDrawing, failedFormat1);
        doReturn(false).when(spyView).tryReadWithFormat(realUri, mockDrawing, failedFormat2);

        boolean success = spyView.tryReadWithAllFormats(realUri, mockDrawing, mockFormat);

        assertFalse("Should return false when all fallback formats fail to read", success);
        // Verify all fallbacks were attempted
        verify(spyView, times(1)).tryReadWithFormat(realUri, mockDrawing, failedFormat1);
        verify(spyView, times(1)).tryReadWithFormat(realUri, mockDrawing, failedFormat2);
    }
}
