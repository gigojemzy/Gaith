package clustering.mitosis;

import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class Phase2 {

	private Vector<Association> l1;
	private Vector<Association> l2;
	private Map<Integer, MyCluster> clusterMap;
	double K;
	private Map<Integer, Double> averageDistanceMap;
	private Vector<Integer> deleteditems;
	private QuickSort qq;

	public Phase2(double K, Vector<Association> l1,
			Map<Integer, Double> averageDistanceMap) {
		this.K = K;
		this.l1 = l1;
		this.averageDistanceMap = averageDistanceMap;
		initialize();
		_DO();
	};

	public Vector<Association> getL1() {
		return this.l1;
	}

	public Vector<Association> getL2() {
		return this.l2;
	}

	public Map<Integer, MyCluster> getClusterMap() {
		return this.clusterMap;
	}

	private void initialize() {
		clusterMap = new HashMap<Integer, MyCluster>();
		deleteditems = new Vector<Integer>();
		qq = new QuickSort();
	}

	private void _DO() {
		MergePatternsIntoClusters();
		refineStepOne();
	}

	private int searchInCluList(int pattern) {
		for (MyCluster myCluster : clusterMap.values())
			if (myCluster.getPatterns().contains(pattern))
				return myCluster.getId();
		return -1;
	}

	private int getClusterId(int pattern) {
		int res = searchInCluList(pattern);
		if (res == -1) {
			MyCluster c = new MyCluster();
			c.setId(pattern);
			Vector<Integer> a = new Vector<Integer>();
			a.add(pattern);
			c.setPatterns(a);
			c.setAveDis(averageDistanceMap.get(pattern));
			clusterMap.put(pattern, c);
			return pattern;
		} else
			return res;
	}

	private void combineTwoArrayLists(Vector<Association> a1,
			Vector<Association> a2) {
		for (int i = 0; i < a2.size(); i++) {
			if (!a1.contains(a2.get(i)))
				a1.add(a2.get(i));
		}
	}

	private void MergePatternsIntoClusters() {
		l2 = new Vector<Association>();
		double newAve;
		for (int i = 0; i < l1.size(); i++) {
			Association a = l1.get(i);
			int p = a.getP();
			int q = a.getQ();
			double d = a.getDis();
			MyCluster c1 = clusterMap.get(getClusterId(p));
			MyCluster c2 = clusterMap.get(getClusterId(q));

			if ((d < (K * (Math.min(c1.getAveDis(), c2.getAveDis()))))
					&& ((Math.max(c1.getAveDis(), c2.getAveDis())) < (K * (Math
							.min(c1.getAveDis(), c2.getAveDis()))))) {

				if (c1.getId() != c2.getId()) {
					newAve = d + (c1.aveDis * c1.getClusterList().size())
							+ (c2.aveDis * c2.getClusterList().size());

					MyCluster c1Temp = c1;
					MyCluster c2Temp = c2;
					if (c1.getId() > c2.getId()) {
						c1Temp = c1;
						c2Temp = c2;
					} else {
						c1Temp = c2;
						c2Temp = c1;
					}
					combineTwoArrayLists(c1Temp.getClusterList(),
							c2Temp.getClusterList());
					if (!c1Temp.getClusterList().contains(a))
						c1Temp.getClusterList().add(a);
					qq.quicksort(c1Temp.getClusterList(), 0, c1Temp
							.getClusterList().size() - 1);
					c1Temp.setAveDis(newAve / c1Temp.getClusterList().size());
					Vector<Integer> c2Patterns = c2Temp.getPatterns();
					for (Integer c2Pattern : c2Patterns)
						if (!c1Temp.getPatterns().contains(c2Pattern))
							c1Temp.getPatterns().add(c2Pattern);
					clusterMap.remove(c2Temp.getId());
				} else {
					if (!c1.getClusterList().contains(a)) {
						newAve = d + (c1.aveDis * c1.getClusterList().size());
						c1.getClusterList().add(a);
						qq.quicksort(c1.getClusterList(), 0, c1
								.getClusterList().size() - 1);
						c1.setAveDis(newAve / c1.getClusterList().size());
					}
				}
				deleteditems.add(i);
				l2.add(l1.get(i));
			}
		}
		Vector<Association> temp = new Vector<Association>();
		for (int i = 0; i < l1.size(); i++) {
			if (!deleteditems.contains(i)) {
				temp.add(l1.get(i));
			}
		}
		l1 = temp;
	}

	private void refineStepOne() {
		deleteditems.clear();
		double newAve = 0.0;
		for (int i = 0; i < l1.size(); i++) {
			Association a = l1.get(i);
			int p = a.getP();
			int q = a.getQ();
			double d = a.getDis();
			MyCluster c1 = clusterMap.get(getClusterId(p));
			MyCluster c2 = clusterMap.get(getClusterId(q));
			if (c1.getId() == c2.getId()) {
				if (d < K * c1.getAveDis()) {
					if (!c1.getClusterList().contains(a)) {
						newAve = d + (c1.aveDis * c1.getClusterList().size());
						c1.getClusterList().add(a);
						c1.setAveDis(newAve / c1.getClusterList().size());
						qq.quicksort(c1.getClusterList(), 0, c1
								.getClusterList().size() - 1);
					}
					l2.add(l1.get(i));
					deleteditems.add(i);
				}
			}
		}
		Vector<Association> temp = new Vector<Association>();
		for (int i = 0; i < l1.size(); i++) {
			if (!deleteditems.contains(i)) {
				temp.add(l1.get(i));
			}
		}
		l1 = temp;
		qq.quicksort(l2, 0, l2.size() - 1);
	}
}