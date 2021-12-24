package shared;

import com.google.gson.reflect.TypeToken;
import models.Channel;
import models.Message;
import models.User;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CommunicationTypes {
    public static Type mapJsonTypeData = new TypeToken<Map<String, String>>() {
    }.getType();
    public static Type mapListChannelJsonTypeData = new TypeToken<Map<String, List<Channel>>>() {
    }.getType();
    public static Type mapListUserJsonTypeData = new TypeToken<Map<String, List<User>>>() {
    }.getType();
    public static Type mapListMessageJsonTypeData = new TypeToken<Map<String, List<Message>>>() {
    }.getType();

}
