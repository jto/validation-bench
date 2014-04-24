package io.github.jto.models;

import java.util.List;
import org.joda.time.DateTime;
import javax.validation.constraints.*;
import javax.validation.Valid;

public class Track {

	@NotNull
	private String id;
	@NotNull
	private String title;
	@NotNull @Valid
	private List<Similar> similars;
	@NotNull
	private DateTime timestamp;
	@NotNull
	private String artist;
	@NotNull @Valid
	private List<Tag> tags;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Similar> getSimilars() {
		return this.similars;
	}

	public void setSimilars(List<Similar> similars) {
		this.similars = similars;
	}

	public List<Tag> getTags() {
		return this.tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public DateTime getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getArtist() {
		return this.artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}