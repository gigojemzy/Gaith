package semanticAnnotation.SA;

import java.util.Arrays;
import java.util.PriorityQueue;

public class SpreadingActivation {

	// Priority Queue state used in annotate method
	private static class State implements Comparable<State> {

		double weight;
		int id;

		public State(double weight, int id) {
			super();
			this.weight = weight;
			this.id = id;
		}

		public int compareTo(State o) {
			return Double.compare(o.weight, weight);
		}

	}

	private static final double NO_EDGE = -1.0;
	private static final double EPS = 1e-9;
	private static final double MAX = 1.0;

	private double decay;
	private double fire;
	private int concepts;

	private double[][] conceptToConceptGraph;

	public SpreadingActivation(double decay, double fire, int concepts) {
		super();
		this.decay = decay;
		this.fire = fire;
		this.concepts = concepts;
		conceptToConceptGraph = new double[concepts][concepts];
		for (int i = 0; i < conceptToConceptGraph.length; i++)
			Arrays.fill(conceptToConceptGraph[i], NO_EDGE);
	}

	public void addRelation(int i, int j, double d) {
		conceptToConceptGraph[i][j] = d;
	}

	// Given weighted concept in document (No repeated firing)
	public boolean[] annoutate(double[] docConcept) {
		boolean[] fired = new boolean[concepts];

		PriorityQueue<State> waiting = new PriorityQueue<SpreadingActivation.State>();

		for (int i = 0; i < docConcept.length; i++)
			if (isFired(docConcept[i]))
				waiting.add(new State(docConcept[i], i));

		while (!waiting.isEmpty()) {
			State s = waiting.poll();

			if (fired[s.id])
				continue;
			fired[s.id] = true;

			for (int i = 0; i < conceptToConceptGraph[s.id].length; i++) {
				if (isRelated(s.id, i) && !fired[i]) {
					docConcept[i] += docConcept[s.id]
							* conceptToConceptGraph[s.id][i] * decay;
					docConcept[i] = Math.min(docConcept[i], MAX);
					if (isFired(docConcept[i]))
						waiting.add(new State(docConcept[i], i));
				}
			}
		}

		return fired;
	}

	private boolean isRelated(int i, int j) {
		return conceptToConceptGraph[i][j] != NO_EDGE;
	}

	private boolean isFired(double d) {
		return Double.compare(d, fire) > -EPS;
	}

	public static void main(String[] args) {
		SpreadingActivation a = new SpreadingActivation(0.5, 0.5, 7);
		a.addRelation(0, 1, 0.9);
		a.addRelation(0, 2, 0.9);
		a.addRelation(1, 3, 0.9);
		a.addRelation(1, 4, 0.9);
		a.addRelation(2, 3, 0.9);
		a.addRelation(2, 5, 0.9);
		a.addRelation(5, 0, 0.9);
		a.addRelation(5, 6, 0.9);
		boolean[] ann = a
				.annoutate(new double[] { 1.0, 0, 0.75, 0, 0, 0.25, 0 });
		for (int i = 0; i < ann.length; i++) {
			System.out.println(ann[i]);
		}
	}

}
