import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.jsoup.Jsoup
import play.api.libs.ws.ahc._

import scala.concurrent.ExecutionContext.Implicits.global


object Main extends App {
  implicit val system = ActorSystem("Test")
  implicit val mat: akka.stream.Materializer = ActorMaterializer()


  def detectAndGetBody(url: String, body: Array[Byte], contentType: String): String = {
    val is = new ByteArrayInputStream(body)
    val idx = contentType.indexOf("charset=")
    var charset = "UTF-8"
    if (idx > 0) {
      charset = contentType.substring(idx + 8)
    }
    System.out.println(charset)
    val doc = Jsoup.parse(is, charset, url);
    is.close()
    return doc.body().html()
  }

  override def main(args: Array[String]): Unit = {
    val wsClient = AhcWSClient()

    val url = "http://www.sina.com.cn/"

    wsClient.url(url)
      .addHttpHeaders("User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36")
      .get()
      .map(
        response => {
          response.headers.foreach(println)
          println(detectAndGetBody(url, response.bodyAsBytes.toArray, response.header("Content-Type").getOrElse("")))
        }
      )
  }
}
