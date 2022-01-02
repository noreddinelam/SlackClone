package shared;

public class NetCodes {

    public static String LOGOUT ="99";
    public static String LOGOUT_SUCCEED ="999";

    public static String REGISTER = "0";
    public static String REGISTER_SUCCEED = "1";
    public static String REGISTER_FAILED = "2";

    public static String CONNECTION = "100";
    public static String CONNECT_SUCCEED = "101";
    public static String CONNECT_FAILED = "102";

    // Channel :
    public static String CREATE_CHANNEL = "200";
    public static String CREATE_CHANNEL_SUCCEED = "201";
    public static String CREATE_CHANNEL_FAILED = "202";

    public static String JOIN_CHANNEL = "300";
    public static String JOIN_CHANNEL_SUCCEED = "301";
    public static String JOIN_CHANNEL_FAILED = "302";
    public static String JOIN_CHANNEL_BROADCAST_SUCCEED = "303";
    public static String JOIN_CHANNEL_BROADCAST_FAILED = "304";
    public static String JOIN_PRIVATE_CHANNEL = "305";

    public static String DELETE_MESSAGE = "400";
    public static String DELETE_MESSAGE_SUCCEED = "401";
    public static String DELETE_MESSAGE_FAILED = "402";

    public static String MODIFY_MESSAGE = "500";
    public static String MODIFY_MESSAGE_SUCCEED = "501";
    public static String MODIFY_MESSAGE_FAILED = "502";

    public static String DELETE_CHANNEL = "600";
    public static String DELETE_CHANNEL_SUCCEED = "601";
    public static String DELETE_CHANNEL_FAILED = "602";
    public static String DELETE_CHANNEL_BROADCAST_SUCCEEDED = "603";
    public static String DELETE_CHANNEL_BROADCAST_FAILED = "604";

    public static String LIST_CHANNELS_IN_SERVER = "700";
    public static String LIST_CHANNELS_IN_SERVER_SUCCEED = "701";
    public static String LIST_CHANNELS_IN_SERVER_FAILED = "702";

    public static String LIST_OF_USER_IN_CHANNEL = "800";
    public static String LIST_OF_USER_IN_CHANNEL_SUCCEED = "801";
    public static String LIST_OF_USER_IN_CHANNEL_FAILED = "802";

    public static String List_Of_MESSAGE_IN_CHANNEL = "900";
    public static String List_Of_MESSAGE_IN_CHANNEL_SUCCEED = "901";
    public static String List_Of_MESSAGE_IN_CHANNEL_FAILED = "902";

    public static String CONSUME_MESSAGE = "1000";
    public static String MESSAGE_CONSUMED = "1001";
    public static String MESSAGE_CONSUMPTION_ERROR = "1002";
    public static String MESSAGE_BROADCAST_SUCCEED = "1003";
    public static String MESSAGE_BROADCAST_FAILED = "1004";

    public static String LIST_REQUEST_JOIN_CHANNEL = "1100";
    public static String LIST_REQUEST_JOIN_CHANNEL_SUCCEED = "1101";
    public static String LIST_REQUEST_JOIN_CHANNEL_FAILED = "1102";

    public static String RESPONSE_JOIN_CHANNEL = "1200";
    public static String RESPONSE_JOIN_SUCCEED = "1201";
    public static String RESPONSE_JOIN_FAILED = "1202";

    public static String REQUEST_JOIN_FAILED = "1302";

    public static String LIST_OF_JOINED_CHANNELS = "1500";
    public static String LIST_OF_JOINED_CHANNELS_SUCCEEDED = "1501";
    public static String LIST_OF_JOINED_CHANNELS_FAILED = "1502";

    public static String LIST_OF_UN_JOINED_CHANNELS = "1600";
    public static String LIST_OF_UN_JOINED_CHANNELS_SUCCEEDED = "1601";
    public static String LIST_OF_UN_JOINED_CHANNELS_FAILED = "1602";

    public static String LEAVE_CHANNEL = "1400";
    public static String LEAVE_CHANNEL_SUCCEED = "1401";
    public static String LEAVE_CHANNEL_FAILED = "1402";

    public static String DELETE_USER_FROM_CHANNEL = "1700";
    public static String DELETE_USER_FROM_CHANNEL_SUCCEED = "1701";
    public static String DELETE_USER_FROM_CHANNEL_FAILED = "1702";

    public static String MODIFY_CHANNEL = "1800";
    public static String MODIFY_CHANNEL_SUCCEED = "1801";
    public static String MODIFY_CHANNEL_FAILED = "1802";
    public static String MODIFY_CHANNEL_BROADCAST_SUCCEED = "1803";
    public static String MODIFY_CHANNEL_BROADCAST_FAILED = "1804";

//    public static String MODIFY_CHANNEL_STATUS = "1900";
//    public static String MODIFY_CHANNEL_STATUS_SUCCEED = "1901";
//    public static String MODIFY_CHANNEL_STATUS_FAILED = "1902";


    private NetCodes() {
    }
}
