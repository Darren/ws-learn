import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc._
import scala.concurrent._
import ExecutionContext.Implicits.global


object Main extends App {
  implicit val system = ActorSystem("Test")
  implicit val mat: akka.stream.Materializer = ActorMaterializer()


  override def main(args: Array[String]): Unit = {
    val wsClient = AhcWSClient()

    wsClient.url("http://www.sina.com.cn/")
      .addHttpHeaders("User-Agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36" )
      .get()
      .map (
        response => {
          response.headers.foreach(println)
          println(response.body)
        }
      )
  }
}
