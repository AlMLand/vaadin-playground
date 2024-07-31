package com.almland.vaadinplayground.todo.service.export.pdf.barcode

import com.almland.vaadinplayground.todo.service.export.pdf.image.ImageUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.oned.Code128Writer
import java.io.ByteArrayOutputStream
import java.io.IOException

internal object BarcodeUtils {

    private const val IMAGE_PNG = "png"
    private const val WIDTH_MULTIPLICATOR = 11
    private const val DEFAULT_RESPONSE_BARCODE = ""

    fun getBarCodeAsBase64(text: String): String =
        try {
            ImageUtils
                .convertImageToBase64(
                    getBarCode(text).toByteArray(),
                    IMAGE_PNG
                )
        } catch (ioE: IOException) {
            ioE.printStackTrace()
            DEFAULT_RESPONSE_BARCODE
        } catch (writeE: WriterException) {
            writeE.printStackTrace()
            DEFAULT_RESPONSE_BARCODE
        }

    private fun getBarCode(text: String): ByteArrayOutputStream =
        ByteArrayOutputStream().also { stream ->
            val hints: Map<EncodeHintType, Int> = mapOf(EncodeHintType.MARGIN to 0)
            val bitMatrix: BitMatrix = Code128Writer()
                .encode(text, BarcodeFormat.CODE_128, text.length * WIDTH_MULTIPLICATOR, 20, hints)
            MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_PNG, stream)
        }
}
