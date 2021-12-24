package database;

public class SQLTablesInformation {

    public static String channelTable = "CHANNEL";
    public static String channelNameColumn = "channelName";
    public static String channelAdminUsernameColumn = "adminUsername";
    public static String channelDescriptionColumn = "description";
    public static String channelIsPublicChannelColumn = "isPublic";

    public static String clientTable = "CLIENT";
    public static String clientUsernameColumn = "username";
    public static String clientPasswordColumn = "password";

    public static String clientChannelTable = "CLIENT_CHANNEL";
    public static String clientChannelChannelNameColumn = "channelName";
    public static String clientChannelUsernameColumn = "username";

    public static String messageTable = "MESSAGE";
    public static String messageIdMessageColumn = "id";
    public static String messageContentColumn = "content";
    public static String messageChannelNameColumn = "channelName";
    public static String messageUsernameColumn = "username";
    public static String messageDate = "date";

    private SQLTablesInformation(){}
}
