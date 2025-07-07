package cn.allin.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlin.jvm.JvmStatic

/**
 * 1900年
 */
private const val BASE_YEAR = 1900

/**
 * 1900-01-31，农历正月初一
 */
private val BASE_DAY = LocalDate(BASE_YEAR, 1, 31).toEpochDays().toInt()

/**
 * 此表来自：[https://github.com/jjonline/calendar.js/blob/master/calendar.js](https://github.com/jjonline/calendar.js/blob/master/calendar.js)
 * 农历表示：
 * 1.  表示当年有无闰年，有的话，为闰月的月份，没有的话，为0。
 * 2-4.为除了闰月外的正常月份是大月还是小月，1为30天，0为29天。
 * 5.  表示闰月是大月还是小月，仅当存在闰月的情况下有意义。
 */
private val LUNAR_CODE = longArrayOf(
    0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
    0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
    0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
    0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
    0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
    0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0,
    0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
    0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,
    0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
    0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
    0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
    0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
    0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
    0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
    0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,
    0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0,
    0x0a2e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4,
    0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0,
    0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160,
    0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a2d0, 0x0d150, 0x0f252,
    0x0d520
)

// 支持的最大年限
private val MAX_YEAR = BASE_YEAR + LUNAR_CODE.size - 1


/**
 * 获取对应年的农历信息
 *
 * @param year 年
 * @return 农历信息
 */
private fun getCode(year: Int): Long {
    return LUNAR_CODE[year - BASE_YEAR]
}


object Lunar {

    @JvmStatic
    val BASE_DAY: Int get() = cn.allin.utils.BASE_DAY

    @JvmStatic
    val BASE_YEAR get() = cn.allin.utils.BASE_YEAR


    @JvmStatic
    val MAX_YEAR: Int get() = cn.allin.utils.MAX_YEAR



    /**
     * 传回农历 y年的总天数
     *
     * @param y 年
     * @return 总天数
     */
    @JvmStatic
    fun yearDays(y: Int): Int {
        var i: Int
        var sum = 348
        i = 0x8000
        while (i > 0x8) {
            if (getCode(y) and i.toLong() != 0L) {
                sum += 1
            }
            i = i shr 1
        }
        return sum + leapDays(y)
    }

    /**
     * 传回农历 y年闰月的天数，如果本年无闰月，返回0，区分大小月
     *
     * @param y 农历年
     * @return 闰月的天数
     */
    @JvmStatic
    fun leapDays(y: Int): Int {
        return if (leapMonth(y) != 0) {
            if (getCode(y) and 0x10000L != 0L) 30 else 29
        } else 0
    }


    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0<br></br>
     * 此方法会返回润N月中的N，如二月、闰二月都返回2
     *
     * @param y 年
     * @return 润的月, 没闰传回 0
     */
    fun leapMonth(y: Int): Int {
        return (getCode(y) and 0xfL).toInt()
    }

    /**
     * 传回农历 y年m月的总天数，区分大小月
     *
     * @param y 年
     * @param m 月
     * @return 总天数
     */
    @JvmStatic
    fun monthDays(y: Int, m: Int): Int {
        val _0x1: Long = 0x10000
        return if (getCode(y) and _0x1 shr m == 0L) 29 else 30
    }


    /**
     * 获得节日列表
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 获得农历节日
     * @since 5.4.5
     */
    fun getFestivals(year: Int, month: Int, day: Int): String? {
        // 春节判断，如果12月是小月，则29为除夕，否则30为除夕
        var day = day
        if (12 == month && 29 == day) {
            if (29 == monthDays(year, month)) {
                day++
            }
        }
        return getFestivals(month, day)
    }

    /**
     * 获得节日列表，此方法无法判断月是否为大月或小月
     *
     * @param month 月
     * @param day   日
     * @return 获得农历节日
     */
    fun getFestivals(month: Int, day: Int): String? {
        return LUNAR_FESTIVAL[Pair(month, day)]
    }


    /**
     * 获得节日
     *
     * @param month 月
     * @param day   日
     * @return 获得公历节日
     */
    fun getSolarFestivals(month: Int, day: Int): String? {
        return SOLAR_FESTIVAL[month to day]
    }


    /**
     * 传入 月日的offset 传回干支, 0=甲子
     *
     * @param num 月日的offset
     * @return 干支
     */
    fun cyclicalm(num: Int): String {
        return GAN[num % 10] + ZHI[num % 12]
    }

    /**
     * 传入年传回干支
     *
     * @param year 农历年
     * @return 干支
     * @since 5.4.7
     */
    fun getGanzhiOfYear(year: Int): String {
        // 1864年（1900 - 36）是甲子年，用于计算基准的干支年
        return cyclicalm(year - BASE_YEAR + 36)
    }

    /**
     * 获取干支月
     *
     * @param year  公历年
     * @param month 公历月，从1开始
     * @param day   公历日
     * @return 干支月
     * @since 5.4.7
     */
    fun getGanzhiOfMonth(year: Int, month: Int, day: Int): String {
        //返回当月「节」为几日开始
        val firstNode = solarGetTerm(year, month * 2 - 1)
        // 依据12节气修正干支月
        var monthOffset = (year - BASE_YEAR) * 12 + month + 11
        if (day >= firstNode) {
            monthOffset++
        }
        return cyclicalm(monthOffset)
    }

    /**
     * 获取干支日
     *
     * @param year  公历年
     * @param month 公历月，从1开始
     * @param day   公历日
     * @return 干支
     * @since 5.4.7
     */
    fun getGanzhiOfDay(year: Int, month: Int, day: Int): String {
        // 与1970-01-01相差天数，不包括当天
        val days = LocalDate(year, month, day).toEpochDays() - 1
        //1899-12-21是农历1899年腊月甲子日  41：相差1900-01-31有41天
        return cyclicalm((days - BASE_DAY + 41).toInt())
    }


    /**
     * 传入公历y年获得该年第n个节气的公历日期
     *
     * @param y  公历年(1900-2100)
     * @param n 二十四节气中的第几个节气(1~24)；从n=1(小寒)算起
     * @return getTerm(1987,3) -》4;意即1987年2月4日立春
     */
    @JvmStatic
    fun solarGetTerm(y: Int, n: Int): Int {
        if (y < 1900 || y > 2100) {
            return -1
        }
        if (n < 1 || n > 24) {
            return -1
        }
        val _table: String = S_TERM_INFO[y - 1900]

        val w = 5
        val _info = IntArray(6){ i ->
            _table.substring(i * w, w * (i + 1)).toInt(16)
        }
        val _4 = 4
        val _calday = arrayOfNulls<String>(24)
        for (i in 0..5) {
            _calday[_4 * i] = _info[i].toString().substring(0, 1)
            _calday[_4 * i + 1] = _info[i].toString().substring(1, 3)
            _calday[_4 * i + 2] = _info[i].toString().substring(3, 4)
            _calday[_4 * i + 3] = _info[i].toString().substring(4, 6)
        }

        return (_calday[n - 1])!!.toInt()
    }

    /**
     * 根据日期获取节气
     * @param localDate 日期
     * @return 返回指定日期所处的节气，若不是一个节气则返回空字符串
     */
    @JvmStatic
    fun solarGetTerm(localDate: LocalDate): String {
        return getTermInternal(localDate.year, localDate.month.number, localDate.day)
    }

    /**
     * 根据年月日获取节气
     * @param year 公历年
     * @param mouth 公历月，从1开始
     * @param day 公历日，从1开始
     * @return 返回指定年月日所处的节气，若不是一个节气则返回空字符串
     */
    @JvmStatic
    fun solarGetTerm(year: Int, mouth: Int, day: Int): String {
        return solarGetTerm(LocalDate(year, mouth, day))
    }

}

/**
 * 十天干：甲（jiǎ）、乙（yǐ）、丙（bǐng）、丁（dīng）、戊（wù）、己（jǐ）、庚（gēng）、辛（xīn）、壬（rén）、癸（guǐ）
 * 十二地支：子（zǐ）、丑（chǒu）、寅（yín）、卯（mǎo）、辰（chén）、巳（sì）、午（wǔ）、未（wèi）、申（shēn）、酉（yǒu）、戌（xū）、亥（hài）
 * 十二地支对应十二生肖:子-鼠，丑-牛，寅-虎，卯-兔，辰-龙，巳-蛇， 午-马，未-羊，申-猴，酉-鸡，戌-狗，亥-猪
 *
 * @see [天干地支：简称，干支](https://baike.baidu.com/item/%E5%A4%A9%E5%B9%B2%E5%9C%B0%E6%94%AF/278140)
 */
private val GAN = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
private val ZHI = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")

private val LUNAR_FESTIVAL = hashMapOf(
    (1 to 1) to "春节",
    (1 to 15) to "元宵节",


    (5 to 5) to "端午节",

    (7 to 7) to "七夕",
    (7 to 15) to "中元节",

    (8 to 15) to "中秋节",

    (9 to 9) to "重阳节",

    (10 to 15) to "下元节",

    (12 to 8) to "腊八节",
    (12 to 23) to "小年",
    (12 to 30) to "除夕",
)



private val SOLAR_FESTIVAL = hashMapOf(
    // 节日
    (1 to 1) to "元旦",
    // 二月
    (2 to 14) to "情人节",
    // 三月
    (3 to 8) to "妇女节",
    (3 to 12) to "植树节",
    (3 to 15) to "消费者",
    // 四月
    (4 to 1) to "愚人节",
    // 五月
    (5 to 1) to "劳动节",
    (5 to 4) to "青年节",
    // 六月
    (6 to 1) to "儿童节",
    // 七月
    (7 to 1) to "建党节",
    // 八月
    (8 to 1) to "建军节",
    // 九月
    (9 to 10) to "教师节",
    // 十月
    (10 to 1) to "国庆节",
)

/**
 * 1900-2100各年的24节气日期速查表
 * 此表来自：https://github.com/jjonline/calendar.js/blob/master/calendar.js
 */
private val S_TERM_INFO = arrayOf(
    "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf97c3598082c95f8c965cc920f",
    "97bd0b06bdb0722c965ce1cfcc920f", "b027097bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
    "97bcf97c359801ec95f8c965cc920f", "97bd0b06bdb0722c965ce1cfcc920f", "b027097bd097c36b0b6fc9274c91aa",
    "97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f", "97bd0b06bdb0722c965ce1cfcc920f",
    "b027097bd097c36b0b6fc9274c91aa", "9778397bd19801ec9210c965cc920e", "97b6b97bd19801ec95f8c965cc920f",
    "97bd09801d98082c95f8e1cfcc920f", "97bd097bd097c36b0b6fc9210c8dc2", "9778397bd197c36c9210c9274c91aa",
    "97b6b97bd19801ec95f8c965cc920e", "97bd09801d98082c95f8e1cfcc920f", "97bd097bd097c36b0b6fc9210c8dc2",
    "9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec95f8c965cc920e", "97bcf97c3598082c95f8e1cfcc920f",
    "97bd097bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec9210c965cc920e",
    "97bcf97c3598082c95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
    "97b6b97bd19801ec9210c965cc920e", "97bcf97c3598082c95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722",
    "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f",
    "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
    "97bcf97c359801ec95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
    "97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f", "97bd097bd07f595b0b6fc920fb0722",
    "9778397bd097c36b0b6fc9210c8dc2", "9778397bd19801ec9210c9274c920e", "97b6b97bd19801ec95f8c965cc920f",
    "97bd07f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c920e",
    "97b6b97bd19801ec95f8c965cc920f", "97bd07f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2",
    "9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bd07f1487f595b0b0bc920fb0722",
    "7f0e397bd097c36b0b6fc9210c8dc2", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
    "97bcf7f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
    "97b6b97bd19801ec9210c965cc920e", "97bcf7f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
    "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf7f1487f531b0b0bb0b6fb0722",
    "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
    "97bcf7f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
    "97b6b97bd19801ec9210c9274c920e", "97bcf7f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
    "9778397bd097c36b0b6fc9210c91aa", "97b6b97bd197c36c9210c9274c920e", "97bcf7f0e47f531b0b0bb0b6fb0722",
    "7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c920e",
    "97b6b7f0e47f531b0723b0b6fb0722", "7f0e37f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2",
    "9778397bd097c36b0b70c9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721", "7f0e37f1487f595b0b0bb0b6fb0722",
    "7f0e397bd097c35b0b6fc9210c8dc2", "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721",
    "7f0e27f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
    "97b6b7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
    "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
    "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721",
    "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
    "97b6b7f0e47f531b0723b0787b0721", "7f0e27f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
    "9778397bd097c36b0b6fc9210c91aa", "97b6b7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
    "7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9210c8dc2", "977837f0e37f149b0723b0787b0721",
    "7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f5307f595b0b0bc920fb0722", "7f0e397bd097c35b0b6fc9210c8dc2",
    "977837f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e37f1487f595b0b0bb0b6fb0722",
    "7f0e397bd097c35b0b6fc9210c8dc2", "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
    "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "977837f0e37f14998082b0787b06bd",
    "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
    "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
    "7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
    "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14998082b0787b06bd",
    "7f07e7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
    "977837f0e37f14998082b0723b06bd", "7f07e7f0e37f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
    "7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b0721",
    "7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f1487f595b0b0bb0b6fb0722", "7f0e37f0e37f14898082b0723b02d5",
    "7ec967f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f1487f531b0b0bb0b6fb0722",
    "7f0e37f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
    "7f0e37f1487f531b0b0bb0b6fb0722", "7f0e37f0e37f14898082b072297c35", "7ec967f0e37f14998082b0787b06bd",
    "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e37f0e37f14898082b072297c35",
    "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
    "7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f149b0723b0787b0721",
    "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14998082b0723b06bd",
    "7f07e7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722", "7f0e37f0e366aa89801eb072297c35",
    "7ec967f0e37f14998082b0723b06bd", "7f07e7f0e37f14998083b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
    "7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14898082b0723b02d5", "7f07e7f0e37f14998082b0787b0721",
    "7f07e7f0e47f531b0723b0b6fb0722", "7f0e36665b66aa89801e9808297c35", "665f67f0e37f14898082b0723b02d5",
    "7ec967f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0722", "7f0e36665b66a449801e9808297c35",
    "665f67f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
    "7f0e36665b66a449801e9808297c35", "665f67f0e37f14898082b072297c35", "7ec967f0e37f14998082b0787b06bd",
    "7f07e7f0e47f531b0723b0b6fb0721", "7f0e26665b66a449801e9808297c35", "665f67f0e37f1489801eb072297c35",
    "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722"
)

/**
 * 24节气
 */
private val TERMS = arrayOf(
    "小寒", "大寒", "立春", "雨水", "惊蛰", "春分",
    "清明", "谷雨", "立夏", "小满", "芒种", "夏至",
    "小暑", "大暑", "立秋", "处暑", "白露", "秋分",
    "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
)


/**
 * 根据年月日获取节气, 内部方法，不对月和日做有效校验
 * @param year 公历年
 * @param mouth 公历月，从1开始
 * @param day 公历日，从1开始
 * @return 返回指定年月日所处的节气，若不是一个节气则返回空字符串
 */
private fun getTermInternal(year: Int, mouth: Int, day: Int): String {
    require(!(year < 1900 || year > 2100)) { "只支持1900-2100之间的日期获取节气" }
    val termTable = S_TERM_INFO[year - 1900]

    // 节气速查表中每5个字符含有4个节气，通过月份直接计算偏移
    val segment = (mouth + 1) / 2 - 1
    val termInfo = termTable.substring(segment * 5, (segment + 1) * 5).toInt(16)
    val termInfoStr = termInfo.toString()
    val segmentTable = arrayOfNulls<String>(4)
    segmentTable[0] = termInfoStr.substring(0, 1)
    segmentTable[1] = termInfoStr.substring(1, 3)
    segmentTable[2] = termInfoStr.substring(3, 4)
    segmentTable[3] = termInfoStr.substring(4, 6)

    // 奇数月份的节气在前2个，偶数月份的节气在后两个
    val segmentOffset = if (mouth and 1 == 1) 0 else 2
    if (day == segmentTable[segmentOffset]!!.toInt()) {
        return TERMS[segment * 4 + segmentOffset]
    }
    return if (day == segmentTable[segmentOffset + 1]!!.toInt()) {
        TERMS[segment * 4 + segmentOffset + 1]
    } else ""
}
