package searchEngine;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Example program to list links from a URL.
 */
public class Crawler {
	public static void main(String[] args) throws IOException {
		System.out.println("Enter the keyword to search for");
		Scanner keyWord = new Scanner(System.in);
		String url = new String("http://www.oldwestbury.edu/");
		HashMap<String,List<String>> dataGathered = new HashMap();
		String search = keyWord.next();
		print("Fetching %s...", url);

		Document doc = Jsoup.connect(url).userAgent("Chrome").get();
		if(doc.text().contains(search))
		{
			System.out.println("true");
		}

		Elements links = doc.select("a[href]");

		//print("\nLinks: (%d)", links.size());
		for (Element link : links) {

			Document doc2 = Jsoup.connect(link.attr("abs:href")).userAgent("Mozilla").ignoreHttpErrors(true).timeout(15*1000).get();
			//System.out.println(link.attr("abs:href"));
			String Url = new String(link.attr("abs:href"));
			if(doc2.text().contains(search))
			{
				System.out.println("true");

				List<String> listOfLinks = new ArrayList<>();
				Elements tempLinks = doc2.select("a[href]");	
				for(Element temper: tempLinks)
				{
					listOfLinks.add(temper.attr("abs:href"));
				}
				dataGathered.put(Url, listOfLinks);
			}

		}
		if(!(dataGathered.size() > 0) )
		{
			System.out.println("no pages found with that keyword");
		}
		else
		{
			double damper = .85;
			PageRank pageRanker = new PageRank(damper,dataGathered);
			pageRanker.Ranker();
			pageRanker.sortRanked();
			System.out.println(pageRanker.getRanked().toString());
			pageRanker.printRanks();
		}
		System.out.println(dataGathered.size());
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	//    private static String trim(String s, int width) {
	//        if (s.length() > width)
	//            return s.substring(0, width-1) + ".";
	//        else
	//            return s;
	//    }
}