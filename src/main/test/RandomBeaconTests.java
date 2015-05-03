import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.util.Calendar;
import java.util.TreeMap;

/**
 * Created by chvs on 03.05.2015.
 */
public class RandomBeaconTests {

    @DataProvider
    public Object[][] convertToUnixDateData(){
        Calendar firstDate = Calendar.getInstance();
        firstDate.set(2015, Calendar.MAY, 1, 22, 55);
        long firstExpected = 1430510100;

        Calendar secondDate = Calendar.getInstance();
        secondDate.set(2008, Calendar.DECEMBER, 27, 20, 17, 53); //with seconds
        long secondExpected = 1230401820;

        Calendar thirdDate = Calendar.getInstance();
        thirdDate.set(1970, Calendar.JANUARY, 5, 18, 37, 11); //with seconds
        long thirdExpected = 405420;

        return new Object[][]{
                new Object[]{firstDate, firstExpected},
                new Object[]{secondDate, secondExpected},
                new Object[]{thirdDate, thirdExpected},
        };
    }

    @Test(dataProvider = "convertToUnixDateData")
    public void convertToUnixDateTest(Calendar date, long expected){
        Assert.assertEquals(RandomBeacon.convertToUnixDate(date), expected);
    }

    @DataProvider
    public Object[][] parseDateStringData(){
        Calendar firstExpDate = Calendar.getInstance();
        firstExpDate.set(Calendar.MILLISECOND, 0);
        firstExpDate.set(Calendar.SECOND, 0);
        firstExpDate.add(Calendar.MINUTE, -1); //"1 minute ago"

        Calendar secondExpDate = Calendar.getInstance();
        secondExpDate.set(Calendar.MILLISECOND, 0);
        secondExpDate.set(Calendar.SECOND, 0);
        secondExpDate.add(Calendar.HOUR, -25);//"25 hours ago"

        Calendar thirdExpDate = Calendar.getInstance();
        thirdExpDate.set(Calendar.MILLISECOND, 0);
        thirdExpDate.set(Calendar.SECOND, 0);
        thirdExpDate.add(Calendar.DAY_OF_YEAR, -140);//"140 days ago"

        Calendar fourthExpDate = Calendar.getInstance();
        fourthExpDate.set(Calendar.MILLISECOND, 0);
        fourthExpDate.set(Calendar.SECOND, 0);
        fourthExpDate.add(Calendar.MONTH, -3);//"3 months ago"

        return new Object[][]{
                new Object[]{"1 minute ago", firstExpDate},
                new Object[]{"25 hours ago", secondExpDate},
                new Object[]{"140 days ago", thirdExpDate},
                new Object[]{"3 months ago", fourthExpDate},
        };
    }

    @Test(dataProvider = "parseDateStringData")
    public void parseDateStringTest(String str, Calendar expected){
        Calendar actualDate = RandomBeacon.parseDateString(str);
        actualDate.set(Calendar.SECOND, 0);
        actualDate.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(actualDate, expected);
    }

    @DataProvider
    public Object[][] sumTreeMapsData(){
        TreeMap<Character, Integer> firstMapF = new TreeMap<Character, Integer>();
        firstMapF.put('A', 5);
        firstMapF.put('D', 2);
        TreeMap<Character, Integer> firstMapS = new TreeMap<Character, Integer>();
        firstMapS.put('A', 7);
        firstMapS.put('F', 5);
        TreeMap<Character, Integer> firstExpMap = new TreeMap<Character, Integer>();
        firstExpMap.put('A', 12);
        firstExpMap.put('F', 5);
        firstExpMap.put('D', 2);

        TreeMap<Character, Integer> secondMapF = new TreeMap<Character, Integer>();
        secondMapF.put('1', 14);
        secondMapF.put('E', 4);
        secondMapF.put('G', 2);
        secondMapF.put('2', 1);
        secondMapF.put('5', 7);
        secondMapF.put('C', 4);
        secondMapF.put('A', 1);
        TreeMap<Character, Integer> secondMapS = new TreeMap<Character, Integer>();
        secondMapS.put('F', 7);
        secondMapS.put('G', 2);
        secondMapS.put('3', 3);
        secondMapS.put('1', 5);
        TreeMap<Character, Integer> secondExpMap = new TreeMap<Character, Integer>();
        secondExpMap.put('1', 19);
        secondExpMap.put('E', 4);
        secondExpMap.put('G', 4);
        secondExpMap.put('2', 1);
        secondExpMap.put('5', 7);
        secondExpMap.put('C', 4);
        secondExpMap.put('A', 1);
        secondExpMap.put('F', 7);
        secondExpMap.put('3', 3);

        TreeMap<Character, Integer> thirdMapF = new TreeMap<Character, Integer>();
        thirdMapF.put('B', 1);
        thirdMapF.put('4', 1);
        thirdMapF.put('2', 1);
        thirdMapF.put('3', 1);
        thirdMapF.put('A', 1);
        thirdMapF.put('F', 1);
        TreeMap<Character, Integer> thirdMapS = new TreeMap<Character, Integer>();
        thirdMapS.put('7', 1);
        thirdMapS.put('3', 1);
        thirdMapS.put('5', 1);
        thirdMapS.put('2', 1);
        thirdMapS.put('B', 1);
        thirdMapS.put('A', 1);
        TreeMap<Character, Integer> thirdExpMap = new TreeMap<Character, Integer>();
        thirdExpMap.put('2', 2);
        thirdExpMap.put('3', 2);
        thirdExpMap.put('4', 1);
        thirdExpMap.put('5', 1);
        thirdExpMap.put('7', 1);
        thirdExpMap.put('A', 2);
        thirdExpMap.put('B', 2);
        thirdExpMap.put('F', 1);

        TreeMap<Character, Integer> fourthMapF = new TreeMap<Character, Integer>();
        fourthMapF.put('A', 1);
        TreeMap<Character, Integer> fourthMapS = new TreeMap<Character, Integer>();
        fourthMapS.put('B', 1);
        TreeMap<Character, Integer> fourthExpMap = new TreeMap<Character, Integer>();
        fourthExpMap.put('A', 1);
        fourthExpMap.put('B', 1);

        return new Object[][]{
                new Object[]{ firstMapF, firstMapS, firstExpMap},
                new Object[]{ secondMapF, secondMapS, secondExpMap},
                new Object[]{ thirdMapF, thirdMapS, thirdExpMap},
                new Object[]{ fourthMapF, fourthMapS, fourthExpMap},
        };
    }

    @Test(dataProvider = "sumTreeMapsData")
    public void sumTreeMapsTest(TreeMap<Character, Integer> firstMap, TreeMap<Character, Integer> secondMap, TreeMap<Character, Integer> expectedMap){
        Assert.assertEquals(RandomBeacon.sumTreeMaps(firstMap, secondMap), expectedMap);
    }

    @DataProvider
    public Object[][] countCharactersData(){
        String firstString = "AAABBB11";
        TreeMap<Character, Integer> firstExpected = new TreeMap<Character, Integer>();
        firstExpected.put('A', 3);
        firstExpected.put('B', 3);
        firstExpected.put('1', 2);

        String secondString = "ABCDEF123456789001123456789ABCDEF";
        TreeMap<Character, Integer> secondExpected = new TreeMap<Character, Integer>();
        secondExpected.put('0', 2);
        secondExpected.put('1', 3);
        secondExpected.put('2', 2);
        secondExpected.put('3', 2);
        secondExpected.put('4', 2);
        secondExpected.put('5', 2);
        secondExpected.put('6', 2);
        secondExpected.put('7', 2);
        secondExpected.put('8', 2);
        secondExpected.put('9', 2);
        secondExpected.put('A', 2);
        secondExpected.put('B', 2);
        secondExpected.put('C', 2);
        secondExpected.put('D', 2);
        secondExpected.put('E', 2);
        secondExpected.put('F', 2);

        String thirdString = "13F23G122F31GH";
        TreeMap<Character, Integer> thirdExpected = new TreeMap<Character, Integer>();
        thirdExpected.put('1', 3);
        thirdExpected.put('2', 3);
        thirdExpected.put('3', 3);
        thirdExpected.put('F', 2);
        thirdExpected.put('G', 2);
        thirdExpected.put('H', 1);

        String fourthString = "85DC0E946CEFA5E81ABEE47B2579887760D764E25AF3A06370BEA0A2CA7C1B99830DE956BCC2C89BFC690E580B578E1706DBA2A221C2B045321758C5DEC2D468";
        TreeMap<Character, Integer> fourthExpected = new TreeMap<Character, Integer>();
        fourthExpected.put('0', 10);
        fourthExpected.put('1', 5);
        fourthExpected.put('2', 10);
        fourthExpected.put('3', 4);
        fourthExpected.put('4', 5);
        fourthExpected.put('5', 10);
        fourthExpected.put('6', 8);
        fourthExpected.put('7', 10);
        fourthExpected.put('8', 10);
        fourthExpected.put('9', 7);
        fourthExpected.put('A', 9);
        fourthExpected.put('B', 9);
        fourthExpected.put('C', 11);
        fourthExpected.put('D', 6);
        fourthExpected.put('E', 11);
        fourthExpected.put('F', 3);

        return new Object[][]{
                new Object[]{firstString, firstExpected},
                new Object[]{secondString, secondExpected},
                new Object[]{thirdString, thirdExpected},
                new Object[]{fourthString, fourthExpected},
        };
    }

    @Test(dataProvider = "countCharactersData")
    public void countCharactersTest(String str, TreeMap<Character, Integer> expectedMap){
        Assert.assertEquals(RandomBeacon.countCharacters(str), expectedMap);
    }

    @DataProvider
    public Object[][] getOutputValueFromXMLData(){
        return new Object[][]{
                new Object[]{"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><record><outputValue>5CDAED</outputValue></record>",
                        "5CDAED"},
                new Object[]{"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><record><version>Version 1.0</version><outputValue>90B66F6DFBD4C7465C37FFEDEC073BCB2368DD49B7B4FDEAE4D4282C213B5A65BD67E0F5F062F0C8049D337ABD4F0708D57592E004A1B430F4CB6625485FA6AD</outputValue></record>",
                        "90B66F6DFBD4C7465C37FFEDEC073BCB2368DD49B7B4FDEAE4D4282C213B5A65BD67E0F5F062F0C8049D337ABD4F0708D57592E004A1B430F4CB6625485FA6AD"},
                new Object[]{"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><record><version>Version 1.0</version><frequency>60</frequency><timeStamp>1430679660</timeStamp><seedValue>021EF04624D1207A3214ED22B1219C291AAF9523A66D12192245E7D724E10E62E2D3647636A6E41D7C8EAE02E706715D39ACD5EEBC40A5B65B5AD24DE27C3B22</seedValue><previousOutputValue>205F5E25C4DFE3451362558745F42BCD63338954A8F728A65735843793950B0BA17CCDE23CF6D3A4EFF14FA4BBA4DBE048B018305B02761879D5162BF159A989</previousOutputValue><signatureValue>B5C3CF42D0B841E3A4E268BCEB19BFFAA92B07538DD6C2A39ED4ECD78815F8F4825877EFC924B0BBDF40180B07E7A3DE92AE2C651EB20AE9DC49413150C471D7123B42A7BBCFEC333049B1C98A9A518FFABBF0C07659680C30762647136E14F8A5A126405414859D143C6D2E04C14A0450A202A09B1AF224EBE55C405E43B2EDE61B46E943614E054DCE4A95116634E1020BE9BB522A8464816395D49DFA133B103516A2F7E069FA6D5E78456B0141DFBD948CB494C483C0DBF586A8DF21FDC2639BC5EDE5861B2E5ADB342D609933F83C50D898B5EC191CC2F0EE852F439E1A5CFD8524E3EE3F3DAA73AB049BE97872C5D4278042A32E5E5BDD5772D8094B1D</signatureValue><outputValue>F2A50C99E6A3EE26BBE2D5E783E0814D0B5E8156E5B57FE1D2F7B87D6529C4B8B88F720A5A0498FBA8DA19DBF83538F78D088F914BE0C9D53BB3A163DE319FB1</outputValue><statusCode>0</statusCode></record>",
                        "F2A50C99E6A3EE26BBE2D5E783E0814D0B5E8156E5B57FE1D2F7B87D6529C4B8B88F720A5A0498FBA8DA19DBF83538F78D088F914BE0C9D53BB3A163DE319FB1"},
        };
    }

    @Test(dataProvider = "getOutputValueFromXMLData")
    public void getOutputValueFromXMLTest(String strXML, String expected) throws JAXBException {
        Assert.assertEquals(RandomBeacon.getOutputValueFromXML(strXML), expected);
    }

    @DataProvider
    public Object[][] getXMLStringData(){
        return new Object[][]{
                new Object[]{"1430510100", "<outputValue>FA7F89F4844C3CE625FA5887DDB03D6643E2FCD1713B001B5531059B7483470BAAC640BA7E5C93B0C09FE6F1FA44832B63C0E37DD3C8BE1207D7C673027A289D</outputValue>"},
                new Object[]{"33420", "<outputValue>17070B49DBF3BA12BEA427CB6651ECF7860FDC3792268031B77711D63A8610F4CDA551B7FB331103889A62E2CB23C0F85362BBA49B9E0086D1DA0830E4389AB1</outputValue>"},
                new Object[]{"1123433400", "<outputValue>17070B49DBF3BA12BEA427CB6651ECF7860FDC3792268031B77711D63A8610F4CDA551B7FB331103889A62E2CB23C0F85362BBA49B9E0086D1DA0830E4389AB1</outputValue>"},
                new Object[]{"1430680080", "<outputValue>2751ADDD916590ED0512BBCE40FFC381CD52206493CCE45AD0BA678D906EA24771697C889C285CA5ECCD2357DFA48CA6559141C218FE669F87BFC365DB50C209</outputValue>"},
        };
    }

    @Test(dataProvider = "getXMLStringData")
    public void getXMLStringTest(String record, String expected){
        Assert.assertTrue(RandomBeacon.getXMLString(record).contains(expected));
    }

    //To check that correct quantity of hexidecimal characters(128) in outputValue tag
    @Test
    public void checkQuantityOfCharacters() throws JAXBException {
        Assert.assertEquals(RandomBeacon.getOutputValueFromXML(RandomBeacon.getXMLString("last")).toCharArray().length, 128);
    }
}
