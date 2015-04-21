import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chv on 4/19/2015.
 */
public class RandomBeacon {

    //To launch - launch main() method with argumnents or not
    public static void main(String[] args) throws JAXBException {
       if (args.length >= 2){
           System.out.println("2 arguments are passed: '" + args[0] + "' and '" + args[1] +"'");
           Calendar fromDate = parseDateString(args[0]);
           Calendar toDate = parseDateString(args[1]);

           long fromDateUnix = convertToUnixDate(fromDate);
           long toDateUnix = convertToUnixDate(toDate);

           final long unixEpochTime = 1378395540;
           if (fromDateUnix < unixEpochTime|| toDateUnix < unixEpochTime){
               System.out.println("'From' or 'To' dates are less than start of unix epoch time. Please change it.");
               System.exit(0);
           }

           long diffFromTo = toDateUnix - fromDateUnix;

           TreeMap<Character, Integer> results =  new TreeMap<Character, Integer>();
           System.out.print("In progress.");
           for (int j = 0; j < (int)(diffFromTo/60 + 1); j++) {
               results = sumTreeMaps(results, countCharacters(getOutputValueFromXML(getXMLString(fromDateUnix + ""))));
               System.out.print(".");
               fromDateUnix += 60;
           }
           System.out.println();
           System.out.println("Results:");
           for (Map.Entry<Character, Integer> characterIntegerEntry : results.entrySet()) {
               System.out.println(characterIntegerEntry.getKey() + "," + characterIntegerEntry.getValue());
           }
       }
        else {
           System.out.println("Without arguments, results:");
           TreeMap<Character, Integer> results = countCharacters(getOutputValueFromXML(getXMLString("last")));
           for (Map.Entry<Character, Integer> characterIntegerEntry : results.entrySet()) {
               System.out.println(characterIntegerEntry.getKey() + "," + characterIntegerEntry.getValue());
           }
       }
    }

    public static TreeMap<Character, Integer> sumTreeMaps(TreeMap<Character, Integer> first, TreeMap<Character, Integer> second){
        TreeMap<Character, Integer> result = new TreeMap<Character, Integer>(first);

        for (Map.Entry<Character, Integer> entry : second.entrySet()) {
            Integer i = result.get(entry.getKey());
            result.put(entry.getKey(), entry.getValue() + ( i == null ? 0 : i));
        }
        return result;
    }

    public static long convertToUnixDate(Calendar date){
        date.set(Calendar.SECOND, 0);  // to prevent 406 http error
        return date.getTimeInMillis()/1000L;
    }

    // implemented only "one type" parsing: "2 hours ago" , "14 months ago", "14 days ago", "1 minute ago" etc.
    public static Calendar parseDateString(String str){
        Pattern p = Pattern.compile("(\\d+)\\s+(.*?)s? ago");

        Map<String, Integer> types = new HashMap<String, Integer>() {{
            put("minute", Calendar.MINUTE);
            put("hour",   Calendar.HOUR);
            put("day",    Calendar.DATE);
            put("month",  Calendar.MONTH);
        }};

        Matcher m = p.matcher(str);

        if (m.matches()) {
            int amount = Integer.parseInt(m.group(1));
            String unit = m.group(2);

            Calendar cal = Calendar.getInstance();
            cal.add(types.get(unit), -amount);
            return cal;
        }
        return null;
    }

    public static TreeMap<Character, Integer> countCharacters(String str){
        char[] chars = str.toCharArray();
        TreeMap<Character, Integer> treeMap = new TreeMap<Character, Integer>();

        for (int i = 0; i < chars.length; i++) {
            if (treeMap.containsKey(chars[i])){
                Integer a = treeMap.get(chars[i]);
                treeMap.put(chars[i],a + 1);
            }
            else {
                treeMap.put(chars[i],1);
            }
        }
        return treeMap;
    }


    public static String getOutputValueFromXML(String str) throws JAXBException {
        String outputValue = "";
        SAXBuilder saxBuilder = new SAXBuilder();
        try{
            Document document = saxBuilder.build(new StringReader(str));
            outputValue = document.getRootElement().getChild("outputValue").getText();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputValue;
    }

    public static String getXMLString(String record){
        String xmlString = "";
        try{
            URL url = new URL("https://beacon.nist.gov/rest/record/" + record);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/xml");

            if (connection.getResponseCode() != 200){
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output;
            while ((output = bufferedReader.readLine()) != null){
                xmlString = output;
            }
            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlString;
    }
}
