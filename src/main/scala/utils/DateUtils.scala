package utils

import java.util.{GregorianCalendar, TimeZone}

object DateUtils {

  def transformDateToPosix(date: String): Option[Long] = {

    val timeZone = TimeZone.getTimeZone("UTC")
    // example: 2019-12-31
    val dateWithDashDelimiter =
      """(\d{4})-(\d{2})-(\d{2})""".r
    // example: 31.12.2019
    val dateWithPointDelimiter =
      """(\d{2})\.(\d{2})\.(\d{4})""".r
    // example: 31122019
    val dateWithoutDelimiter =
      """(\d{2})(\d{2})(\d{4})""".r
    // example: 31.12.2019 00:00:00
    val dateTimeWithPointDelimiter =
      """(\d{2})\.(\d{2})\.(\d{4}) (\d{2}):(\d{2}):(\d{2})""".r

    date match {
      case dateWithDashDelimiter(year, month, day) => {
        val calendar = new GregorianCalendar(year.toInt, month.toInt - 1, day.toInt)
        calendar.setTimeZone(timeZone)
        Some(calendar.getTimeInMillis)
      }

      case dateWithPointDelimiter(day, month, year) => {
        val calendar = new GregorianCalendar(year.toInt, month.toInt - 1, day.toInt)
        calendar.setTimeZone(timeZone)
        Some(calendar.getTimeInMillis)
      }

      case dateWithoutDelimiter(day, month, year) => {
        val calendar = new GregorianCalendar(year.toInt, month.toInt - 1, day.toInt)
        calendar.setTimeZone(timeZone)
        Some(calendar.getTimeInMillis)
      }

      case dateTimeWithPointDelimiter(day, month, year, hours, minutes, seconds) => {
        val calendar = new GregorianCalendar(
          year.toInt,
          month.toInt - 1,
          day.toInt,
          hours.toInt,
          minutes.toInt,
          seconds.toInt
        )
        calendar.setTimeZone(timeZone)
        Some(calendar.getTimeInMillis)
      }

      case _ => None
    }
  }
}
