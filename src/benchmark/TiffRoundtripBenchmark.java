package org.apache.commons.imaging.formats.tiff;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.tiff.TiffImagingParameters;
import org.openjdk.jmh.annotations.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // Misura il tempo medio per esecuzione
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Unità di misura
@State(Scope.Thread) // Ogni thread ha la propria istanza
public class TiffRoundtripBenchmark {

    private List<File> images;

    @Setup(Level.Trial) // Eseguito una volta prima dei benchmark
    public void setup() throws Exception {
        // Esempio di file TIFF per il benchmark
        images = Arrays.asList(
                new File("src/test/resources/test-image1.tiff"),
                new File("src/test/resources/test-image2.tiff")
        );
    }

    @Benchmark
    public void testTiffRoundtrip() throws Exception {
        for (final File imageFile : images) {
            final ImageMetadata metadata = Imaging.getMetadata(imageFile);
            assert metadata != null;

            final ImageInfo imageInfo = Imaging.getImageInfo(imageFile);
            assert imageInfo != null;

            final BufferedImage image = Imaging.getBufferedImage(imageFile);
            assert image != null;

            final int[] compressions = {
                    TiffConstants.COMPRESSION_UNCOMPRESSED,
                    TiffConstants.COMPRESSION_LZW,
                    TiffConstants.COMPRESSION_PACKBITS,
                    TiffConstants.COMPRESSION_DEFLATE_ADOBE
            };
            final TiffImageParser tiffImageParser = new TiffImageParser();

            for (final int compression : compressions) {
                final TiffImagingParameters params = new TiffImagingParameters();
                params.setCompression(compression);

                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    tiffImageParser.writeImage(image, bos, params);
                    byte[] tempFile = bos.toByteArray();
                    final BufferedImage image2 = Imaging.getBufferedImage(tempFile);
                    assert image2 != null;
                }
            }
        }
    }
}