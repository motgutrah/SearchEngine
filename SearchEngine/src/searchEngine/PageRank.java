package searchEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class PageRank {

	private HashMap<String,List<String>> unranked;
	private HashMap<String,Double> ranked;
	private double dampFact;
	private int unrankedSize;
	public PageRank(double damp, HashMap<String,List<String>> unrank)
	{
		setUnranked(unrank);
		setDampFact(damp);
		ranked = new HashMap<>();
		unrankedSize=unrank.size();
	}
	public HashMap<String,Double> getRanked() {
		return ranked;
	}
	public void setRanked(HashMap<String,Double> ranked) {
		this.ranked = ranked;
	}
	public HashMap<String,List<String>> getUnranked() {
		return unranked;
	}
	public void setUnranked(HashMap<String,List<String>> unranked) {
		this.unranked = unranked;
	}
	public double getDampFact() {
		return dampFact;
	}
	public void setDampFact(double dampFact) {
		this.dampFact = dampFact;
	}
	/**
	 * Ranker Method
	 * ranks the pages that we have gathered using the crawler
	 */
	public void Ranker()
	{
		//initialize every page with a rank of 1/n 
		for(String initial:unranked.keySet())
		{
			ranked.put(initial, 1.0/unrankedSize);

		}
		//start ranking
		double newRank;
		for(String rankUrl:unranked.keySet())
		{
			newRank =ranked.get(rankUrl);

			for(String updateUrlRank: unranked.keySet())
			{
				if(!rankUrl.equals(updateUrlRank))
				{
					int tempSize = unranked.get(updateUrlRank).size();
					double tempRank = ranked.get(updateUrlRank);
					if(unranked.get(updateUrlRank).contains(rankUrl))
					{
						newRank = tempRank/tempSize;

					}

				}
			}

			newRank = (1-dampFact)  + (newRank * dampFact);
			ranked.put(rankUrl, newRank);
		}

	}
	/**
	 * SortRanked Method
	 * sorts the hash by values
	 */
	public void sortRanked()
	{
		List<String> mapKeys = new ArrayList<>(ranked.keySet());
		List<Double> mapValues = new ArrayList<>(ranked.values());
		Collections.sort(mapValues, Collections.reverseOrder());

		Collections.sort(mapKeys, Collections.reverseOrder());


		LinkedHashMap<String, Double> sortedMap =
				new LinkedHashMap<>();

		Iterator<Double> valueIt = mapValues.iterator();
		while (valueIt.hasNext()) {
			Double val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();

			while (keyIt.hasNext()) {
				String key = keyIt.next();
				Double comp1 = ranked.get(key);
				Double comp2 = val;

				if (comp1.equals(comp2)) {
					keyIt.remove();
					sortedMap.put(key, val);
					break;
				}
			}
		}
		setRanked(sortedMap);
	}
	/**
	 * print out a more readable output
	 */
	public void printRanks()
	{
		for(String printer:ranked.keySet())
		{
			System.out.println(printer + "     =      " + ranked.get(printer));
		}
	}

}
