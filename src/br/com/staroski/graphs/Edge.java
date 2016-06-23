package br.com.staroski.graphs;

@SuppressWarnings("unchecked")
public class Edge {

	private final Object id;
	private final Vertex destination;
	private final Vertex source;
	private final double weight;

	public Edge(Object id, Vertex source, Vertex destination, double weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Edge) {
			Edge that = (Edge) obj;
			if (this.id == null) {
				return that.id == null;
			}
			return this.id.equals(that.id);
		}
		return false;
	}

	public Vertex getDestination() {
		return destination;
	}

	public <I> I getId() {
		return (I) id;
	}

	public Vertex getSource() {
		return source;
	}

	public double getWeight() {
		return weight;
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
		return String.format("%s -> %s", source.toString(), destination.toString());
	}
}