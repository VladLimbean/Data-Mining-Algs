package DataObj;

import java.util.HashSet;
import java.util.Set;

/**
 * Cluster class used in K-Means alg
 */
public class Cluster
{
    private final int ID;

    private Set<Student> students;
    private double centerX, centerY;

    //each cluster contains one student from its initiation
    public Cluster(Student initialStudent, int ID)
    {
        this.ID = ID;

        this.students = new HashSet<Student>();
        students.add(initialStudent);

        this.centerX = 0;
        this.centerY = 0;
    }

    /**
     * Calculates the centroid coordinates for the cluster
     */
    public void recalculateCentroid()
    {
       centerX = 0;
       centerY = 0;

        for(Student currentStudent : students)
        {
            this.centerX += currentStudent.getAge();
            this.centerY += currentStudent.getStudyProg();
        }
        this.centerX /= students.size();
        this.centerY /= students.size();
    }

    public double getCenterX()
    {
        return centerX;
    }

    public double getCenterY()
    {
        return  centerY;
    }

    /**
     * Adds new student to cluster.
     *
     * @param studToAdd Student to add
     */
    public void addToCluster(Student studToAdd)
    {
        students.add(studToAdd);
    }

    /**
     * Removes a student from a cluster
     *
     * @param studentToRemove Student to remove
     */
    public void removeFromCluster(Student studentToRemove)
    {
        students.remove(studentToRemove);
    }

    public String toString()
    {
        StringBuilder result = new StringBuilder();

        result.append("Cluster " + ID + " : {" + centerX + " " + centerY + "}\n");

        for(Student student : students)
        {
            result.append("Age[" + student.getAge() + "]" + "SP[" + student.getStudyProg() + "]\n");
        }

        return result.toString();
    }
}
