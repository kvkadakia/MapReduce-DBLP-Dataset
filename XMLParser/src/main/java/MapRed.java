import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.typesafe.config.Config;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;
import com.typesafe.config.ConfigFactory;

/*This class is used to parse the dblp xml
file and save the pairs and names of the cs
faculties found into a text file named
author_pairs.txt*/

public class MapRed {
        static Logger log = LoggerFactory.getLogger(MapRed.class);
        static Config conf = ConfigFactory.parseResources("application.conf");

        public static void main(String argv[]) throws FileNotFoundException {

        //Store the list of cs faculties
        final List<String> csFaculties = new ArrayList<String>();

        //Used to match cs faculties from dblp
        File file = new File(conf.getString("csFaculties.name"));
        Scanner sc = new Scanner(file);
        while(sc.hasNextLine())
        {
            csFaculties.add(sc.nextLine());
        }

        try {
            //SAX Parser to parse the huge xml file
            Properties props = System.getProperties();
            props.setProperty("entityExpansionLimit", "2000000");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
            boolean barticle = false;
            boolean bauthor = false;
            boolean btitle = false;
            boolean bnname = false;
            boolean bsalary = false;
            boolean csFaculty = false;

            //Stores the pair of authors
            List<String> authors = new ArrayList<String>();


            //called whenever a start tag is detected
            public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException {

                //if the start tag is article or inproceedings
                if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
                    barticle = true;
                    //store all the authors that come now
                }

                //if the start tag is author
                if (qName.equalsIgnoreCase("author") && barticle == true) {
                    bauthor = true;
                    //store the name of the author
                }
            }

            //called whenever the end tag is detected
            public void endElement(String uri, String localName, String qName) throws SAXException {

                //if the end tag is article
                if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
                    bauthor = false;
                    barticle = false;
                    String auth = "";

                    //cs author with no collaborations
                    if(authors.size()==1)
                    {
                        log.info("Single author " + authors.get(0));
                        appendStrToFile(authors.get(0)+"\n");
                    }

                    //if author has collaborations
                    else if (authors.size() > 1 && authors.size() < 3) {
                        for (int i = 0; i < authors.size(); i++) {
                            if (i == 0)
                                auth = authors.get(i);
                            else
                                auth = auth + "," + authors.get(i);
                        }
                        log.info("Collaboration between 2 authors " + auth);
                         appendStrToFile(auth + "\n");
                    }

                    //more than 2 collaborations
                    else if (authors.size() > 2) {
                        for (int i = 0; i < authors.size() - 1; i++) {
                            String authPair = "";
                            for (int j = i + 1; j < authors.size(); j++) {
                                authPair = authors.get(i) + "," + authors.get(j);
                            }
                            log.info("Collaboration between more than 2 authors " + authPair);
                            appendStrToFile(authPair + "\n");
                        }
                    }
                    authors.clear();
                }
            }

            //called whenever there is some text inside the xml tags
            public void characters(char ch[], int start, int length) throws SAXException {
                if (bauthor) {
                    //Store the name of author
                    String authorName = new String(ch, start, length);
                    if(csFaculties.contains(authorName) && !authors.contains(authorName)) {
                        log.info("Add author to list");
                        authors.add(authorName);
                    }
                }
            }

            };

            saxParser.parse("dblp.xml", handler);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean appendStrToFile(String str){
        try {
            // Open given file in append mode.
            if(str != null) {
                BufferedWriter out = new BufferedWriter(new FileWriter(conf.getString("outputfileName.output"), true));
                out.write(str);
                log.info(str);
                out.close();
                return true;
            }
            else{
                return false;
            }
        }
        catch (IOException e) {
            log.error("exception occoured" + e);
            return false;
        }
    }


}