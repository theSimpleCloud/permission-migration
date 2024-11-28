package app.simplecloud.migration.permission.database

import com.google.gson.JsonParser
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Paths
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.UUID

object DatabaseHelper {

    fun getDatabaseType(plugin: JavaPlugin): String {

        val path = plugin.dataFolder.toPath().toAbsolutePath().parent.parent.parent.parent.resolve("storage/database.json").toString()
        val file = File(path)


        val jsonString = file.readText()


        val jsonElement = JsonParser.parseString(jsonString)
        val jsonObject = jsonElement.asJsonObject

        return jsonObject.get("databaseType").asString
    }

    fun getUniqueIdsFromSqlite(plugin: JavaPlugin): List<String> {
        val path = plugin.dataFolder.toPath().toAbsolutePath().parent.parent.parent.parent.resolve("storage/database.db").toString()
        val dbUrl = "jdbc:sqlite:$path"
        val uniqueIds = mutableListOf<String>()

        val connection: Connection? = DriverManager.getConnection(dbUrl)
        val statement = connection?.createStatement()
        val resultSet: ResultSet? = statement?.executeQuery("SELECT uniqueId FROM cloud_players")

        while (resultSet?.next() == true) {
            uniqueIds.add(resultSet.getString("uniqueId"))
        }

        resultSet?.close()
        statement?.close()
        connection?.close()

        return uniqueIds
    }

    fun getUniqueIdsFromMySQL(plugin: JavaPlugin): List<String> {
        val path = plugin.dataFolder.toPath().toAbsolutePath().parent.parent.parent.parent.resolve("storage/database.json").toString()
        val file = File(path)
        val jsonString = file.readText()
        val jsonElement = JsonParser.parseString(jsonString)
        val jsonObject = jsonElement.asJsonObject

        val host = jsonObject.get("host").asString
        val port = jsonObject.get("port").asInt
        val databaseName = jsonObject.get("databaseName").asString
        val userName = jsonObject.get("userName").asString
        val password = jsonObject.get("password").asString

        val dbUrl = "jdbc:mysql://$host:$port/$databaseName"
        val uniqueIds = mutableListOf<String>()

        val connection: Connection? = DriverManager.getConnection(dbUrl, userName, password)
        val statement = connection?.createStatement()
        val resultSet: ResultSet? = statement?.executeQuery("SELECT uniqueId FROM cloud_players")

        while (resultSet?.next() == true) {
            uniqueIds.add(resultSet.getString("uniqueId"))
        }

        resultSet?.close()
        statement?.close()
        connection?.close()

        return uniqueIds
    }

    fun getUniqueIdsFromMongoDB(plugin: JavaPlugin): List<String> {
        val path = plugin.dataFolder.toPath().toAbsolutePath().parent.parent.parent.parent.resolve("storage/database.json").toString()
        val file = File(path)
        val jsonString = file.readText()
        val jsonElement = JsonParser.parseString(jsonString)
        val jsonObject = jsonElement.asJsonObject

        val host = jsonObject.get("host").asString
        val port = jsonObject.get("port").asInt
        val databaseName = jsonObject.get("databaseName").asString
        val userName = jsonObject.get("userName").asString
        val password = jsonObject.get("password").asString
        val collectionPrefix = jsonObject.get("collectionPrefix").asString

        val connectionString = "mongodb://$userName:$password@$host:$port"
        val client = MongoClients.create(connectionString)
        val database = client.getDatabase(databaseName)
        val collection: MongoCollection<Document> = database.getCollection("${collectionPrefix}players")

        val uniqueIds = mutableListOf<String>()
        val cursor = collection.find().iterator()

        while (cursor.hasNext()) {
            val document = cursor.next()
            uniqueIds.add(document.getString("uniqueId"))
        }

        cursor.close()
        client.close()

        return uniqueIds
    }

    fun selectDatabase(plugin: JavaPlugin): List<String> {
        return if (getDatabaseType(plugin) == "sqlite".uppercase()) {
            getUniqueIdsFromSqlite(plugin)
        } else if (getDatabaseType(plugin) == "mysql".uppercase()) {
            getUniqueIdsFromMySQL(plugin)
        } else if (getDatabaseType(plugin) == "mongodb".uppercase()) {
            getUniqueIdsFromMongoDB(plugin)
        } else {
            emptyList()
        }
    }

}