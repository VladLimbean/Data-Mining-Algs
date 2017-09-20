package DataObj;

import java.util.List;

/**
 *
 * Class storing information for one data object
 */
public class DataObject
{
    private int    age;
    private String gender;
    private String reasonForAttendance;
    private String studyProgramme;

    private String[] listOfGames;
    private String[] listOfProgLang;
    private String[] listOfCheckpoints;

    public DataObject(int       age,
                      String    gender,
                      String    studyProgramme,
                      String    reasonForAttendance,
                      String[]  listOfGames,
                      String[]  listOfProgLang,
                      String[]  listOfCheckpoints)
    {
        this.age = age;
        this.gender = gender;
        this.reasonForAttendance = reasonForAttendance;
        this.studyProgramme = studyProgramme;

        this.listOfGames = listOfGames;
        this.listOfProgLang = listOfProgLang;
        this.listOfCheckpoints = listOfCheckpoints;
    }

    public int getAge()
    {
        return age;
    }

    public String getGender()
    {
        return gender;
    }

    public String getReasonForAttendance()
    {
        return reasonForAttendance;
    }

    public String getStudyProgramme()
    {
        return studyProgramme;
    }

    public String[] getListOfGames()
    {
        return listOfGames;
    }

    public int getNumberOfGames()
    {
        return listOfGames.length;
    }

    public String[] getListOfProgLang()
    {
        return listOfProgLang;
    }

    public int getNumberOfProgLang()
    {
        return listOfProgLang.length;
    }

    public String[] getListOfCheckpoints()
    {
        return listOfCheckpoints;
    }

    public String toString()
    {
        String result = "Age: " + age + "\n" +
                        "Gender: " + gender + "\n" +
                        "Study program: " + studyProgramme + "\n" +
                        "Reason for attendance: " + reasonForAttendance + "\n"
                        ;

        return result;
    }
}
