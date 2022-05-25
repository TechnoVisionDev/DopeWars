package civbot.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

/**
 * Manages data between the bot and the MongoDB database.
 *
 * @author TechnoVision
 */
public class DatabaseManager {

    public @NotNull MongoCollection<Document> users;

    /**
     * Connect to database using MongoDB URI and
     * initialize any collections that don't exist.
     *
     * @param uri MongoDB uri string.
     */
    public DatabaseManager(String uri) {
        // Setup MongoDB database with URI.
        MongoClientURI clientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(clientURI);
        MongoDatabase database = mongoClient.getDatabase("CivBot");

        // Initialize collections if they don't exist.
        users = database.getCollection("users");
    }
}
