package io.devicefarmer.minicap

import kotlin.math.roundToInt

data class Projection(
    val realSize: Size, var targetSize: Size,
    val orientation: Int
) {
    fun forceAspectRatio() {
        val aspect = realSize.width.toFloat() / realSize.height.toFloat()
        targetSize = if (targetSize.height > targetSize.width / aspect) {
            Size(targetSize.width, ((targetSize.width / aspect)).roundToInt())
        } else {
            Size((targetSize.height * aspect).roundToInt(), targetSize.height)
        }
    }

    override fun toString(): String =
        "${realSize.width}x${realSize.height}@${targetSize.width}x${targetSize.height}/${orientation}"
}
