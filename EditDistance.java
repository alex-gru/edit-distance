import java.lang.Math.*;
import java.time.*;
import java.util.HashMap;

/**
* @author: Alexander Gruschina
*
* Preparation for final exam on "Similarity Search VO"
* held by Prof. Nikolaus Augsten (Database Research Group)
* Dept. of CS, University of Salzburg, Austria
* Course website: https://dbresearch.uni-salzburg.at/teaching/2016ws/ssdb/
* 
* Investigating approaches for computing Edit Distance of 2 strings
* - brute force (exponential) 
* - dynamic programming O(m*n) time+space
* - dynamic programming with optimized space complexity O(m) (wlog m < n)
**/

public class EditDistance {

	static boolean DEBUG = false;
	static boolean DEBUG_DYN_ARRAY = false;
	static int recursions = 0;

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java EditDistance <string1> <string2>");
			return;
		}
		String x = args[0];
		String y = args[1];

		System.out.print("Computing Edit Distance (Levenshtein) of: ");
		System.out.print("'" + x + "',");		
		System.out.println("'" + y + "'");

		// brute force
		System.out.println("Running brute force without caching...");
		long start = System.currentTimeMillis();
		int ed_bf = ed_bf(x,y);
		float time = System.currentTimeMillis() - start;
		System.out.print("\tDistance: " + ed_bf);
		System.out.print(", took " + time + " ms");
		System.out.println(" (" + recursions + " recursions)");

		// dynamic programming
		System.out.println("Running dynamic programming...");
		start = System.currentTimeMillis();
		int ed_dyn = ed_dyn(x,y);
		time = System.currentTimeMillis() - start;
		System.out.print("\tDistance: " + ed_bf);
		System.out.println(", took " + time + " ms");		

		// dynamic programming with space optimization
		System.out.println("Running dynamic programming with space optimization...");
		start = System.currentTimeMillis();
		int ed_dyn_reduced_space = ed_dyn_reduced_space(x,y);
		time = System.currentTimeMillis() - start;
		System.out.print("\tDistance: " + ed_dyn_reduced_space);
		System.out.println(", took " + time + " ms");	
	}

	public static int ed_bf(String x, String y) {
		recursions++;

		int m = x.length();
		int n = y.length();
		int c = 0;

		if (DEBUG) {
			System.out.println("m: " + m + ", n: " + n);
			System.out.println("x: " + x + ", y: " + y);
		}

		if (m == 0) return n;
		if (n == 0) return m;
		if (x.charAt(m - 1) == y.charAt(n - 1)) c = 0; else c = 1;

		String x_ = (m-1) > 0 ? x.substring(0,m-1) : "";
		String y_ = (n-1) > 0 ? y.substring(0,n-1) : "";

		return Math.min(
			Math.min(
				ed_bf(x, y_) + 1,
				ed_bf(x_, y) + 1),
			ed_bf(x_,y_) + c
			);
	}

	public static int ed_dyn(String x, String y) {
		int m = x.length();
		int n = y.length();
		int c = 0;
		int[][] array = new int[m+1][n+1];

		for (int i = 0; i < m; i++) array[i][0] = i;
		for (int j = 1; j < n; j++) array[0][j] = j;

		for (int j = 1; j < n; j++) {
			for (int i = 1; i < m; i++) {
				if (x.charAt(i) == y.charAt(j)) c = 0; else c = 1;

				array[i][j] = Math.min(
					Math.min(
						array[i][j-1] + 1,
						array[i-1][j] + 1),
					array[i-1][j-1] + c
					);
			}
		}

		if (DEBUG_DYN_ARRAY) {
			for (int j = 0; j < n; j++) {
				for (int i = 0; i < m; i++) {
					System.out.println(array[i][j]);
				}
			}
		}
		return array[m-1][n-1];
	}

	public static int ed_dyn_reduced_space(String x, String y) {
		int m = x.length();
		int n = y.length();
		int c = 0;
		int[] col0 = new int[m+1];
		int[] col1 = new int[m+1];

		for (int i = 0; i < m; i++) col0[i] = i;

		for (int j = 1; j < n; j++) {
			col1[0] = j;
			for (int i = 1; i < m; i++) {
				if (x.charAt(i) == y.charAt(j)) c = 0; else c = 1;

				col1[i] = Math.min(
					Math.min(
						col0[i] + 1,
						col1[i-1] + 1),
					col0[i-1] + c
					);
			}
			col0 = col1;
			col1 = new int[m+1];
		}

		return col0[m-1];
	}

}
