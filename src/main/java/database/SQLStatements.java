package database;

import static database.SQLTablesInformation.*;

public class SQLStatements {
    public static String listChannelsInServer =
            "SELECT * "
                    + "FROM " + channelTable
                    + "INNER JOIN " + clientTable + "ON " + channelTable +
                    "." + channelAdminUsernameColumn + "=" + clientTable +
                    "." + clientUsernameColumn + ";";
    public static String listOfUserInChannel =
            "SELECT " + clientChannelUsernameColumn
                    + " FROM " + clientChannelTable
                    + " where " + clientChannelChannelNameColumn + "= ? ;";
    public static String listOfMessageInChannel =
            "SELECT " + messageContentColumn
                    + " FROM " + messageTable
                    + " where " + messageContentColumn + " = ? ;";
    public static String joinChannel =
            "INSERT INTO " + clientChannelTable
                    + "(" + clientChannelChannelNameColumn + "," + clientChannelUsernameColumn + ")"
                    + "VALUES (?,?) ;";
    public static String createChannel =
            "INSERT INTO " + channelTable
                    + "(" + channelNameColumn + "," + channelAdminUsernameColumn + ","
                    + channelDescriptionColumn
                    + "," + channelIsPublicChannelColumn + ")" +
                    "VALUES (?,?,?,?) ;";

    public static String createUser =
            "INSERT INTO " + clientTable
                    + "(" + clientUsernameColumn + "," + clientPasswordColumn + ") "
                    + "VALUES (?,?) ;";

    public static String addMessage =
            "INSERT INTO " + messageTable
                    + "(" + messageContentColumn + "," + messageChannelNameColumn + ","
                    + messageUsernameColumn
                    + "," + messageDate + ")" +
                    "VALUES (?,?,?,?) ;";

    public static String modifyMessage =
            "UPDATE " + messageTable
                    + " set " + messageContentColumn + "=? "
                    + " WHERE " + messageIdMessageColumn + " = ? ;";

    public static String fetchMessageFromChannel =
            "SELECT * FROM " + messageTable + " where " + messageIdMessageColumn + "=? ;";

    public static String fetchAllUsersWithChannelName = "SELECT " + clientChannelChannelNameColumn
            + "FROM  " + clientChannelTable
            + "where " + clientChannelChannelNameColumn + "= ? ;";

    public static String deleteMessage = "DELETE FROM " + messageTable
            + "WHERE " + messageIdMessageColumn + " =? ;";

    private SQLStatements() {
    }
}

