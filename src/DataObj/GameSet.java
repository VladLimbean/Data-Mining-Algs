package DataObj;

import java.util.Arrays;

/**
 * GameSet class stores information about frequent games appearing in the data set.
 */
public class GameSet
{
	/**
	 * A list representation of all games in data set
	 */
    private final String[] gameSet;
    private int supportCount;
    private double confidence;

    /**
     * Creates a new instance of the DataObj.GameSet class.
     */
    public GameSet(String[] gamesToAdd)
    {
        gameSet = gamesToAdd;
    }

    public int size()
    {
        return gameSet.length;
    }

    public String[] getGamesArr()
    {
        return gameSet;
    }

    /**
     * Auto-generated equals method.
     *
     * Checks is an object is equal to a stored value.
     *
     * @return  True if equal, False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSet gameSet1 = (GameSet) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(gameSet, gameSet1.gameSet);
    }

    /**
     * Auto-generated hashCode method.
     *
     * Generates custom hashCode for the current game set.
     * Used later on in storing information in the Algs.Apriori hashtable.
     *
     * @return  int representation of a hash code.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(gameSet);
    }

    public void setSupportCount(int input)
    {
        this.supportCount = input;
    }

    public void setConfidence(double input)
    {
        this.confidence = input;
    }

    public int getSupportCount()
    {
        return supportCount;
    }

    public double getConfidence()
    {
        return confidence;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for(String game : gameSet)
        {
            if(gameSet[0] == game)
            {
                sb.append(game);
            }
            else
            {
                sb.append(", " + game);
            }
        }
        sb.append("]");

        sb.append("[" + supportCount + "]");

        return sb.toString();
    }
}
