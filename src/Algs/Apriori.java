package Algs;

import DataObj.DataFilter;
import DataObj.DataObject;
import DataObj.GameSet;

import java.util.*;

/**
 * Apriori class calculates the frequent games appearing in a data base
 */
public class Apriori
{
    private static final double confidenceThreshold = 0.7;
	private final List<GameSet> gameSets;

	private Hashtable<GameSet, Integer> frequentGameSets;
    private Hashtable<GameSet, Integer> nLevelGameSets;

	public Apriori(List<GameSet> gameSets)
    {
        this.gameSets = gameSets;
        this.frequentGameSets = new Hashtable<>();
    }
    private List<GameSet> apriori(List<GameSet> gameSets, int supportThreshold)
    {
        //generates the 1st level frequent game sets
        Hashtable<GameSet, Integer> frequentSet = generateFrequentGames_1(gameSets, supportThreshold);
        //store all frequent games
        this.frequentGameSets.putAll(frequentSet);

        List<GameSet> gameSubSets = new ArrayList<>();

        //initialized at 2 for the first run
        int loopBreaker = 2;

        //loop through frequent game sets and generate higher level game sets
        for (int k = 1; frequentSet.size() > loopBreaker; k++)
        {
            System.out.println("Finding frequent game sets of length " + (k + 1) + "…");

            //update loop breaker to avoid infinite cycles
            loopBreaker = frequentSet.size();
            //generate higher level game sets
            frequentSet = generateFrequentGames_n(supportThreshold, gameSets, frequentSet);

            //saves the top level game sets in a hash table for later use
            nLevelGameSets = frequentSet;

            //prints frequent games found
            for(GameSet gameSet : frequentSet.keySet())
            {
                for(int i = 0; i < gameSet.size(); i++)
                {
                    //formatting print
                    if(i == 0)
                    {
                        System.out.print(gameSet.getGamesArr()[i]);
                    }
                    else
                    {
                        System.out.print(", " + gameSet.getGamesArr()[i]);
                    }

                }
                System.out.println();
            }
            //updates master map
            this.frequentGameSets.putAll(frequentSet);
            gameSubSets.addAll(frequentSet.keySet());

            System.out.println("Found " + frequentSet.size() + " frequent sets.");
            System.out.println();
        }

        //create the result list
        List<GameSet> resultList = new ArrayList<>();

        // get all relevant sets
        Set<GameSet> allFrequentSets = frequentGameSets.keySet();
        Set<GameSet> nLevelFreqSets  = nLevelGameSets.keySet();

        //iterate through all frequent sets
        for(GameSet currentGameSet : allFrequentSets)
        {
            //ensure the gamesets have more than one element
            if(currentGameSet.getGamesArr().length > 1)
            {
                //extract full game set
                List<String> fullList = Arrays.asList(currentGameSet.getGamesArr());

                for(int i = 0; i < currentGameSet.getGamesArr().length; i++)
                {
                    //extract the single elements and create GameSet
                    String[] singleElem = new String[]{currentGameSet.getGamesArr()[i]};
                    GameSet oneElemSet = findGameSet(singleElem);

                    //set support count
                    oneElemSet.setSupportCount(frequentGameSets.get(oneElemSet));

                    if(oneElemSet != null)
                    {
                        //get confidence
                        double confidence = calculateConfidence(oneElemSet, currentGameSet);
                        //oneElemSet.setConfidence(confidence);

                        //print all game sets that comply with the association rules
                        if(confidence >= confidenceThreshold)
                        {
                            //add game sets to result list. once.
                            if(!resultList.contains(oneElemSet) || !resultList.contains(currentGameSet))
                            {
                                resultList.add(oneElemSet);
                                resultList.add(currentGameSet);
                            }
                            System.out.println(
                                    oneElemSet.toString() + " & " + currentGameSet.toString() + " CONFIDENCE: " + confidence);
                        }
                    }
                }
            }
        }
        //return list of game sets which are frequent and abide by the established association rules
        return resultList;
    }

    /**
     * Generates frequent game sets containing only one game
     *
     * @param gameSets          A list of all games
     * @param supportThreshold  Integer representation of support count
     * @return                  A hashtable containing the gamesets as key and their support count as value
     */
    private Hashtable<GameSet, Integer> generateFrequentGames_1(List<GameSet> gameSets, int supportThreshold)
    {
        Hashtable<GameSet, Integer> newLevelGameSet = new Hashtable<>();

        //iterate through all games
        for(int i = 0; i < this.gameSets.size(); i++)
        {
            GameSet currentGameSet = this.gameSets.get(i);
            //extract each game from each game set
            for (int j =  0; j < currentGameSet.size(); j++)
            {
                GameSet currentGame = new GameSet(new String[]{currentGameSet.getGamesArr()[j]});

                //if it is not in the hashtable, then put it in
                if(!newLevelGameSet.containsKey(currentGame))
                {
                    newLevelGameSet.put(currentGame, 1);
                    // set initial support count of game set
                    currentGame.setSupportCount(1);
                }
                else
                {
                    //if it is already in the hashtable, time to update the values
                    int gameCount = newLevelGameSet.get(currentGame);
                    newLevelGameSet.put(currentGame, gameCount+1);

                    //update support count of game set
                    currentGame.setSupportCount(gameCount+1);
                }
            }
        }

        // we turn to keys of the hash table into an array of objects, ultimately a String[]
        Object[] finalResult = newLevelGameSet.keySet().toArray();

        // checks all games and remove those with values under the supportThreshold
        for(Object keyObj : finalResult)
        {
            GameSet key = (GameSet) keyObj;

            //we check if they are under the support threshold
            if(newLevelGameSet.get(key) <= supportThreshold)
            {
                //if so, they get removed from the final hashtable
                newLevelGameSet.remove(key);
            }
        }
        //return the cleaned up table
        return newLevelGameSet;
    }

    /**
     * Generates higher level game sets where each game set can have two or more games.
     *
     * @param supportThreshold   Integer representation of the support threshold
     * @param gameSets           List representation of all games
     * @param lowerLevelItemSet  A hash table of lower level game sets
     * @return                   A hash table of higher level frequent game sets
     */
    private Hashtable<GameSet, Integer> generateFrequentGames_n(int supportThreshold,
                                                                List<GameSet> gameSets,
                                                                Hashtable<GameSet, Integer> lowerLevelItemSet)
    {
        Hashtable<GameSet, Integer> result = new Hashtable<>();

        // extract the frequent game sets as an object array
        Object[] gameSubSet = lowerLevelItemSet.keySet().toArray();

        // then iterate through each of them
        for(int i = 0; i < gameSubSet.length; i++)
        {
            GameSet currentGameSet = (GameSet) gameSubSet[i];
            // after which iterate through the consecutive game sets
            for(int j = i + 1; j < gameSubSet.length; j++)
            {
                GameSet nextGameSet = (GameSet) gameSubSet[j];

                //trigger for adding frequent game set
                boolean shouldAdd = true;
                // now iterate through the individual games of a game set
                for(int z = 0; z < currentGameSet.getGamesArr().length - 1; z++)
                {
                    // if a game set and its successive neighbors have the same number of games
                    // and games are not the same until the very last one
                    if(currentGameSet.getGamesArr().length == nextGameSet.getGamesArr().length &&
                            !currentGameSet.getGamesArr()[z].equals(nextGameSet.getGamesArr()[z]))
                    {
                        // then we discard the game set and move on to the next successive game set
                        shouldAdd = false;
                        break;
                    }
                }

                // if a game set and one of its neighbors is the same to the very last game in their respective arrays of games
                if (shouldAdd)
                {
                    // then we join the sets
                    GameSet newSet = joinSets(currentGameSet, nextGameSet);

                    // and we count the support of the new game set
                    int supportCount = countSupport(newSet, gameSets);

                    // if the support is higher than the threshold we add the game set to the hashtable
                    if (supportCount >= supportThreshold)
                    {
                        result.put(newSet, supportCount);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Calculates the support of a list of games relative to games appearing in the overall database.
     *
     * @param gameSubList        GameSet representation for which we are calculating the support
     * @param gameSets           A list of all games in our database
     *
     * @return                   The number representation of support count
     */
    public int countSupport(GameSet gameSubList, List<GameSet> gameSets)
    {
        int count = 0;
        for(GameSet subList : gameSets)
        {
            int elem = 0;
            for (String item : subList.getGamesArr())
            {
                if(item.equals(gameSubList.getGamesArr()[elem]))
                {
                    elem++;
                    if(elem == gameSubList.getGamesArr().length)
                    {
                        count++;
                        break;
                    }
                }
            }
        }
        //update support count
        gameSubList.setSupportCount(count);
        return count;
    }

    /**
     * Adds the last element of the second string array onto the elements of the first.
     *
     * @param first      GameSet representation of a list of game titles
     * @param second     GameSet representation of a list of game titles
     *
     * @return           GameSet representation of the first string array + the last element of the second string array
     */
    private GameSet joinSets(GameSet first, GameSet second)
    {
        // new array of games with an extra space at the end
        String[] joinGames = new String[first.getGamesArr().length + 1];

        //copy the root games
        for(int i = 0; i < first.getGamesArr().length; i++)
        {
            joinGames[i] = first.getGamesArr()[i];
        }

        // add the last game of the second game to the extra space of the new array of games
        joinGames[joinGames.length-1] = second.getGamesArr()[second.getGamesArr().length-1];

        //create the new game set and return it
        GameSet setToReturn = new GameSet(joinGames);
        return setToReturn;
    }

    /**
     * Returns the game set containing an array of game titles
     *
     * @param input The array of game titles
     * @return      Game set containing the array of game titles.
     */
    private GameSet findGameSet(String[] input)
    {
        GameSet inputSet = new GameSet(input);
        for(GameSet game : frequentGameSets.keySet())
        {
            if(inputSet.equals(game))
            {
                return game;
            }
        }
        //if you hit this point the algorithm exploded
        return null;
    }

    /**
     * Calculates the confidence between two game sets.
     *
     * @param firstElem         Game set representation (a subeset of @param currentGameSet)
     * @param currentGameSet    Game set representation
     * @return                  Double representation of the confidence score
     */
    private double calculateConfidence(GameSet firstElem, GameSet currentGameSet)
    {
        double confidence = (double) currentGameSet.getSupportCount() / (double) firstElem.getSupportCount();

        return confidence;
    }

    //client
    public static void main(String[] args)
    {
        List<DataObject> allData = new DataFilter().generateDataObj();
        List<GameSet> allGames = new ArrayList<>();

        for(DataObject response : allData)
        {
            GameSet currentGameSet = new GameSet(response.getListOfGames());

            allGames.add(currentGameSet);
        }

        Apriori apriori= new Apriori(allGames);

        List<GameSet> result = apriori.apriori(allGames, 7);
    }
}
