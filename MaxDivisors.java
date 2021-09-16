package util;

import java.util.Scanner;

/***
 * This Program will ask for upperbound and number of threads from the user
 * for searching the number with the maximum divisors and will print it 
 * with the elapsed time.
 * @author 97254
 *
 */
public class MaxDivisors {
	private int maxdvisors = 0;
	private int num = 0;
	private  long start;
	public void setStart(long start) {
		this.start = start;
	}

	private long end;

	private class DevMax implements Runnable {
		private int from, to;
		private int myMaxNum = 0, numOfDivsiors = 0;

		public int getMyMaxNum() {
			return myMaxNum;
		}

		public void setMyMaxNum(int myMaxNum) {
			this.myMaxNum = myMaxNum;
		}

		public int getNumOfDivsiors() {
			return numOfDivsiors;
		}

		public void setNumOfDivsiors(int numOfDivsiors) {
			this.numOfDivsiors = numOfDivsiors;
		}

		private DevMax(int from, int to) {
			this.from = from;
			this.to = to;
		}

		private int devCounter(int n) {
			int cnt = 0;
			for (int i = 1; i <= Math.sqrt(n); i++) {
				if (n % i == 0) {
					// If divisors are equal,
					// count only one
					if (n / i == i)
						cnt++;

					else // Otherwise count both
						cnt = cnt + 2;
				}
			}
			return cnt;
		}

		private synchronized void updateGeneral(int devnum, int newNum) {
			while (maxdvisors < devnum) {
				maxdvisors = devnum;
				num = newNum;
			}
		}

		private void updateMyMax(int devnum, int num) {
			if (devnum > this.getNumOfDivsiors()) {
				this.setNumOfDivsiors(devnum);
				this.setMyMaxNum(num);
			}
		}

		@Override
		public void run() {
			for (int i = from; i < to; i++)
				updateMyMax(devCounter(i), i);
			updateGeneral(getNumOfDivsiors(), getMyMaxNum());
		}

	}

	public void createThreads(int nThreads, int upperBound) {
		Thread myThreads[] = new Thread[nThreads];
		int steps = upperBound/nThreads,from,to;
		for (int i = 0; i < nThreads; i++) {
			from = i+steps*i;
			to = i == (nThreads-1) ? upperBound : steps*(i+1);
			myThreads[i] = new Thread(new DevMax(from,to));
			myThreads[i].start();
		}
		setStart(System.currentTimeMillis());
		for (int i = 0; i < nThreads; i++) 
			try {
				myThreads[i].join();
			} catch (InterruptedException e) {
			}
		setEnd(System.currentTimeMillis());
		System.out.println(String.format("The number with max divisors is: %d with %d divisors! \n The search took %.3f sec "
				,num,maxdvisors,(getEnd() - getStart())*0.001));
	}
	
	public static void main(String[] args) {
		String threadsNum,upperBound;
		MaxDivisors mds = new MaxDivisors();
		@SuppressWarnings("resource")
		Scanner myscan = new Scanner(System.in);
		System.out.println("Insert upperbound please: ");
		upperBound = myscan.nextLine();
		System.out.println("Insert Num of threads please: ");
		threadsNum = myscan.nextLine();
		mds.createThreads(Integer.parseInt(threadsNum), Integer.parseInt(upperBound));
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
}
