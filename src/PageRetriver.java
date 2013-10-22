import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import sun.misc.IOUtils;

import java.io.*;

public class PageRetriver {
	private String htmlContent;
	/**
	 * @param args
	 */
	public PageRetriver() {
		htmlContent = new String();
	}
	public boolean downloadPage(String url) throws HttpException, 
			IOException {
		InputStream input = null;
		OutputStream output = null;
		HttpGet httpget = new HttpGet(url);
		//System.out.println(httpget.getURI());
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(httpget);
		int httpStatusCode = response.getStatusLine().getStatusCode();
		if ( HttpStatus.SC_OK == httpStatusCode ) {
			Header contentType = response.getFirstHeader("Content-Type");
			String mimeType = contentType.getValue().split(";")[0].trim();
			/* Currently only parser html */
			if ( !mimeType.startsWith("text/html") )
				return false;
			//System.out.println(mimeType);
			HttpEntity entity = response.getEntity();
			input = entity.getContent();
			//System.out.println(response);
		
			BufferedReader br = new BufferedReader( new InputStreamReader(input));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			//String page = sb.toString();
			//pageContent = page;
			htmlContent = sb.toString();
			//System.out.println(pageContent);
			br.close();
			return true;
		}
		return false;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public static void main(String[] args) {
		PageRetriver myPageRetriver = new PageRetriver();
		// TODO Auto-generated method stub
		try {
			//myPageRetriver.downloadPage("http://www.danielbit.com/~dali/");
			//myPageRetriver.downloadPage("http://www.apache.com/");
			myPageRetriver.downloadPage("http://www.missouri.edu");
			System.out.println(myPageRetriver.getHtmlContent());
		} catch (HttpException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
