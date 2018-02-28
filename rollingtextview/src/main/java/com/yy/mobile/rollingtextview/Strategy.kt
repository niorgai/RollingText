package com.yy.mobile.rollingtextview

/**
 * Created by 张宇 on 2018/2/28.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
object Strategy {

    val NoAnimation: CharOrderStrategy = object : SimpleCharOrderStrategy() {
        override fun findCharOrder(sourceChar: Char, targetChar: Char, order: Iterable<Char>?) =
                listOf(targetChar) to Direction.SCROLL_DOWN
    }

    val NormalAnimation: CharOrderStrategy = object : SimpleCharOrderStrategy() {

        override fun findCharOrder(
                sourceChar: Char,
                targetChar: Char,
                order: Iterable<Char>?): Pair<List<Char>, Direction> {

            return if (sourceChar == targetChar) {
                listOf(targetChar) to Direction.SCROLL_DOWN

            } else if (order == null) {
                listOf(sourceChar, targetChar) to Direction.SCROLL_DOWN
                
            } else {
                val srcIndex = order.indexOf(sourceChar)
                val tgtIndex = order.indexOf(targetChar)

                if (srcIndex < tgtIndex) {
                    order.subList(srcIndex, tgtIndex) to Direction.SCROLL_DOWN
                } else {
                    order.subList(tgtIndex, srcIndex).asReversed() to Direction.SCROLL_UP
                }
            }
        }

        private fun <T> Iterable<T>.subList(start: Int, end: Int): List<T> {
            return this.filterIndexed { index, _ -> index in start..end }
        }
    }

    @JvmStatic
    fun SameDirectionAnimation(direction: Direction): CharOrderStrategy = object : CharOrderStrategy {

        override fun findCharOrder(
                sourceText: CharSequence,
                targetText: CharSequence,
                index: Int,
                charPool: CharPool): Pair<List<Char>, Direction> {

            return NormalAnimation.findCharOrder(sourceText, targetText, index, charPool).first to direction
        }
    }
}