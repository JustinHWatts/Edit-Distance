import java.util.*;

/**
 * EditDistanceModel.java - Handles the math behind the various
 * algorithms for finding words in a body of text that are within the
 * provided maximum edit distance of the search word. If a word is found
 * that is within the permissible edit distance, that word will be inclosed
 * in parentheses and placed back into the body of text as such. To compile
 * this file, use the "javac EditDistanceModel.java" command. Run the full
 * program through the "EditDistanceDriver" file.
 * 
 * Initial Conditions:
 * distance[i][0] = i
 * distance[0][j] = j
 * 
 * Recurrence:
 * distance[i + 1][j + 1] = min(distance[i][j + 1] + 1, distance[i][j] + 1,
 * distance[i + 1][j] + 1)
 *
 * @author Justin Watts
 * @version 04/29/2016
 * @category ITEC 360
 */
public class EditDistanceModel
{
	private String search;
	private int maxDistance;
	private List<String> text;
	
	/**
	 * Constructor method for the EditDistanceModel class. Accepts a search word,
	 * maximum edit distance, and a body of text to check for said search word.
	 * @param search_ The word to be compared to each word in the body of text
	 * @param maxDistance_ The maximum edit distance away from the search word
	 * @param text_ The body of text to iterated through with the search word
	 */
	public EditDistanceModel(String search_, int maxDistance_, List<String> text_)
	{
		search = search_;
		maxDistance = maxDistance_;
		text = text_;
	}
	
	/**
	 * Checks a body of text for words within the maximum edit distance from the
	 * search word. If a word is found within said edit distance, parentheses are
	 * placed around the word, and it is placed back into the body of text as such.
	 * @return The body of text after it has been iterated through
	 */
	public List<String> checkForWord()
	{
		int distance = 0;
		int count = 0;
		boolean frontCheck = false;
		boolean backCheck = false;
		String actual = "";
		
		//Iterates through given text and compares search word with each word in text
		for (String current : text)
		{
			if (!current.equals("\n"))
			{
				actual = current.replaceAll("[^A-Za-z]", "");
				actual = actual.toLowerCase();
				
				distance = compare(search, actual);
				
				if (distance <= maxDistance)
				{
					if (!Character.isLetter(current.charAt(0)))
					{
						current = current.substring(0, 1) + "(" + current.substring(1, current.length());
						frontCheck = true;
					}
					
					if (!Character.isLetter(current.charAt(current.length() - 1)))
					{
						current = current.substring(0, current.length() - 1) + ")" + 
								current.substring(current.length() - 1);
						backCheck = true;
					}
					
					if (frontCheck && backCheck)
						text.set(count, current);
					else if (frontCheck)
						text.set(count, current + ")");
					else if (backCheck)
						text.set(count, "(" + current);
					else
						text.set(count, "(" + current + ")");
				}
			}
			
			frontCheck = false;
			backCheck = false;
			count++;
		}
		
		return text;
	}
	
	/**
	 * Compares the search word to a word within the body of text and finds
	 * the smallest edit distance between the two words.
	 * @param search The word being compared to a word from the body of text
	 * @param actual The actual word being used from the body of text
	 * @return The smallest edit distance between the search word and the actual word
	 */
	private int compare(String search, String actual)
	{
		int minDistance = 0;
		int searchLen = search.length();
		int actualLen = actual.length();
		int[][] distance = new int[searchLen + 1][actualLen + 1];
		
		for (int i = 0; i <= searchLen; i++)
			distance[i][0] = i;
		for (int j = 0; j <= actualLen; j++)
			distance[0][j] = j;
		
		for (int i = 0; i < searchLen; i++)
		{
			for (int j = 0; j < actualLen; j++)
			{
				if (search.charAt(i) == actual.charAt(j))
					distance[i + 1][j + 1] = distance[i][j];
				else
				{
					//Calculates edit distance with insert, substitute, and delete
					int insert = distance[i][j + 1] + 1;
					int substitute = distance[i][j] + 1;
					int delete = distance[i + 1][j] + 1;
					
					//Find minimum edit distance using insert, substitute, and delete
					if (substitute > insert)
						minDistance = insert;
					else
						minDistance = substitute;
					
					if (minDistance > delete)
						minDistance = delete;
					
					distance[i + 1][j + 1] = minDistance;
				}
			}
		}
		
		return distance[searchLen][actualLen];
	}
}