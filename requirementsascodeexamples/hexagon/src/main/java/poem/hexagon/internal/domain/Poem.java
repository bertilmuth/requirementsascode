package poem.hexagon.internal.domain;

public class Poem {
	private String verses;

	public Poem(String verses) {
		this.verses = verses;
	}

	public String getVerses() {
		return verses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((verses == null) ? 0 : verses.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Poem other = (Poem) obj;
		if (verses == null) {
			if (other.verses != null)
				return false;
		} else if (!verses.equals(other.verses))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return verses;
	}
}
