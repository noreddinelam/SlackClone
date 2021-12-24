package shared;

public class NetCodes {
    public static String CONNECTION = "100";

    // Channel :
    public static String CREATE_CHANNEL = "200";
    public static String CREATE_CHANNEL_SUCCEED = "201";
    public static String CREATE_CHANNEL_FAILED = "202";

    public static String JOIN_CHANNEL = "300";
    public static String JOIN_CHANNEL_SUCCEED = "301";
    public static String JOIN_CHANNEL_FAILED = "302";
    public static String JOIN_CHANNEL_BROADCAST = "303";
    public static String JOIN_CHANNEL_BROADCAST_FAILED = "304";

    public static String DELETE_MESSAGE = "400";
    public static String DELETE_MESSAGE_SUCCEED = "401";
    public static String DELETE_MESSAGE_FAILED = "402";



    public static String MODIFY_MESSAGE = "500";
    public static String MODIFY_MESSAGE_SUCCEED  = "501";
    public static String MODIFY_MESSAGE_FAILED = "502";

    public static String DELETE_CHANNEL = "600";

    public static String LIST_CHANNELS_IN_SERVER = "700";
    public static String LIST_CHANNELS_IN_SERVER_SUCCEED  = "701";
    public static String LIST_CHANNELS_IN_SERVER_FAILED = "702";

    public static String LIST_OF_USER_IN_CHANNEL = "800";
    public static String List_Of_MESSAGE_IN_CHANNEL = "900";

    public static String CONSUME_MESSAGE = "1000";
    public static String MESSAGE_CONSUMED = "1001";
    public static String MESSAGE_CONSUMPTION_ERROR = "1002";
    public static String MESSAGE_BROADCAST = "1003";
    public static String MESSAGE_BROADCAST_FAILED = "1004";




    private NetCodes(){}
}
