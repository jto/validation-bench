package io.github.jto

import org.scalameter.api._

object RangeBenchmark extends PerformanceTest.HTMLReport {

  override def tester: RegressionReporter.Tester = RegressionReporter.Tester.OverlapIntervals()
  override def historian: RegressionReporter.Historian = ???
  override def online = true
  override def reporter: Reporter = HtmlReporter(!online)


  val sizes = Gen.range("size")(1000, 10000, 1000)

  measure method "Scala" in {
    import io.github.jto.scala._
    import _root_.scala.io._
    import play.api.libs.json._
    import play.api.data.mapping._
    import play.api.data.mapping.json.Rules._
    import models._
    import models.Rules._

  	using(sizes) curve("naive") in { s =>
      import models._
			import _root_.scala.io._
			val source = Source.fromFile("/Users/jto/Documents/validation-bench/src/test/resources/clean.json")
			source.getLines.take(s).foldLeft((0, 0)) { case ((f, s), l) =>
				From[JsValue, Track](Json.parse(l)).fold(
					_ => (f + 1, s),
					_ => (f, s + 1))
			}
			source.close()
		}

    using(sizes) curve("naive-invalids") in { s =>
      import models._
      import _root_.scala.io._
      val source = Source.fromFile("/Users/jto/Documents/validation-bench/src/test/resources/invalids.json")
      source.getLines.take(s).foldLeft((0, 0)) { case ((f, s), l) =>
        From[JsValue, Track](Json.parse(l)).fold(
          _ => (f + 1, s),
          _ => (f, s + 1))
      }
      source.close()
    }

    using(sizes) curve("par") in { s =>
      val source = Source.fromFile("/Users/jto/Documents/validation-bench/src/test/resources/clean.json")
      source.getLines.take(s).toStream.par.aggregate((0, 0))({ case ((f, s), l) =>
        From[JsValue, Track](Json.parse(l)).fold(
          _ => (f + 1, s),
          _ => (f, s + 1))
      }, { case ((f1, s1), (f2, s2)) => (f1 + f2, s1 + s2) })
      source.close()
    }

    using(sizes) curve("par-invalids") in { s =>
      val source = Source.fromFile("/Users/jto/Documents/validation-bench/src/test/resources/invalids.json")
      source.getLines.take(s).toStream.par.aggregate((0, 0))({ case ((f, s), l) =>
        From[JsValue, Track](Json.parse(l)).fold(
          _ => (f + 1, s),
          _ => (f, s + 1))
      }, { case ((f1, s1), (f2, s2)) => (f1 + f2, s1 + s2) })
      source.close()
    }
  }

  measure method "Java" in {
  	using(sizes) curve("JSR-303") in { s =>
      new io.github.jto.JavaBench().run(new java.io.File("/Users/jto/Documents/validation-bench/src/test/resources/clean.json"), s)
    }

    using(sizes) curve("JSR-303-invalids") in { s =>
      new io.github.jto.JavaBench().run(new java.io.File("/Users/jto/Documents/validation-bench/src/test/resources/invalids.json"), s)
    }
  }

}
