package org.example;


import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class PrimaryControllerTest {

    PrimaryController primaryController = new PrimaryController();
    JFXPanel jfxPanel = new JFXPanel();
    Image image;
    int[] graph;

    @Before
    public void setUp() throws Exception {
        File imFile = new File("src/main/resources/org/example/romeMap.png");
        image = new Image(imFile.toURI().toString(), 400, 400, false, false);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setBlackWhite() {
        primaryController.setBlackWhite(image);

        PixelReader pixelReader = primaryController.setBlackWhite(image).getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                assertTrue(pixelReader.getColor(x, y).equals(Color.BLACK) || pixelReader.getColor(x, y).equals(Color.WHITE));
        }
    }

    @Test
    public void createGraphArray() {
        graph = primaryController.createGraphArray(primaryController.setBlackWhite(image));

        for (Integer num : graph) {
            assertTrue(num.equals(-1) || num.equals(0));
        }
    }
}
