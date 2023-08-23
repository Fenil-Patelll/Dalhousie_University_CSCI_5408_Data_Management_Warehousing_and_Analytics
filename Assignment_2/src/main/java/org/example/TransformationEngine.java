package org.example;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *  The TransformationEngine class is responsible for cleaning and transforming
 *  the data stored in files, and uploading the cleaned data to MongoDB.
 */
public class TransformationEngine {


    /**
     *Transforms and cleans the content of each file, and uploads the cleaned content to MongoDB.
     */
  public static void transformFiles() {
    File folder = new File(".");
    File[] listOfFiles = folder.listFiles((dir, name) -> name.startsWith("news_articles_") && name.endsWith(".txt"));

    if (listOfFiles != null) {
        String connectionString = "";
        MongoClient mongoClient = MongoClients.create(connectionString);

        for (File file : listOfFiles) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.getName())));
                String[] articles = content.split("Title: ");

                for (String article : articles) {
                    if (article.trim().isEmpty()) {
                        continue;
                    }

                    String[] splitContent = article.split("\n", 2);
                    String title = splitContent[0].trim();
                    String articleContent = splitContent.length > 1 ? splitContent[1].replaceFirst("\nContent: ", "").trim() : "";

                    String cleanedTitle = cleanAndTransformData(title);
                    String cleanedArticleContent = cleanAndTransformData(articleContent);

                    // Upload cleaned title and content to MongoDB
                    storeTransformedData(cleanedTitle, cleanedArticleContent, mongoClient);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mongoClient.close();
    }
}

    /**
     * Cleans and transforms the given content by removing URLs, special characters,
     * emoticons, and multiple spaces.
     * @param content The content to be cleaned and transformed
     * @return The cleaned and transformed content
     */
    public static String cleanAndTransformData(String content) {
        // Remove URLs
        String cleanedContent = content.replaceAll("https?://\\S+\\s?", "");

        // Remove special characters
        cleanedContent = cleanedContent.replaceAll("[^a-zA-Z0-9 .,?!-]+", " ");

        // Remove multiple spaces
        cleanedContent = cleanedContent.replaceAll("\\s{2,}", " ");

        cleanedContent = cleanedContent.replaceAll("[\uD83C-\uDBFF\uDC00-\uDFFF]+", "");

        return cleanedContent;
    }

    /**
     * Uploads the cleaned content to a MongoDB collection.
     * @param cleanedTitle Title of the text
     * @param cleanedContent Contecnt of the news Articles
     * @param mongoClient mongoClient object for storing the data
     */
    public static void storeTransformedData(String cleanedTitle, String cleanedContent, MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("myMongoNews");
        MongoCollection<Document> collection = database.getCollection("cleanedArticles");

        Document document = new Document("title", cleanedTitle)
                .append("content", cleanedContent);
        collection.insertOne(document);
    }
}
