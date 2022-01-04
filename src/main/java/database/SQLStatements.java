package database;

import static database.SQLTablesInformation.*;

public class SQLStatements {
    public static String listChannelsInServer =
            "SELECT * "
                    + "FROM " + channelTable + " ;";
    /*
   public static String listChannelsInServer =
           "SELECT * "
                   + "FROM " + channelTable
                   + " INNER JOIN " + clientTable + "ON " + channelTable +
                   "." + channelAdminUsernameColumn + "=" + clientTable +
                   "." + clientUsernameColumn + ";";
   */
    public static String listOfUserInChannel =
            "SELECT " + clientChannelUsernameColumn
                    + " FROM " + clientChannelTable
                    + " where " + clientChannelChannelNameColumn + "= ? ;";

    public static String listOfMessageInChannel =
            "SELECT * "
                    + " FROM " + messageTable
                    + " where " + messageChannelNameColumn + " = ? ;";


    public static String joinChannel =
            "INSERT INTO " + clientChannelTable
                    + "(" + clientChannelChannelNameColumn + "," + clientChannelUsernameColumn + ")"
                    + " VALUES (?,?) ;";

    public static String verifyJoinChannel =
            "SELECT *" + " FROM " +
                    clientChannelTable + " WHERE " +
                    clientChannelChannelNameColumn + "=?" +
                    " AND " + clientChannelUsernameColumn + "=? ;";

    public static String verifyRequestJoinChannel =
            "SELECT *" + " FROM " +
                    requestTable + " WHERE " +
                    requestChannelName + "=?" +
                    " AND " + requestUsername + "=? ;";

    public static String verifyChannelStatus =
            "SELECT isPublic" + " FROM " +
                    channelTable + " WHERE " +
                    channelNameColumn + "=?;";

    public static String addRequestJoinChannel =
            "INSERT INTO " + requestTable
                    + "(" + requestAdminName + "," + requestChannelName + "," + requestUsername
                    + ")" +
                    " VALUES (?,?,?) ;";
    public static String deleteUserFromMyChannel =
            "DELETE FROM " + clientChannelTable
                    + " WHERE " + clientChannelChannelNameColumn + " =? AND " +
                    clientChannelUsernameColumn + "=? ;";


    public static String joinListChannelRequest =
            "SELECT " + requestChannelName + "," + requestUsername + " FROM " + requestTable
                    + " WHERE " + requestAdminName + "=? ;";

    public static String deleteRequestJoinChannel =
            "DELETE FROM " + requestTable
                    + " WHERE " + requestChannelName + "=? AND " + requestUsername + "=? ;";

    public static String createChannel =
            "INSERT INTO " + channelTable
                    + "(" + channelNameColumn + "," + channelAdminUsernameColumn + ","
                    + channelDescriptionColumn
                    + "," + channelIsPublicChannelColumn + ")" +
                    " VALUES (?,?,?,?) ;";

    public static String createUser =
            "INSERT INTO " + clientTable
                    + "(" + clientUsernameColumn + "," + clientPasswordColumn + ") "
                    + " VALUES (?,?) ;";

    public static String insertAdminClientChannelTable =
            "INSERT INTO " + clientChannelTable
                    + "(" + clientChannelChannelNameColumn + "," + clientChannelUsernameColumn + ") "
                    + " VALUES (?,?) ;";

    public static String addMessage =
            "INSERT INTO " + messageTable
                    + "(" + messageContentColumn + "," + messageChannelNameColumn + ","
                    + messageUsernameColumn
                    + "," + messageDate + ")" +
                    " VALUES (?,?,?,?) ;";

    public static String modifyMessage =
            "UPDATE " + messageTable
                    + " SET " + messageContentColumn + "= ? "
                    + " WHERE " + messageIdMessageColumn + " = ? ;";

    public static String fetchMessageFromChannel =
            "SELECT * FROM " + messageTable + " where " + messageIdMessageColumn + "=? ;";

    public static String fetchAllUsersWithChannelName = "SELECT * "
            + " FROM  " + clientChannelTable
            + " where " + clientChannelChannelNameColumn + "= ? ;";

    public static String deleteMessage = "DELETE FROM " + messageTable
            + " WHERE " + messageIdMessageColumn + " =? ;";

    public static String deleteChannel = "DELETE FROM " + channelTable
            + " WHERE " + channelNameColumn + " =? ;";

    public static String verifyConnection = "SELECT * " +
            " FROM " + clientTable +
            " WHERE " + clientUsernameColumn + " = ? and " + clientPasswordColumn + " = ? ;";

    public static String register =
            "INSERT INTO " + clientTable
                    + "(" + clientUsernameColumn + "," + clientPasswordColumn + ")"
                    + " VALUES (?,?) ;";

    public static String listOfJoinedChannels =
            "SELECT * FROM " + clientChannelTable + " INNER JOIN " + channelTable + " ON "
                    + clientChannelTable + "." + clientChannelChannelNameColumn + " = " + channelTable + "." + channelNameColumn
                    + " WHERE " + clientChannelUsernameColumn + " = ?";

    public static String listOfUnJoinedChannels =
            "SELECT  * FROM " + channelTable +
                    " WHERE " + channelNameColumn + " NOT IN (SELECT " + clientChannelChannelNameColumn
                    + " FROM " + clientChannelTable + " WHERE " +
                    clientChannelUsernameColumn + "=?);";

    public static String leaveChannel =
            "DELETE FROM " + clientChannelTable
                    + " WHERE " + requestChannelName + "=?  AND " + requestUsername + "=? ;";

    public static String modifyChannel =
            "UPDATE " + channelTable
                    + " SET " + channelNameColumn + "=? , "+  channelIsPublicChannelColumn + "=?  WHERE " + channelNameColumn + "=? ;";

//    public static String modifyChannelStatus =
//            "UPDATE " + channelTable
//                    + "  SET " + channelIsPublicChannelColumn + "=?  WHERE " + channelNameColumn + "=? ;";

    private SQLStatements() {
    }
}

