package fr.xebia;

import fr.xebia.core.ImageComponents;
import fr.xebia.transform.morphology.DilationTransformer;
import fr.xebia.transform.morphology.ErodeTransformer;
import fr.xebia.transform.morphology.MorphologyOperations;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class DilateTransformerTest implements ImageComponents, MorphologyOperations {

    @Test
    public void should_erode_an_image() throws IOException {
        assertOnImage(
                "src/test/resources/morph/circles.png",
                "src/test/resources/morph/circles-dilated.png",
                new DilationTransformer()
        );
    }

    private void assertOnImage(String originalPathName, String expectedPathName, Function<BufferedImage, BufferedImage> transformer) throws IOException {
        // Given a image
        final BufferedImage rawImage = ImageIO.read(new File(originalPathName));

        // When
        final BufferedImage result = transformer.apply(rawImage);

        // Then
        //exportImage(result);

        final BufferedImage expectedImage = ImageIO.read(new File(expectedPathName));
        assertThat(imageToMatrix(result)).isEqualTo(imageToMatrix(expectedImage));
    }

}
