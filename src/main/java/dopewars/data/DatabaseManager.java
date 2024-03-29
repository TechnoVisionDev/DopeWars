package dopewars.data;

import com.mongodb.client.model.Indexes;
import dopewars.data.cache.Market;
import dopewars.data.cache.Player;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Manages data between the bot and the MongoDB database.
 *
 * @author TechnoVision
 */
public class DatabaseManager {

    public @NotNull MongoCollection<Player> players;
    public @NotNull MongoCollection<Market> markets;

    /**
     * Connect to database using MongoDB URI and
     * initialize any collections that don't exist.
     *
     * @param uri MongoDB uri string.
     */
    public DatabaseManager(String uri) {
        // Setup MongoDB database with URI.
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(codecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(clientSettings);
        MongoDatabase database = mongoClient.getDatabase("DopeWars");

        // Initialize collections if they don't exist.
        players = database.getCollection("players", Player.class);
        markets = database.getCollection("markets", Market.class);

        // Add index to collection
        Bson userIndex = Indexes.descending("user_id");
        players.createIndex(userIndex);
    }
}
