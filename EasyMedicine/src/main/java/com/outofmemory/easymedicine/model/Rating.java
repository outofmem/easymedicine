/**
 * 
 */
package com.outofmemory.easymedicine.model;

/**
 * The enum for rating an entity
 * 
 * @author pribiswas
 * 
 */
public enum Rating {
	FIVE_STAR(5), FOUR_STAR(4), THREE_STAR(3), TWO_STAR(2), ONE_STAR(1);

	private final int rating;

	Rating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

}
