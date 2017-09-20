package Preprocessing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Class used to normalize elements from the data set.
 */
public class DataCleaner
{

    /**
     * Removes quotation marks from input string
     *
     * @param input     String
     * @return          String w/o quotation marks
     */
    public String removerQuotationMarks(String input)
    {
        if(input.contains("\""))
        {
            input = input.replace("\"", "");
        }
        return input;
    }

    /**
     * Replaces commas with full stop signs.
     *
     * @param input    Input string with comma
     * @return         Input string with the comma converted to a full stop
     */
    public String changeCommaToFullStop(String input)
    {
        if(input.contains(","))
        {
            input = input.replace(",", ".");
        }

        return input;
    }

    /**
     * Sets gender information:
     *
     * Masculine gender will be set to "m"
     * Feminine gender will be set to "f"
     * Non-binary gender identity will be labeled "other"
     *
     * Exception made for the apache helicopter in the data set. Plus points for humor.
     *
     * @param input     String representation of gender
     */
    public String classifyGender(String input)
    {
        input = input.trim();
        input = removerQuotationMarks(input);

        if(input.startsWith("m"))
        {
            input = "m";
        }
        else if (input.startsWith("f"))
        {
            input = "f";
        }
        //remember to change this before you submit
        //changed in normalizer
        else if((!input.startsWith("m") || !input.startsWith("f")) && !input.equals("apache helicopter"))
        {
            input = "other";
        }
        return input;
    }

    /**
     * Sets date information
     *
     * @param input     Date information
     * @return          Formatted date
     */
    public Date normalizeDate(String input)
    {
        input = input.trim();

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm:ss aa", Locale.US);

        try
        {
            Date currentDate = format.parse(input);

            return currentDate;
        }
        catch (ParseException e)
        {
            System.out.printf("Input %s is incorrect\n", input);
            return null;
        }
    }

    /**
     * Checks if a given list of games also contains the answer "i have not played any of these games" in conjunction
     * with one or more games
     *
     * @param currentGames     a list of games from the survey
     * @return                 True if the list contains an invalid answer, False otherwise
     */
    public boolean isInvalidGameSet(String[] currentGames)
    {
        String invalidAnswer = "i have not played any of these games";

        //worth mentioning that the invalid answer appears at the very end of the list
        for(String game : currentGames)
        {
            if(game.equals(invalidAnswer) && currentGames.length > 1)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes full stops symbols.
     *
     * @param input      String with full stop symbols to remove
     */
    public String removeFullStop(String input)
    {
        if(input != null && input.contains("."))
        {
            input = input.replace(".", "");
        }
        return input;
    }
}
