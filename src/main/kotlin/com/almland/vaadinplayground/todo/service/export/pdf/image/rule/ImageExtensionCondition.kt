package com.almland.vaadinplayground.todo.service.export.pdf.image.rule

internal enum class ImageExtensionCondition(val extension: String, val base64Image: String) {
    IMAGE_PNG(".png", "png"), IMAGE_GIF(".gif", "gif"), IMAGE_JPEG(".jpeg", "jpeg")
}