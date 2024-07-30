package com.almland.vaadinplayground.todo.service.export.pdf.image.rule

import java.util.function.Supplier

internal object RuleCreator {
    fun createRules(imagePath: String): Map<ImageExtensionCondition, Rule> =
        mutableMapOf<ImageExtensionCondition, Rule>().apply {
            this[ImageExtensionCondition.IMAGE_PNG] = createImagePng(imagePath)
            this[ImageExtensionCondition.IMAGE_GIF] = createImageGif(imagePath)
            this[ImageExtensionCondition.IMAGE_JPEG] = createImageJpeg(imagePath)
        }

    private fun createRule(
        condition: Supplier<Boolean>,
        process: Supplier<String>
    ): Rule = Rule(condition, process)

    private fun createImageJpeg(imagePath: String): Rule =
        createRule(
            { isCorrectFormat(imagePath, ImageExtensionCondition.IMAGE_JPEG.extension) },
            { ImageExtensionCondition.IMAGE_JPEG.base64Image }
        )

    private fun createImageGif(imagePath: String): Rule =
        createRule(
            { isCorrectFormat(imagePath, ImageExtensionCondition.IMAGE_GIF.extension) },
            { ImageExtensionCondition.IMAGE_GIF.base64Image }
        )

    private fun createImagePng(imagePath: String): Rule =
        createRule(
            { isCorrectFormat(imagePath, ImageExtensionCondition.IMAGE_PNG.extension) },
            { ImageExtensionCondition.IMAGE_PNG.base64Image }
        )

    private fun isCorrectFormat(imagePath: String, extension: String): Boolean =
        imagePath
            .lowercase()
            .endsWith(extension)
}
