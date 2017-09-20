package Preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CSVFileReader class is used to load a csv file.
 * Based on lab implementation.
 */
public class CSVFileReader
{
	/**
	 * The read method reads in a csv file as a two dimensional string array.
	 * The method utilizes string.split to differentiate the lines of data.
	 *
	 * @param csvFile 		 File to load
	 * @param separationChar Character used to separate entries
	 * @param nullValue 	 What to insert in case of missing values
	 *
	 * @return 			 	 Data file as a list containing and array of strings
	 * @throws 				 IOException
	 */
	public static List<String[]> readDataFile(String csvFile, String separationChar, String nullValue, boolean skipHeaderRow)
			throws IOException
	{
		DataCleaner dataCleaner = new DataCleaner();

		List<String[]> lines = new ArrayList<String[]>();
		BufferedReader bufRdr = new BufferedReader(new FileReader(new File(csvFile)));

		//used to skip or not skip the header
		if(skipHeaderRow)
		{
			String header = bufRdr.readLine();
			String[] allAtrib = header.split(";;");

			int counter = 1;
			for(String s : allAtrib)
			{
				System.out.println(counter + " >>> " + s);
				counter++;
			}

			skipHeaderRow = false;
		}
		else
		{
			//used to read next line in CSV
			String line = bufRdr.readLine();
		}

		//used to read rest of the csv
		String line;
		while ((line = bufRdr.readLine())!= null)
		{
			String[] arr = line.split(separationChar);

			for(int i = 0; i < arr.length; i++)
			{
				//replaces empty answers with string nullValue
				if(arr[i].equals(""))
				{
					arr[i] = nullValue;
				}
				else
				{
					//sets every non-null string to lowercase and removes quotation marks
					arr[i] = dataCleaner.removerQuotationMarks(arr[i].toLowerCase());
				}
			}
			lines.add(arr);
		}
		bufRdr.close();

		return lines;
	}

	//test client
	public static void main(String args[]) {
		try
		{
			List<String[]> data = readDataFile(args[0],";;", "nullValue",true);

			for (String[] line : data)
			{
				System.out.println(Arrays.toString(line));
			}
			System.out.println("Number of tuples loaded: " + data.size());
		}
		catch (IOException e)
		{
			System.err.println(e.getLocalizedMessage());
		}
	}
}
