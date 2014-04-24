package io.github.jto.models;

import javax.validation.constraints.*;

public class Similar {

	@NotNull
	private String id;

	@Min(0)
	@Max(1)
	private float score;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}
}