import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class MasterCrawler {
	private static BloomFilter myBloomFiler = new BloomFilter();
	private static Queue<String> myQueue = new ConcurrentLinkedQueue<String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		myQueue.add("http://www.missouri.edu");
		int iteration = 0;
		while ( !myQueue.isEmpty() ) {
			String url = myQueue.poll();
			//System.out.println(url);
			Crawler myCrawler = new Crawler(url, myBloomFiler, myQueue);
			myCrawler.run();
			iteration++;
			//if (iteration==10)
			//	break;
		}
		System.out.println("Web Crawler Finished!!");
	}

}
