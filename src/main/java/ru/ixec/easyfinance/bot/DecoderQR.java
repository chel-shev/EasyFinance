package ru.ixec.easyfinance.bot;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class DecoderQR {

    public static String decode(java.io.File qrCodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<>(DecodeHintType.class) {{
                put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            }};
            Result result = new MultiFormatReader().decode(bitmap, tmpHintsMap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return "";
        }
    }
}
