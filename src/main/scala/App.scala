import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import utils.DateUtils

object App {
  private final val APP_NAME = "Final Project App"
  private final val SPARK_MASTER = "local"

  private final val RAW_USERS_CSV_PATH = "/raw/users/users.csv"
  private final val RAW_PRODUCTS_CSV_PATH = "/raw/products/prods.csv"
  private final val CLEANED_USERS_CSV_PATH = "/cleaned/users/users.csv"
  private final val CLEANED_PRODUCTS_CSV_PATH = "/cleaned/products/prods.csv"
  private final val CONSOLIDATED_ANALYTICS_CSV_PATH = "/analytic/data.csv"

  def getSC(): SparkContext = {
    val conf = new SparkConf().setMaster(SPARK_MASTER).setAppName(APP_NAME)
    new SparkContext(conf)
  }

  def cleanUsers(sc: SparkContext, inputFile: String, outputFile: String) = {
    val text = sc.textFile(inputFile)
    val header = text.first

    text.filter(
      line => {
        if (!line.equals(header)) {
          val rowValues = line.split("\\|")
          rowValues.length >= 5 && rowValues(3).equals("Active")
        } else true
      }
    ).map(
      line => {
        if (!line.equals(header)) {
          val rowLines = line.split("\\|")
          rowLines(4) = DateUtils.transformDateToPosix(rowLines(4)).get.toString
          rowLines.mkString("|")
        } else line
      }
    ).saveAsTextFile(outputFile)
  }

  def cleanProducts(sc: SparkContext, inputFile: String, outputFile: String) = {
    val text = sc.textFile(inputFile)
    val header = text.first()
    text.map(
      line => {
        if (!line.equals(header)) {
          val rowLines = line.split("\\|")
          rowLines(4) = DateUtils.transformDateToPosix(rowLines(4)).get.toString
          rowLines.mkString("|")
        } else line
      }
    ).saveAsTextFile(outputFile)
  }

  def join(sc: SparkContext, userInput: String, productInput: String, joinOutput: String) = {
    val sqlContext = new SQLContext(sc)

    val userDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .option("delimiter", "|")
      .load(userInput)

    val productDF = sqlContext.read.format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .option("delimiter", "|")
      .load(productInput)

    val joinedDF = productDF.join(userDF, productDF("buyer id") === userDF("id"), "inner")
    joinedDF.select(
      productDF("id"),
      productDF("product name"),
      productDF("price"),
      productDF("purchased at"),
      userDF("full name"),
      userDF("phone"),
      userDF("created at"),
      userDF("email")
    )
      .coalesce(1)
      .write
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", "|")
      .save(joinOutput)
  }

  def clean(sc: SparkContext) = {
    cleanUsers(sc, RAW_USERS_CSV_PATH, CLEANED_USERS_CSV_PATH)
    cleanProducts(sc, RAW_PRODUCTS_CSV_PATH, CLEANED_PRODUCTS_CSV_PATH)
  }

  def consolidate(sc: SparkContext) = {
    join(sc, CLEANED_USERS_CSV_PATH, CLEANED_PRODUCTS_CSV_PATH, CONSOLIDATED_ANALYTICS_CSV_PATH)
  }

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) println("At least one parameter needed (clean | consolidate)")
    else if (args(0).equals("clean")) clean(getSC())
    else if (args(0).equals("consolidate")) consolidate(getSC())
    else println("Unknown command")
  }
}
