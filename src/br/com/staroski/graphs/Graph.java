package br.com.staroski.graphs;

import java.util.List;

public class Graph {

	private final List<Edge> edges;
	private final List<Vertex> vertexes;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

}
