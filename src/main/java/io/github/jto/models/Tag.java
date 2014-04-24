package io.github.jto.models;

import javax.validation.constraints.*;

public class Tag {

	private String id;
	private String score;

	@NotNull
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@NotNull
	public String getScore() {
		return this.score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}