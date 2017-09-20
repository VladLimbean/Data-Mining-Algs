package Algs;

import DataObj.DataObject;
import DataObj.ResultType;
import DataObj.Student;
import Preprocessing.Normalizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static DataObj.ResultType.*;

/**
 * k-Nearest Neighbors algorithm classifies sort of experienced programmers.
 *
 * Clarification: students who know 3 or more programming languages are considered (at least) moderately
 * experienced programmers. Students who know bellow 3 languages are considered to be inexperienced programmers.
 */
public class KNN
{
    private static final int k = 8;
    private static final int thresholdNumber = 2;

    private List<DataObject> allData;
    private List<Student> allStudents;
    private Normalizer normalizer;

    /**
     * The constructor sores and creates all the information to be used by the algorithm
     */
    public KNN()
    {
        this.normalizer = new Normalizer();
        this.allStudents = new ArrayList<>();
        this.allData = normalizer.getInstanceAllData();

        //populating the normalized lists
        for(DataObject dataObject : allData)
        {
            normalizer.addStudyProg(dataObject.getStudyProgramme());
            normalizer.addReasonForAttendance(dataObject.getReasonForAttendance());

            //normalizing programming languages
            for(String progLang : dataObject.getListOfProgLang())
            {
                progLang = progLang.trim();
                progLang = normalizer.manualProgLangReplacement(progLang);
                normalizer.addProgLang(progLang);
            }
        }

        //create Student to be used by the algorithm
        for(DataObject dataObject : allData)
        {
            int age          = normalizer.ageNormalizer(dataObject.getAge());
            int gender       = normalizer.genderNormalizer(dataObject.getGender());
            int studyProg    = normalizer.getStudyProgramID(dataObject.getStudyProgramme());
            int reasonForAtt = normalizer.getReasonForAttendanceID(dataObject.getReasonForAttendance());

            List<Integer> progLang = new ArrayList<>();
            for(String language : dataObject.getListOfProgLang())
            {
                language = normalizer.manualProgLangReplacement(language);
                progLang.add(normalizer.getProgLangID(language));
            }
            // create student with normalized data parameters
            Student currentStudent = new Student(age, gender, studyProg, reasonForAtt, progLang);

            //add student to list
            allStudents.add(currentStudent);
        }
    }

    /**
     * Calculates weight for the k-neighbors.
     * The method is to be used only for binary data types.
     *
     * @param k     Integer representation of the number of neighbors
     * @return      Double representation of the weight value
     */
    private static double calculateWeightedValue(int k)
    {
        double result = 0;
        for(int i = 0; i < k; i++)
        {
            result += 1.0 / (i+1);
        }
        return result;
    }

    /**
     * Calculates the euclidean distance between two students
     *
     * @param testStud      Student representation
     * @param trainingStud  Student representation
     * @return              Double representation of the euclidean distance
     */
    private static double calculateDistance(Student testStud, Student trainingStud)
    {
        int distance = 0;

        //age
        distance += Math.pow(testStud.getAge() - trainingStud.getAge(), 2);

        //gender
        distance += Math.pow(testStud.getGender() - trainingStud.getGender(), 2);

        //study programme
        distance += Math.pow(testStud.getStudyProg() - trainingStud.getStudyProg(), 2);

        //reason for taking course
        distance += Math.pow(testStud.getReasonForTakingCourse() - trainingStud.getReasonForTakingCourse(), 2);

        return Math.sqrt(distance);
    }

    /**
     * Makes a prediction of the type of student (programmer or not) based on their relative distances
     *
     * @param testStud  Student for which to make the prediction
     * @param distances HashMap representation of all students and their distances
     * @return          Result type prediction (True Positive or True Negative or False Positive or False Negative)
     */
    public static ResultType makePrediction(Student testStud, HashMap<Student, Double> distances)
    {
        //sort the students in the hashmap closest to farthers
        List<Student> sortedStudents = distances.entrySet().stream().sorted((x, y) -> x.getValue()
                    .compareTo(y.getValue())).map(Map.Entry::getKey)
                    .collect(Collectors.toList());

        //iterate through the k nearest neighbors
        double programmerCount = 0;
        for(int i = 0; i < k; i++)
        {
            Student closestStudent = sortedStudents.get(i);

            //if the current close student knows more programming languages than the threshold number
            //then increase programmer count
            if(closestStudent.getNumberOfProgLang() > thresholdNumber)
            {
                // add more weight to closest neighbors
                programmerCount += (1.0 / (i+1));
            }
        }

        //calculate weighted threshold
        double weightedThreshold = calculateWeightedValue(k);
        //If we have more programmers than half of the nearest neighbors, then we make  a prediction
        if(programmerCount > (weightedThreshold / 2))
        {
            if(testStud.getNumberOfProgLang() > thresholdNumber)
            {
                // true positive if we have more programming languages than the threshold number
                System.out.println("TP : more than " + thresholdNumber + " <> " + testStud.getNumberOfProgLang());
                return TRUE_POSITIVE;
            }
            else
            {
                // false positive otherwise
                System.out.println("FP : more than " + thresholdNumber + " <> " + testStud.getNumberOfProgLang());
                return ResultType.FALSE_POSITIVE;
            }
        }
        else
        {
            //if we have less programmers than half the nearest neighbors, then
            if(testStud.getNumberOfProgLang() < thresholdNumber)
            {
                // true positive if the number of programming languages is bellow the threshold
                System.out.println("TN : less than " + thresholdNumber + " <> " + testStud.getNumberOfProgLang());
                return ResultType.TRUE_NEGATIVE;
            }
            else
            {
                // false negative otherwise
                System.out.println("FN : less than " + thresholdNumber + " <> " + testStud.getNumberOfProgLang());
                return ResultType.FALSE_NEGATIVE;
            }
        }
    }

    //client
    public static void main(String[] args)
    {
        KNN knn = new KNN();

        List<Student> allStudents = knn.allStudents;

        List<Student> trainingSet = new ArrayList<>();
        List<Student> testSet     = new ArrayList<>();

        // create test list containing 1/3 of the list of all students
        for (int i = 0; i < allStudents.size() / 3; i++)
        {
            testSet.add(allStudents.get(i));
        }

        //populate training list with the remaining 2/3 list of all students.
        for(int j = (allStudents.size() / 3) + 1; j < allStudents.size() - 1; j++)
        {
            trainingSet.add(allStudents.get(j));
        }

        int TP = 0;
        int TN = 0;
        int FP = 0;
        int FN = 0;
        //iterate through all test students
        for(Student testStud : testSet)
        {
            HashMap<Student, Double> mapOfDistances = new HashMap<>();
            for(Student trainingStud : trainingSet)
            {
                //add the training student to the map holding the distance they have relative to the test student
                mapOfDistances.put(trainingStud, calculateDistance(testStud, trainingStud));
            }

            //make prediction
            ResultType currentPrediction = makePrediction(testStud, mapOfDistances);

            //calculate the number of result types
            if(currentPrediction == TRUE_POSITIVE)
            {
                TP++;
            }
            else if(currentPrediction == TRUE_NEGATIVE)
            {
                TN++;
            }
            else if(currentPrediction == FALSE_POSITIVE)
            {
                FP++;
            }
            else if(currentPrediction == FALSE_NEGATIVE)
            {
                FN++;
            }
        }

        //print out relevant information
        System.out.println("TP: " + TP);
        System.out.println("TN: " + TN);
        System.out.println("FP: " + FP);
        System.out.println("FN: " + FN);

        double totalT = TP + TN;
        double totalF = FP + FN;
        double grandTotal = TP + TN + FP + FN;

        System.out.println("Percent accurate prediction: " + ((totalT) * 100) / grandTotal + "%");
        System.out.println("Percent inaccurate prediction: " + ((totalF) * 100) / grandTotal + "%");
    }
}
