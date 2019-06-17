import org.clapper.argot.ArgotParser

class Parser(args:Array[String], name:String) {
  val parser = new ArgotParser(name)

  private val confsParse =
    parser.multiOption[Map[String, String]](List("c", "conf"), "configuration", "Add a custom configuration flag as key=value"){
      (sValue, opt) => {
        val Seq(k, v) = sValue.split("=").toSeq
        Map[String, String](k -> v)
      }
    }

  def confs = confsParse.value.reduceOption((u, v) => u ++ v).getOrElse(Map[String, String]())

  // Helpers
  private def stringParser(name:String, short:String = "", valueName:String = "", default:String="", help:String="") ={
    val names = if(short.isEmpty) List(name) else List(short, name)
    parser.option[String](names, valueName, help){
      (sValue, _) => if(sValue != null) sValue else default
    }
  }

  parser.parse(args)
}
