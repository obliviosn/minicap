package io.devicefarmer.minicap

class Parameters private constructor(
    val projection: Projection?,
    val screenshot: Boolean,
    val socket: String,
    val quality: Int,
    val displayInfo: Boolean,
    val frameRate: Float
) {
    data class Builder(
        var projection: Projection? = null,
        var screenshot: Boolean = false,
        var socket: String = "minicap",
        var quality: Int = 100,
        var displayInfo: Boolean = false,
        var frameRate: Float = Float.MAX_VALUE
    ) {
        //TODO make something more robust
        fun projection(p: String) = apply {
            this.projection = p.run {
                val s = this.split('@', '/', 'x')
                Projection(
                    Size(s[0].toInt(), s[1].toInt()),
                    Size(s[2].toInt(), s[3].toInt()),
                    s[4].toInt()
                )
            }
        }

        fun screenshot(s: Boolean) = apply { this.screenshot = s }
        fun socket(name: String) = apply { this.socket = name }
        fun quality(value: Int) = apply { this.quality = value }
        fun displayInfo(enabled: Boolean) = apply { this.displayInfo = enabled }
        fun frameRate(value: Float) = apply { this.frameRate = value }
        fun build() = Parameters(projection, screenshot, socket, quality, displayInfo, frameRate)
    }
}
