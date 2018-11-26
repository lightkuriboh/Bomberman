package uet.oop.bomberman.database;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.print.Doc;
import javax.swing.text.AbstractDocument;

public class Mongo {
    private static final String db_name = "bomberDB";
    private static final String collection_name = "actions";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection mongoCollection;

    public static int getAction(String player) {
        int ans = -1;
        Document qr = new Document();
        qr.append("name", player);
        FindIterable<Document> findIterable = mongoCollection.find(qr);
        for (Document doc: findIterable) {
            ans = doc.getInteger("action");
        }
        return ans;
    }

    public static void updateAction(String player, int action) {
        Document find = new Document();
        find.append("name", player);

        Document update = new Document();
        update.append("action", action);

        Document updateObject = new Document();
        updateObject.append("$set", update);

        mongoCollection.findOneAndUpdate(find, updateObject);
    }

    public static void ping() {
        mongoClient = null;
        try {
            mongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        mongoDatabase = mongoClient.getDatabase(db_name);
        mongoCollection = mongoDatabase.getCollection(collection_name);

        updateAction("player1", 0);
        updateAction("player2", 0);


//        try {
//            Document player1 = new Document();
//            player1.append("name", "player1");
//            player1.append("action", 0);
//
//            Document player2 = new Document();
//            player2.append("name", "player2");
//            player2.append("action", 0);
//
//            mongoCollection.insertOne(player1);
//            mongoCollection.insertOne(player2);
//            System.out.println("insert completed!");
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }

        System.out.println("Done!");
    }
}
