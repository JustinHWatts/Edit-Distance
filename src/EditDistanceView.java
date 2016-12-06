import java.io.*;
import java.util.*;

/**
 * EditDistanceView.java - Runs the command line loop for the
 * Edit Distance program, allowing for user input on how to read in the
 * search word, maximum edit distance, and body of text to iterate through.
 * Also collects the user input and runs it through the EditDistanceModel
 * class to complete the Edit Distance algorithm on the specified input.
 * To compile this file, use the "javac EditDistanceView.java" command.
 * Run the full program through the "EditDistanceDriver" file.
 *
 * @author Justin Watts
 * @version 04/29/2016
 * @category ITEC 360
 */
public class EditDistanceView
{
	/**
	 * Handles the program loop which allows a user to input commands for
	 * reading in a body of text to iterate through, determining if each
	 * word falls within a specified edit distance from the specified
	 * search word.
	 */
	public void start()
	{
		boolean done = false;
		BufferedReader reader = null;
		Scanner scan = new Scanner(System.in);
		EditDistanceModel model;
		
		System.out.println("Indicate Type of Input ('File', 'Command', or 'Exit')");
		//"File" to read from file and "Command" for command line
		
		while (!done) //Program loop
		{
			List<String> text = new ArrayList<String>();
			String input = scan.nextLine();
			input = input.toLowerCase();
			
			if (input.equals("file"))
			{
				System.out.println("Input Filename");
				input = scan.nextLine();
				String line = "";
				String search = "";
				int count = 0;
				int maxDistance = 0;
				
				//Reads a file for the search word, edit distance, and body of text
				try
				{
					reader = new BufferedReader(new FileReader(input));
					while ((line = reader.readLine()) != null)
					{
						if (count == 0)
							search = line.toLowerCase();
						else if (count == 1)
							maxDistance = Integer.parseInt(line);
						else
						{
							String[] currentLine = line.split(" ");
							
							for (String current : currentLine)
								text.add(current);
							text.add("\n");
						}
						
						count++;
					}
				}
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					if (reader != null)
					{
						try
						{
							reader.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				
				model = new EditDistanceModel(search, maxDistance, text);
				text = model.checkForWord();
				
				System.out.println("\n" + search);
				System.out.println(maxDistance);
				
				//Prints the resulting body of text after it has been iterated through
				for (String current : text)
				{
					if (!current.equals("\n"))
						System.out.print(current + " ");
					else
						System.out.print(current);
				}
				
				System.out.println("\nIndicate Type of Input ('File', 'Command', or 'Exit')");
			}
			else if (input.equals("command"))
			{
				System.out.println("Input the Search Word");
				input = scan.nextLine();
				String search = input.toLowerCase();
				
				System.out.println("Input the Maximum Edit Distance");
				int maxDistance = scan.nextInt();
				
				System.out.println("Input the Text to Search (Enter 'Done!' when Finished)");
				input = scan.nextLine();
				//Reads in the body of text to iterate through
				while (!input.equals("Done!"))
				{
					String[] line = input.split(" ");
					for (String current : line)
						text.add(current);
					text.add("\n");
					input = scan.nextLine();
				}
				
				model = new EditDistanceModel(search, maxDistance, text);
				text = model.checkForWord();
				
				System.out.println("\n" + search);
				System.out.println(maxDistance);
				
				//Prints the resulting body of text after it has been iterated through
				for (String current : text)
				{
					if (!current.equals("\n"))
						System.out.print(current + " ");
					else
						System.out.print(current);
				}
				
				System.out.println("\nIndicate Type of Input ('File', 'Command', or 'Exit')");
			}
			else if (input.equals("exit"))
				done = true;
			else
				System.out.println("\nIncorrect Input, Please Enter 'File', 'Command', or 'Exit'");
		}
	}
}