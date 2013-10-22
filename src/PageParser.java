import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.*;

public class PageParser {
	private String pageContent;
	private static final String REG_URL_LINK = 
			"\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private static final String REG_PAGE_TAG = 
			//"(?i)<a([^>]+)>(.+?)</a>";
			"<[^>]+>";
	private static final String REG_SCRIPT_TAG =
			//"<\\s*(?i)script[^>]*>(.+?)</script[^>]*>";
			"<\\s*(?i)script[^>]*>[^<]*</script[^>]*>";
			//"<\\s*script[^>]*>";
	private static final String REG_TITLE_TAG = 
			"<\\s*(?i)title[^>]*>[^<]*</title[^>]*>";
	private Pattern patternUrlLink;
	private Pattern patternTag;
	private Pattern patternScriptTag;
	private Pattern patternTitleTag;
	private Matcher matcherUrlLink;
	private Matcher matcherTag;
	private Matcher matcherScriptTag;
	private Matcher matcherTitleTag;
	/**
	 * @param args
	 */
	public PageParser(String value) {
		pageContent = value;
		patternUrlLink = Pattern.compile(REG_URL_LINK);
		patternTag = Pattern.compile(REG_PAGE_TAG);
		patternScriptTag = Pattern.compile(REG_SCRIPT_TAG);
		patternTitleTag = Pattern.compile(REG_TITLE_TAG);
		matcherUrlLink = patternUrlLink.matcher(pageContent);
		matcherTitleTag = patternTitleTag.matcher(pageContent);
	}
	public List<String> extractURL() {
		List<String> rst = new ArrayList<String>();
		String url;
		while ( matcherUrlLink.find() ) {
			url = matcherUrlLink.group(0).replace("href=", "").replace("\"", "").replace("'", "").replace(" ", "");
			/*if (	url.endsWith(".css")
				|| 	url.endsWith(".pdf")
				||	url.endsWith(".ppt")
				||	url.endsWith(".doc")
				||	url.endsWith(".wmv")
				)
				continue;*/
			if (url.contains("#"))
				continue;
			if (url.contains("missouri.edu"))
				rst.add( url );
		}
		return rst;
	}
	public String extractTitle() {
		String pageTitle;
		if ( matcherTitleTag.find() ) {
			pageTitle = matcherTitleTag.group(0);
			//pageTitle.replaceAll("<\\s*(?i)title[^>]*>", "");
			return pageTitle.replaceAll("<\\s*(?i)title[^>]*>", "").replaceAll("</title[^>]*>", "").replaceAll("^[\\s]*", "");
		}
		return "No Title";
	}
	public String removeScript() {
		matcherScriptTag = patternScriptTag.matcher(pageContent);
		String rst = matcherScriptTag.replaceAll("");
		//System.out.println(rst);
		return rst;
	}
	public String extractContent() {
		matcherTag = patternTag.matcher(removeScript());
		//matcherTag = patternTag.matcher(pageContent);
		String rst = matcherTag.replaceAll("");
		return rst.replaceAll("(\\s)+", " ");
	}
	public static void main(String[] args) {
		String value = "<script>var _asdf[]</script><a href=\"www.danielbit.com\"></a><href=>this is text1 <a href='mkyong.com' target='_blank'>hello</a> this is text2...";
		System.out.println(value);
		PageParser pageParser = new PageParser(value);
		Iterator<String> myIterator = pageParser.extractURL().iterator();
		while ( myIterator.hasNext() ) {
			System.out.println(myIterator.next());
		}
		//System.out.println(pageParser.extractURL());
		System.out.println(pageParser.extractContent());
		
	}
}
