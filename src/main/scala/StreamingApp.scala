import java.util.Date

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils
import twitter4j.Status
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

object StreamingApp extends App {

  val conf = new SparkConf().setAppName("streamming-app").setMaster("local[2]")
  val ssc = new StreamingContext(conf, Seconds(5))

  val consumerKey = sys.env("TWITTER_CONSUMER_KEY")
  val consumerSecret = sys.env("TWITTER_CONSUMER_SECRET")
  val accessToken = sys.env("TWITTER_ACCESS_SECRET")
  val accessTokenSecret = sys.env("TWITTER_ACCESS_TOKEN_SECRET")

  val cb = new ConfigurationBuilder
  cb.setDebugEnabled(false)
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessTokenSecret)
    .setTweetModeExtended(true)
    .setIncludeEmailEnabled(false)
    .setIncludeEntitiesEnabled(false)
    .setIncludeExtAltTextEnabled(false)
    .setIncludeMyRetweetEnabled(false)
    .setAsyncNumThreads(1)

  val auth = new OAuthAuthorization(cb.build)
  val tweets = TwitterUtils
                  .createStream(ssc, Some(auth))
                  .filter(filterFunction)
                  .map(transform)

  tweets.saveAsTextFiles("tweets/part", "")
  ssc.start()
  ssc.awaitTermination()

  def filterFunction(tweet:Status):Boolean = {
    tweet.getLang=="pt" && !tweet.isTruncated && !tweet.getText.contains("http") && !tweet.getText.contains("www")
  }

  def transform(tweet:Status) = {
    tweet.getText.replace("\n", " ")
  }

}

