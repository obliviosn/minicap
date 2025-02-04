/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.devicefarmer.minicap

import java.lang.NumberFormatException
import kotlin.Throws

/**
 * Immutable class for describing width and height dimensions in pixels.
 */
class Size
/**
 * Create a new immutable Size instance.
 *
 * @param width The width of the size, in pixels
 * @param height The height of the size, in pixels
 */(
    /**
     * Get the width of the size (in pixels).
     * @return width
     */
    val width: Int,
    /**
     * Get the height of the size (in pixels).
     * @return height
     */
    val height: Int
) {
    /**
     * Check if this size is equal to another size.
     *
     *
     * Two sizes are equal if and only if both their widths and heights are
     * equal.
     *
     *
     *
     * A size object is never equal to any other type of object.
     *
     *
     * @return `true` if the objects were equal, `false` otherwise
     */
    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (this === obj) {
            return true
        }
        if (obj is Size) {
            val other = obj
            return width == other.width && height == other.height
        }
        return false
    }

    /**
     * Return the size represented as a string with the format `"WxH"`
     *
     * @return string representation of the size
     */
    override fun toString(): String {
        return width.toString() + "x" + height
    }

    /**
     * {@inheritDoc}
     */
    override fun hashCode(): Int {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return height xor (width shl Integer.SIZE / 2 or (width ushr Integer.SIZE / 2))
    }

    companion object {
        private fun invalidSize(s: String): NumberFormatException {
            throw NumberFormatException("Invalid Size: \"$s\"")
        }

        /**
         * Parses the specified string as a size value.
         *
         *
         * The ASCII characters `\``u002a` ('*') and
         * `\``u0078` ('x') are recognized as separators between
         * the width and height.
         *
         *
         * For any `Size s`: `Size.parseSize(s.toString()).equals(s)`.
         * However, the method also handles sizes expressed in the
         * following forms:
         *
         *
         * "*width*`x`*height*" or
         * "*width*`*`*height*" `=> new Size(width, height)`,
         * where *width* and *height* are string integers potentially
         * containing a sign, such as "-10", "+7" or "5".
         *
         * <pre>`Size.parseSize("3*+6").equals(new Size(3, 6)) == true
         * Size.parseSize("-3x-6").equals(new Size(-3, -6)) == true
         * Size.parseSize("4 by 3") => throws NumberFormatException
        `</pre> *
         *
         * @param string the string representation of a size value.
         * @return the size value represented by `string`.
         *
         * @throws NumberFormatException if `string` cannot be parsed
         * as a size value.
         * @throws NullPointerException if `string` was `null`
         */
        @Throws(NumberFormatException::class)
        fun parseSize(string: String): Size {
            var sep_ix = string.indexOf('*')
            if (sep_ix < 0) {
                sep_ix = string.indexOf('x')
            }
            if (sep_ix < 0) {
                throw invalidSize(string)
            }
            return try {
                Size(string.substring(0, sep_ix).toInt(), string.substring(sep_ix + 1).toInt())
            } catch (e: NumberFormatException) {
                throw invalidSize(string)
            }
        }
    }
}