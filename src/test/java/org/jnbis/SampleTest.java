package org.jnbis;

import org.jnbis.record.HighResolutionGrayscaleFingerprint;
import org.jnbis.record.UserDefinedDescriptiveText;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:m.h.shams@gmail.com">M. H. Shamsi</a>
 * @version 1.0.0
 * @since Oct 31, 2007
 */
public class SampleTest {
    private NistDecoder nistDecoder;
    private WsqDecoder wsqDecoder;
    private ImageUtils imageUtils;

    @Before
    public void setUp() throws Exception {
        nistDecoder = new NistDecoder();
        wsqDecoder = new WsqDecoder();
        imageUtils = new ImageUtils();
    }

    @Test
    public void wsq2jpeg() throws Exception {
        Bitmap decoded = wsqDecoder.decode(FileUtils.absoluteFile("samples/sample.wsq"));

        Assert.assertNotNull(decoded);

        Assert.assertEquals(8, decoded.getDepth());
        Assert.assertEquals(622, decoded.getHeight());
        Assert.assertEquals(545, decoded.getWidth());
        Assert.assertEquals(622 * 545, decoded.getLength());
        Assert.assertEquals(1, decoded.getLossyFlag());
        Assert.assertEquals(-1, decoded.getPpi());
        Assert.assertEquals(622 * 545, decoded.getPixels().length);

        // For local check.
        //FileUtils.save(imageUtils.bitmap2jpeg(decoded), "/path/to/file.jpeg");
    }

    @Test
    public void nist2jpeg() throws Exception {
        DecodedData decoded = nistDecoder.decode(FileUtils.absoluteFile("samples/sample.an2"), DecodedData.Format.JPEG);

        Map<Integer, String> userDefinedFields = decoded.getUserDefinedText(0).getUserDefinedFields();
        Assert.assertEquals("57", userDefinedFields.get(1));
        Assert.assertEquals("00", userDefinedFields.get(2));
        Assert.assertEquals("domain defined text place holder", userDefinedFields.get(3));

        Set<Integer> keys = decoded.getHiResGrayscaleFingerPrintKeys();

        Assert.assertEquals(14, keys.size());

        for (Integer key : keys) {
            HighResolutionGrayscaleFingerprint image = decoded.getHiResGrayscaleFingerprint(key);

            Assert.assertNotNull(image);

            // For local check
//            FileUtils.save(image.getImageData(), "/path/to/file-" + key + ".jpeg");
        }
    }
}