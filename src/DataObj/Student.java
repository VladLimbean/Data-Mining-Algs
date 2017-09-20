package DataObj;

import java.util.List;

/**
 * The Student class is used for the k-NearestNeighbors algorithm.
 */
public class Student
{
    private int             age;
    private int             gender;
    private int             studyProgram;
    private int             reasonForTakingCourse;
    private List<Integer>   listOfProgLang;
    private int             numberOfProgLang;

    public Student(int           age,
                   int           gender,
                   int           studyProgram,
                   int           reasonForTakingCourse,
                   List<Integer> listOfProgLang)
    {
        this.age =                      age;
        this.gender =                   gender;
        this.studyProgram =             studyProgram;
        this.reasonForTakingCourse =    reasonForTakingCourse;
        this.listOfProgLang =           listOfProgLang;

        this.numberOfProgLang =         listOfProgLang.size();
    }

    public int getAge()
    {
        return age;
    }

    public int getNumberOfProgLang()
    {
        return numberOfProgLang;
    }

    public int getGender()
    {
        return gender;
    }

    public int getStudyProg()
    {
        return studyProgram;
    }

    public int getReasonForTakingCourse()
    {
        return reasonForTakingCourse;
    }

    public List<Integer> getListOfProgLang()
    {
        return listOfProgLang;
    }

}
