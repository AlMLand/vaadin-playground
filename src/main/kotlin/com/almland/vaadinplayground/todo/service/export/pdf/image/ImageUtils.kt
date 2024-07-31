package com.almland.vaadinplayground.todo.service.export.pdf.image

import com.almland.vaadinplayground.todo.service.export.pdf.image.rule.ImageExtensionCondition
import com.almland.vaadinplayground.todo.service.export.pdf.image.rule.RuleCreator
import java.util.Base64
import org.apache.poi.util.IOUtils

internal object ImageUtils {

    fun convertImageToBase64(imagePath: String): String =
        RuleCreator.createRules(imagePath).let { rules ->
            ImageExtensionCondition.entries
                .filter { rules[it]!!.condition.get() }
                .map { rules[it]!!.process.get() }
                .first()
                .let { convertImageToBase64(imagePath, it) }
        }

    private fun convertImageToBase64(imagePath: String, imageFormat: String): String =
        this::class.java.getResource("/templates/$imagePath")
            ?.let { url ->
                Base64
                    .getEncoder()
                    .encodeToString(url.openStream().use { IOUtils.toByteArray(it) })
                    .let { imageBase64 -> "data:image/$imageFormat;base64,$imageBase64" }
            }
            ?: "no image"

    fun convertImageToBase64(byteArray: ByteArray, imageFormat: String): String =
        Base64
            .getEncoder()
            .encodeToString(byteArray)
            .let { imageBase64 -> "data:image/$imageFormat;base64,$imageBase64" }
}
