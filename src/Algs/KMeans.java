package Algs;

import DataObj.Cluster;
import DataObj.DataObject;
import DataObj.Student;
import Preprocessing.Normalizer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * KMeans class used in clustering students by age and study program
 */
public class KMeans
{
    private Normalizer normalizer;
    private List<Student> allStudents;
    private List<Cluster> allClusters;
    //maps in which clustered students belong
    private Hashtable<Student, Cluster> mapOfStudents;

    private int NUM_CLUSTERS = 5;

    /**
     * Constructor sets up the data necessary for the algorithm
     */
    public KMeans()
    {
        this.normalizer = new Normalizer();
        this.mapOfStudents = new Hashtable<>();
        this.allStudents = new ArrayList<>();
        this.allClusters = new ArrayList<>();

        // create all the students
        // clustering only takes account of age and the study program of students
        // consequently other Student attributes are set to -1 for this class
        for(DataObject dataObject : normalizer.getInstanceAllData())
        {
            // the two relevant dimensions
            int age = normalizer.ageNormalizer(dataObject.getAge());

            //replace invalid age with average value
            if(age == -1)
            {
                int avgAge = normalizer.getAvgAge();
                age = normalizer.ageNormalizer(avgAge);
            }
            int studyProg = normalizer.getStudyProgramID(dataObject.getStudyProgramme());

            // irrelevant attributes set to -1
            Student currentStudent = new Student(age, -1, studyProg, -1, new ArrayList<>());

            //adding student to list
            allStudents.add(currentStudent);
        }
    }

    /**
     * Creates student clusters
     */
    public void kmeans()
    {   //create random for later use
        Random random = new Random();

        //create list of randomly picked students
        List<Student> listOfRands = new ArrayList<>();

        //create clusters with one random student inside
        for(int i = 0; i < NUM_CLUSTERS; i++)
        {
            //random index number between 0 and the size of the list of all our students (minus one)
            int randomIndex = random.nextInt(allStudents.size());

            //add the random students to a list
            listOfRands.add(allStudents.get(randomIndex));

            //create new cluster, add cluster to the list, put student and his respective cluster in a map
            Cluster currentCluster = new Cluster(allStudents.get(randomIndex), i+1);
            allClusters.add(currentCluster);
            mapOfStudents.put(allStudents.get(randomIndex), currentCluster);

            //remove student from the list so as not to be randomly picked again
            allStudents.remove(randomIndex);
            //recalculate centroid of the current cluster
            currentCluster.recalculateCentroid();
        }
        //put back removed students
        allStudents.addAll(listOfRands);

        //boolean to check changes, counter to keep track of number of loops
        boolean hasChanged = true;
        int counter = 0;

        while(hasChanged)
        {
            // increment loop counter and assume no changes will happen during the loop
            counter++;
            hasChanged = false;
            // iterate through all students
            for(Student currentStudent : allStudents)
            {
                // find closes cluster relative to student
                Cluster closest = findClosesCluster(currentStudent, allClusters);

                // check if the student and his cluster have been already added to our map
                if(mapOfStudents.containsKey(currentStudent))
                {
                    // get the student's current cluster
                    Cluster residentCluster = mapOfStudents.get(currentStudent);

                    // check if the cluster we found is actually the closest
                    if(residentCluster != closest)
                    {
                        // if it is not then remove the student from the cluster
                        residentCluster.removeFromCluster(currentStudent);
                        // and add him to the closest cluster
                        closest.addToCluster(currentStudent);

                        // update the map
                        mapOfStudents.put(currentStudent, closest);
                    }
                }
                else
                {
                    // if the map does not contain the student and respective cluster then add
                    closest.addToCluster(currentStudent);
                    // and finally update the map
                    mapOfStudents.put(currentStudent, closest);
                }
            }
            // recalculate centroids
            for(Cluster currentCluster :  allClusters)
            {
                // first we store the previous centroid information
                double previousX = currentCluster.getCenterX();
                double previousY = currentCluster.getCenterY();
                // now recalcualate the cluster centroid
                currentCluster.recalculateCentroid();

                // since we're dealing with doubles we will check if there is a difference greater than 0.00001
                if(Math.abs(previousX - currentCluster.getCenterX()) > 0.00001 ||
                        Math.abs(previousY - currentCluster.getCenterY()) > 0.00001)
                {
                    // if there is then changes have happened. boolean becomes true, loop will run again
                    // otherwise we exit the loop
                    hasChanged = true;
                }
            }
        }
        //print out number of loops
        System.out.println("Loops: " + counter);
    }

    /**
     * Calculates the euclidean distance between a student and a cluster's center
     *
     * @param student Student representation
     * @param cluster Cluster representation
     * @return        Double representation of the distance between Student and Cluster
     */
    private double calculateDistance(Student student, Cluster cluster)
    {
        double result;

        result = Math.pow(student.getAge() - cluster.getCenterX(), 2) +
                 Math.pow(student.getStudyProg() - cluster.getCenterY(), 2);

        return Math.sqrt(result);
    }

    /**
     * Finds the cluster which is closest to our student's coordinates
     *
     * @param currentStudent Student representation
     * @param allClusters    List representation of all clusters
     * @return               Cluster which is closest to our student
     */
    private Cluster findClosesCluster(Student currentStudent, List<Cluster> allClusters)
    {
        //initiate a cluster as null and a minimal distance
        Cluster clusterToReturn = null;
        double minDist = Double.MAX_VALUE;

        //go through all clusters
        for(Cluster currentCluster : allClusters)
        {
            //calculate the distance between each cluster and the one student
            double currentDistance = calculateDistance(currentStudent, currentCluster);
            // in case the current distance is smaller than the saved minimum, then overwrite
            if(currentDistance < minDist)
            {
                //update minimal distance and cluster to return
                minDist = currentDistance;
                clusterToReturn = currentCluster;
            }
        }
        //once the loop is done return the closest cluster
        return clusterToReturn;
    }
    //test client
    public static void main(String[] args) {
        KMeans KM = new KMeans();
        KM.kmeans();

        for (Cluster cl : KM.allClusters)
        {
            System.out.println(cl.toString());
        }
    }
}
