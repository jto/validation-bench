package io.github.jto.scala

import java.util.Date
import play.api.libs.json._
import play.api.data.mapping._
import play.api.data.mapping.json.Rules._

package models {
	case class Similar(id: String, score: Float)
	case class Tag(id: String, score: String)
	case class Track(id: String, title: String, timestamp: org.joda.time.DateTime, artist: String, similars: Seq[Similar], tags: Seq[Tag])

	object Rules {
		implicit val similarR = From[JsValue] { __ =>
			((__ \ "id").read[String] and
			 (__ \ "score").read(min(0f) |+| max(1f)))(Similar.apply _)
		}

		implicit val tagR = From[JsValue] { __ =>
			((__ \ "id").read[String] and
			 (__ \ "score").read[String])(Tag.apply _)
		}

		implicit val trackR = From[JsValue] { __ =>
			((__ \ "id").read[String] and
			 (__ \ "title").read[String] and
			 (__ \ "timestamp").read(jodaDateRule("yyyy-MM-dd HH:mm:ss.SSSSSS")) and
			 (__ \ "artist").read[String] and
			 (__ \ "similars").read[Seq[Similar]] and
			 (__ \ "tags").read[Seq[Tag]])(Track.apply _)
		}
	}
}