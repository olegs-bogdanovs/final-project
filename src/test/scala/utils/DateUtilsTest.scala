package utils

import org.scalatest.FunSuite

class DateUtilsTest extends FunSuite {

  test("testTransformDateToPosix") {
    assert(DateUtils.transformDateToPosix("2019-12-31").get === 1577750400000L)
    assert(DateUtils.transformDateToPosix("31.12.2019").get === 1577750400000L)
    assert(DateUtils.transformDateToPosix("31122019").get === 1577750400000L)
    assert(DateUtils.transformDateToPosix("31.12.2019 00:00:00").get === 1577750400000L)
    assert(DateUtils.transformDateToPosix("Unknown Format").isEmpty === true)
  }

}
