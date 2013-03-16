package clustering.mitosis;

import java.util.Vector;

public class QuickSort {

	public QuickSort() {
	};

	public void quicksort(Vector<Association> a, int lo, int hi) {
		// lo is the lower index, hi is the upper index
		// of the region of array a that is to be sorted
		int i = lo, j = hi;
		double h;
		int p1;
		int q1;

		// comparison element x
		double x = a.get((lo + hi) / 2).getDis();

		// partition
		do {
			while (a.get(i).getDis() < x)
				i++;
			while (a.get(j).getDis() > x)
				j--;
			if (i <= j) {
				h = a.get(i).getDis();
				p1 = a.get(i).getP();
				q1 = a.get(i).getQ();

				a.get(i).setDis(a.get(j).getDis());
				a.get(i).setP(a.get(j).getP());
				a.get(i).setQ(a.get(j).getQ());

				a.get(j).setP(p1);
				a.get(j).setQ(q1);
				a.get(j).setDis(h);

				i++;
				j--;
			}
		} while (i <= j);

		// recursion
		if (lo < j)
			quicksort(a, lo, j);
		if (i < hi)
			quicksort(a, i, hi);
	}

	public static void main(String a[]) {

		QuickSort q = new QuickSort();
		int i;
		Vector<Association> array = new Vector<Association>();

		array.add(new Association(1, 1, 0.7));
		array.add(new Association(1, 2, 0.1));
		array.add(new Association(1, 3, 0.8));
		array.add(new Association(1, 4, 0.2));
		array.add(new Association(1, 5, 0.3));
		array.add(new Association(1, 6, 0.1));

		q.quicksort(array, 0, array.size() - 1);
		for (i = 0; i < array.size(); i++)
			System.out.println(array.get(i).getDis() + " "
					+ array.get(i).getQ());
	}

}
