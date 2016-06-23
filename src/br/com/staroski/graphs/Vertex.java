package br.com.staroski.graphs;

@SuppressWarnings("unchecked")
public class Vertex {

	private final Object id;
	private final Object value;

	public Vertex(Object id) {
		this(id, null);
	}

	public Vertex(Object id, Object value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Vertex) {
			Vertex that = (Vertex) obj;
			if (this.id == null) {
				return that.id == null;
			}
			return this.id.equals(that.id);
		}
		return false;
	}

	public <I> I getId() {
		return (I) id;
	}

	public <V> V getValue() {
		return (V) value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Object id = getId();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.valueOf(getId());
	}

}