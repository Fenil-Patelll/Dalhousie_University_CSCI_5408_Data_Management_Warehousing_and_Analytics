package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   The DataProcessingEngine class is responsible for parsing the raw JSON data
 *   from the NewsAPI, writing the news contents and titles to files, and
 *   triggering the TransformationEngine for further processing.
 */
public class DataProcessingEngine {

    /**
     * Parses the raw JSON data, writes the news contents and titles to files, and
     * triggers the TransformationEngine.
     *
     * @param jsonData The raw JSON data from the NewsAPI
     */
    public static void parseAndWriteArticles(String jsonData) {
        Pattern pattern = Pattern.compile("\"title\"\\s*:\\s*\"(.*?)\".*?\"content\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(jsonData);

        int fileIndex = 1;
        int articleCount = 0;

        while (matcher.find()) {
            String title = matcher.group(1);
            String content = matcher.group(2);

            try {
                File file = new File("news_articles_" + fileIndex + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                bufferedWriter.write("Title: " + title + "\n\n");
                bufferedWriter.write("Content: " + content + "\n\n");
                bufferedWriter.write("-------------------------------------------------\n\n");
                bufferedWriter.close();

                articleCount++;

                if (articleCount % 5 == 0) {
                    fileIndex++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Call to Transformation Engine
        TransformationEngine.transformFiles();
    }
}
