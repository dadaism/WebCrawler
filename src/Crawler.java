import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.HttpException;


public class Crawler implements Runnable {
	private String url;
	private String pageTitle;
	private String htmlContent = new String();
	private String pageContent = new String();
	private BloomFilter bloomFilter;
	private Queue<String> queue;
	/**
	 * @param args
	 */
	public Crawler(String value, BloomFilter filter, Queue<String> queue) {
		url = value;
		bloomFilter = filter;
		this.queue = queue;
	}
	public String getPageContent() {
		return pageContent;
	}
	@Override
	public void run() {
		//System.out.println("Hello World");
		PageRetriver myPageRetriver = new PageRetriver();
		try {
			char lastCharUrl = url.charAt(url.length()-1);
			boolean status = myPageRetriver.downloadPage(url);
			if ( status ) {
				htmlContent = myPageRetriver.getHtmlContent();
				PageParser myPageParser = new PageParser(htmlContent);
				pageTitle = myPageParser.extractTitle();
				pageContent = myPageParser.extractContent();
				System.out.println(url);
				System.out.println(pageTitle);
				System.out.println(pageContent);
			
				Iterator<String> myIterator = myPageParser.extractURL().iterator();
				String extractedUrl;
				while ( myIterator.hasNext() ) {
					extractedUrl = myIterator.next();
					if ( extractedUrl==null || extractedUrl.length()==0 )	continue;
					char firstCharExtracted = extractedUrl.charAt(0);
					if ( !extractedUrl.startsWith("http") ) {
						//System.out.println(extractedUrl);
						if ( lastCharUrl=='/' && firstCharExtracted=='/' )
							extractedUrl = url + extractedUrl.substring(1);
						else if ( lastCharUrl!='/' && firstCharExtracted!='/' )
							extractedUrl = url + "/" + extractedUrl;
						else
							extractedUrl = url +  extractedUrl;
					}
					//System.out.println(extractedUrl);
					if ( bloomFilter.checkElement(extractedUrl) ) {					
						//System.out.println("Already in Bloom Filter");
						continue;
					}
					else {
						bloomFilter.addElement(extractedUrl);
						queue.add(extractedUrl);
					}
				}
			} 
		}	
		catch (HttpException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}	
	}
	public static void main(String[] args) {
		Crawler crawler = new Crawler("http://www.missouri.edu", new BloomFilter(), new ConcurrentLinkedQueue<String>());
		crawler.run();
	}

}
