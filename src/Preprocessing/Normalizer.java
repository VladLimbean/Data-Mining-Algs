package Preprocessing;

import DataObj.DataFilter;
import DataObj.DataObject;
import DataObj.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class normalizes non-numeric values for the kNN and kMeans class.
 *
 * Stores a list of Study Programmes, Reason for Attendance and Programming Languages as unique entries in a list.
 */
public class Normalizer
{
    private List<DataObject>    allData;

    private List<String>        list_StudyProg;
    private List<String>        list_ReasonForTakingCourse;
    private List<String>        list_ProgLang;

    //private HashMap<String, Integer> allAnswers;

    /**
     * Creates default lists of valid answers for Study Programs, Reason For Taking Course, List of Programing Languages
     * Stores a list of all data objects.
     */
    public Normalizer()
    {
        List<String> list_StudyProg =             new ArrayList<>();
        this.list_StudyProg = list_StudyProg;

        //adding default answers to lists and frequency map
        list_StudyProg.add("games-a");          //index #0
        list_StudyProg.add("games-t");          //index #1
        list_StudyProg.add("sdt-se");           //index #2
        list_StudyProg.add("sdt-dt");           //index #3
        list_StudyProg.add("swu");              //index #4
        list_StudyProg.add("guest student");    //index #5

        List<String> list_ReasonForTakingCourse = new ArrayList<>();
        this.list_ReasonForTakingCourse = list_ReasonForTakingCourse;

        //adding default answers
        list_ReasonForTakingCourse.add("i am interested in the subject");                   //index #0
        list_ReasonForTakingCourse.add("it may help me to find a job");                     //index #1
        list_ReasonForTakingCourse.add("the other optional courses were least appealing");  //index #2
        list_ReasonForTakingCourse.add("this was a mandatory course for me");               //index #3

        //to be populated with normalized values
        this.list_ProgLang = new ArrayList<>();

        //adding default list of prog lang
        list_ProgLang.add("java");
        list_ProgLang.add("r");
        list_ProgLang.add("sql");
        list_ProgLang.add("python");
        list_ProgLang.add("html");
        list_ProgLang.add("css");
        list_ProgLang.add("javascript");
        list_ProgLang.add("c#");
        list_ProgLang.add("c++");
        list_ProgLang.add("c");
        list_ProgLang.add("gml.");
        list_ProgLang.add("processing");
        list_ProgLang.add("arduino");
        list_ProgLang.add("f#");
        list_ProgLang.add("vba");
        list_ProgLang.add("scala");
        list_ProgLang.add("matlab");
        list_ProgLang.add("kotlin");
        list_ProgLang.add("php");
        list_ProgLang.add("ruby");
        list_ProgLang.add("prolog");
        list_ProgLang.add("swift");
        list_ProgLang.add("haxe");
        list_ProgLang.add("haskell");
        list_ProgLang.add("lua");
        list_ProgLang.add("pascal");
        list_ProgLang.add("vb.net");
        list_ProgLang.add("puredata");
        list_ProgLang.add("android");
        list_ProgLang.add("bash");
        list_ProgLang.add("powershell");
        list_ProgLang.add("batch");
        list_ProgLang.add("vb");
        list_ProgLang.add("erlang");
        list_ProgLang.add("golang");
        list_ProgLang.add("vvvv");

        allData = new DataFilter().generateDataObj();
    }

    /**
     * Calculates global average age.
     *
     * @return Integer representation of global average age.
     */
    public int getAvgAge()
    {
        int sumAge = 0;
        for(DataObject dataObject : allData)
        {
            sumAge += dataObject.getAge();
        }

        return  sumAge / allData.size();
    }

    /**
     * Calculates local average of age (normalized)
     *
     * @param listOfStudents    List of students
     * @return                  Integer representation of average age across the list.
     */
    public int getLocalAverage(List<Student> listOfStudents)
    {
        int sumAge = 0;
        for(Student st : listOfStudents)
        {
            sumAge += st.getAge();
        }

        return sumAge / listOfStudents.size();
    }

    /**
     * Normalized age data in intervals.
     *
     * Age:
     * 20 to 27 >>> interval 1
     * 28 to 33 >>> interval 2
     * 34 to 39 >>> interval 3
     * 40 to 45 >>> interval 4
     * 46 to 51 >>> interval 5
     *
     * Invalid answers will be marked as -1;
     *
     * @param input     Integer representation of age
     * @return          Integer representation of age interval
     */
    public int ageNormalizer(int input)
    {
        if(input > 20 && input <= 27)
        {
            input = 1;
        }
        else if(input > 27 && input <= 33)
        {
            input = 2;
        }
        else if(input > 33 && input <= 39)
        {
            input = 3;
        }
        else if(input > 39 && input <= 45)
        {
            input = 4;
        }
        else if(input > 45 && input <= 51)
        {
            input = 5;
        }
        else
        {
            // invalid answers will hit this
            input = -1;
        }
        return input;
    }
    /**
     * Normalizes gender information:
     *
     * Masculine gender will be set to "0"
     * Feminine gender will be set to  "1"
     * Non-binary gender identity will be labeled "-1"
     *
     * @param input     String representation of gender
     */
    public int genderNormalizer(String input)
    {
        input = input.trim();
        int result;

        if(input.startsWith("m"))
        {
            result = 0;
        }
        else if (input.startsWith("f"))
        {
            result = 1;
        }
        else
        {
            result = -1;
        }
        return result;
    }

    /**
     * Returns ID number of a valid study programme.
     *
     * ID Values:
     *
     *      Games-A         ID: 0
     *      Games-T         ID: 1
     *      SDT-SE          ID: 2
     *      SDT-DT          ID: 3
     *      SWU             ID: 4
     *      Guest Student   ID: 5
     *
     * The list can also grow in case there are additional answers not encompassed in the list above.
     * In case of an invalid answer the method adds the response to the list and returns its index.
     *
     * @param input     String representation of a given study programme
     * @return          Integer representation of the study programme ID as defined above
     */
    public int getStudyProgramID(String input)
    {
        if(list_StudyProg.contains(input))
        {
            return list_StudyProg.indexOf(input);
        }
        list_StudyProg.add(input);

        return list_StudyProg.indexOf(input);
    }

    /**
     * Adds a new study program in case it is not already included in the list.
     * If the answer is not included in the list it is added both to the list and the hashMap with the frequency value 1
     *
     * @param input     String representation of a Study Programme title.
     */
    public void addStudyProg(String input)
    {
        input = input.trim();
        if(!list_StudyProg.contains(input))
        {
            list_StudyProg.add(input);
        }
    }

    /**
     * Returns ID number of a valid 'reason for attendance' response ID.
     *
     * ID Values:
     *      Invalid answer                                      ID: -1
     *      I am interested in the subject                      ID: 0
     *      it may help me to find a job                        ID: 1
     *      the other optional courses were least appealing     ID: 2
     *      this was a mandatory course for me                  ID: 3
     *
     * The list can also grow in case there are additional answers not encompassed in the list above.
     * In case of an invalid answer the method returns -1.
     *
     * @param input     String representation of an answer
     * @return          Integer representation of the answer ID.
     */
    public int getReasonForAttendanceID(String input)
    {
        int ID = -1;
        if(list_ReasonForTakingCourse.contains(input))
        {
            return list_ReasonForTakingCourse.indexOf(input);
        }
        return ID;
    }

    /**
     * Adds a new 'reason for attendance' answer in case it is not already included in the list.
     * If the answer is not included in the list it is added both to the list and the hashMap with the frequency value 1
     *
     * @param input     String representation of a 'Reason for attendance' answer.
     */
    public void addReasonForAttendance(String input)
    {
        if(!list_ReasonForTakingCourse.contains(input))
        {
            list_ReasonForTakingCourse.add(input);
        }
    }

    /**
     * Adds programming language to list in case it is not present.
     * If programming language is already added, then the hashMap of all answers is updated with the new frequency value.
     *
     * @param input     String representation of a programming language
     */
    public void addProgLang(String input)
    {
        input = input.trim();
        if(!list_ProgLang.contains(input))
        {
            list_ProgLang.add(input);
        }
    }

    /**
     * Returns list index number of a given programming language.
     * Returns -1 in case programming language does not exist.
     *
     * @param input     String representation of a programming language
     * @return          Integer representation of the prog. language's index in the list.
     */
    public int getProgLangID(String input)
    {
        return list_ProgLang.indexOf(input);
    }

    /**
     * Manually corrects invalid answers seen throughout the csv file
     *
     * @param input String representation of a programming language
     * @return      String representation of a programming language
     */
    public String manualProgLangReplacement(String input)
    {
        input = input.replaceAll("c sharp", "c#");
        input = input.replaceAll("js", "javascript");
        input = input.replaceAll("jvascript", "javascript");
        input = input.replaceAll("c##", "c#");
        input = input.replaceAll("html5", "html");
        input = input.replaceAll("34", "INVALID");
        input = input.trim();

        return input;
    }

    public List<String> getList_StudyProg() {
        return list_StudyProg;
    }

    public List<String> getList_ReasonForTakingCourse() {
        return list_ReasonForTakingCourse;
    }

    public List<String> getList_ProgLang() {
        return list_ProgLang;
    }

    public List<DataObject> getInstanceAllData()
    {
        return allData;
    }
}
