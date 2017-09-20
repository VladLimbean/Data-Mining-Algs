package DataObj;

import Preprocessing.CSVFileReader;
import Preprocessing.DataCleaner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Generates all DataObjects from the CSV file
 */
public class DataFilter
{
    private List<String[]> surveyData;
    private DataCleaner dataCleaner;

    public DataFilter()
    {
        try
        {
            this.dataCleaner = new DataCleaner();
            this.surveyData = CSVFileReader.readDataFile(
                    "Data/Data Mining - Spring 2017.csv",
                    ";;",
                    "nullValue",
                    false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates all data objects.
     *
     * Note: the checkpoints students go through at ITU was ultimately not used.
     *
     * @return  A list representation of data objects
     */
    public List<DataObject> generateDataObj()
    {

        List<DataObject> resultList = new ArrayList<>();

        int count = 0; // counter for the number of data objects
        System.out.print("Generating data objects ...");

        for(String[] dataRow : surveyData)
        {

            int age = Integer.parseInt(dataRow[1]);                         // Index #1 - int representation of age

            String gender = dataCleaner.classifyGender(dataRow[2]);         // Index #2 - string representation of gender

            String studyProgramme = dataRow[5];                             // Index #5 - string representation of study programme
            String reasonForAttendance = dataRow[6];                        // Index #6 - string representation of reason for taking Data Mining

            String[] listOfProgLang = dataRow[7].split(",");          // Index #7 - string array representation of prog languages known

            String[] listOfGames = dataRow[20].split(";");            // Index #20 - string array representation of game played
            String[] listOfCheckpoints = new String[15];                    // Index #22 <> 36 - string array of checkpoints in ITU range 0 to 14

            for(int i = 22; i <= 36; i++)
            {
                listOfCheckpoints[i - 22] = dataRow[i];
            }

            //create data object
            DataObject currentDataObj =
                    new DataObject(
                            age,
                            gender,
                            studyProgramme,
                            reasonForAttendance,
                            listOfGames,
                            listOfProgLang,
                            listOfCheckpoints);

            //add data object to list and increment counter
            resultList.add(currentDataObj);
            count++;
        }

        System.out.print("\n" + count + " data objects created successfully \n");
        System.out.println();

        return resultList;
    }
}
