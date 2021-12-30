package server;

import Exceptions.*;
import database.Repository;
import database.SQLTablesInformation;
import models.Channel;
import models.Message;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shared.CommunicationTypes;
import shared.FieldsRequestName;
import shared.Mapper;
import shared.NetCodes;
import shared.communication.Request;
import shared.communication.Response;
import shared.gson_configuration.GsonConfiguration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ServerImpl {

    private static final ConcurrentHashMap<String, AsynchronousSocketChannel> listOfClients = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AsynchronousSocketChannel> listOfGuests = new ConcurrentHashMap<>();
    private static final Repository repository = Repository.getRepository();
    private static final Mapper mapper = Mapper.getMapper();
    private static final Hashtable<String, Consumer<String>> listOfFunctions = new Hashtable<>();
    private static final Logger logger = LoggerFactory.getLogger(ServerImpl.class);

    private ServerImpl() {
    }

    public static void connect(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String guest = requestData.get(FieldsRequestName.guest);
        String username = requestData.get(FieldsRequestName.userName);
        String password = requestData.get(FieldsRequestName.password);
        AsynchronousSocketChannel client = listOfGuests.get(guest);
        try {
            ResultSet rs = repository.connectionDB(username, password).orElseThrow(ConnectionException::new);
            if (rs.next()) {
                Response response = new Response(NetCodes.CONNECT_SUCCEED,
                        GsonConfiguration.gson.toJson(new User(username, password)));
                String responseJson = GsonConfiguration.gson.toJson(response);
                ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
                listOfClients.put(username, client);
                listOfGuests.remove(guest);
                client.write(attachment, attachment, new ServerWriterCompletionHandler());
                attachment.clear();
                ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
                client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
            } else {
                throw new ConnectionException();
            }
        } catch (ConnectionException e) {
            Response response = new Response(NetCodes.CONNECT_FAILED, "Connection FAILED " +
                    "! Please create an account before signing in ");
            requestFailure(response, client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void register(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String guest = requestData.get(FieldsRequestName.guest);
        String username = requestData.get(FieldsRequestName.userName);
        String password = requestData.get(FieldsRequestName.password);
        AsynchronousSocketChannel client = listOfGuests.get(guest);
        try {
            repository.registerDB(username, password).orElseThrow(RegisterException::new);
            Response response = new Response(NetCodes.REGISTER_SUCCEED, "You are registered & connected !");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            listOfClients.put(username, client);
            listOfGuests.remove(guest);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (RegisterException e) {
            Response response = new Response(NetCodes.REGISTER_FAILED, "Registration FAILED ! username already exists" +
                    " !");
            requestFailure(response, client);
        }
    }

    public static void createChannel(String data) {
        //todo add to clientchannel table
        Channel requestData = GsonConfiguration.gson.fromJson(data, Channel.class);
        AsynchronousSocketChannel client = listOfClients.get(requestData.getAdmin().getUsername());
        logger.info("Create channel data received {}", requestData);
        try {
            repository.createChannelDB(requestData).orElseThrow(CreateChannelException::new);
            Response response = new Response(NetCodes.CREATE_CHANNEL_SUCCEED, GsonConfiguration.gson.toJson(requestData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", requestData.getAdmin().getUsername());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (CreateChannelException e) {
            Response response = new Response(NetCodes.CREATE_CHANNEL_FAILED, "Channel creation failed");
            requestFailure(response, client);
        }
    }

    public static void joinChannel(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        String admin = requestData.get(FieldsRequestName.adminName);
        logger.info("joining channel {} ", data);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            Response response;
            ResultSet verifyStatusChannelResultSet =
                    repository.verifyChannelStatusDB(channelName).orElseThrow(VerifyStatusChannelException::new);
            if (verifyStatusChannelResultSet.next()) {
                boolean isPublic =
                        verifyStatusChannelResultSet.getBoolean(SQLTablesInformation.channelIsPublicChannelColumn);
                if (isPublic) {
                    ResultSet verifyResultSet =
                            repository.verifyJoinChannelDB(channelName, username).orElseThrow(VerifyJoinChannelException::new);
                    if (!verifyResultSet.next()) {
                        repository.joinChannelDB(channelName, username).orElseThrow(JoinChannelException::new);
                        ResultSet resultSet =
                                repository.fetchAllUsersWithChannelName(channelName).orElseThrow(FetchAllUsersWithChannelNameException::new);
                        Response broadcastResponse = new Response(NetCodes.JOIN_CHANNEL_BROADCAST_SUCCEED,
                                username + " has joined " +
                                "the channel");
                        response = new Response(NetCodes.JOIN_CHANNEL_SUCCEED, "Channel joined");
                        String broadcastUsername;
                        AsynchronousSocketChannel broadcastClient;
                        while (resultSet.next()) {
                            broadcastUsername = resultSet.getString("username");
                            broadcastClient = listOfClients.get(broadcastUsername);
                            if (broadcastClient != null && !broadcastUsername.equalsIgnoreCase(username))
                                broadcastResponseClient(broadcastClient, broadcastResponse);
                        }
                    } else {
                        logger.info("user already joined the channel");
                        response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "Channel joining failed");
                    }

                } else {
                    logger.info("Channel is private, request sent to the admin");
                    ResultSet verifyResultSetRequest =
                            repository.verifyRequestJoinChannelDB(channelName, username).orElseThrow(VerifyJoinChannelException::new);
                    if (!verifyResultSetRequest.next()) {
                        System.out.println("request sent");
                        repository.joinChannelStatusRequestDB(admin, channelName, username).orElseThrow(ResponseJoinChannelException::new);
                        response = new Response(NetCodes.JOIN_PRIVATE_CHANNEL, "Your request is sent to the admin to " +
                                "join the channel");
                    } else {
                        System.out.println("request not sent");
                        logger.info("Request already set ");
                        response = new Response(NetCodes.REQUEST_JOIN_FAILED, "request is already set");
                    }
                }
            } else {
                response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "joining channel failed");
            }
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("username {}", username);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (JoinChannelException | VerifyStatusChannelException | ResponseJoinChannelException e) {
            Response response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "joining channel failed");
            requestFailure(response, client);
        } catch (FetchAllUsersWithChannelNameException e) {
            Response response = new Response(NetCodes.JOIN_CHANNEL_BROADCAST_FAILED, "broadcasting message error");
            requestFailure(response, client);
        } catch (VerifyJoinChannelException e) {
            Response response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "you are already in the channel");
            requestFailure(response, client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void leaveChannel(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        logger.info("leaving channel {} ", data);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            repository.leaveChannelDB(channelName, username).orElseThrow(LeaveChannelException::new);
            Response response = new Response(NetCodes.CREATE_CHANNEL_SUCCEED, "Channel left");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (LeaveChannelException e) {
            Response response = new Response(NetCodes.LEAVE_CHANNEL_FAILED, "Leaving channel failed");
            requestFailure(response, client);
        }
    }

    //TODO : there is enhancements in future (didn't handle the return of deleteMessageDB )
    public static void deleteMessage(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        int idMessage = Integer.parseInt(requestData.get(FieldsRequestName.messageID));
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            Response response = new Response(NetCodes.DELETE_MESSAGE_SUCCEED, "Message deletion succeeded");
            repository.deleteMessageDB(idMessage).orElseThrow(DeleteMessageException::new);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(response).getBytes());
            client.write(buffer, buffer, new ServerWriterCompletionHandler());
            buffer.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (DeleteMessageException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.DELETE_MESSAGE_FAILED, "Message deletion failed");
            requestFailure(response, client);
        }
    }

    //TODO : there is enhancements in future
    public static void modifyMessage(String data) {
        logger.info("modify message {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String idMessage = requestData.get(FieldsRequestName.messageID);
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            repository.modifyMessageDB(requestData.get(FieldsRequestName.messageContent), idMessage).orElseThrow(ModifyMessageException::new);
            Response response = new Response(NetCodes.MODIFY_MESSAGE_SUCCEED, "message modified");
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            logger.info("idMessage {}", idMessage);
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (ModifyMessageException e) {
            Response response = new Response(NetCodes.MODIFY_MESSAGE_FAILED, "Message modification failed");
            requestFailure(response, client);
        }

    }

    public static void deleteChannel(String data) {
        //todo broadcast channel deletion
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String channelName = requestData.get(FieldsRequestName.channelName);
        AsynchronousSocketChannel client = listOfClients.get(requestData.get(FieldsRequestName.userName));
        try {
            Response response = new Response(NetCodes.DELETE_CHANNEL_SUCCEED, data);
            repository.deleteUserWhenChannelDeletedDB(channelName).orElseThrow(DeleteUserWhenChannelDeletedException::new);
            int result = repository.deleteChannelDB(channelName).orElseThrow(DeleteChannelException::new);
            if (result != 0) {
                ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(response).getBytes());
                client.write(buffer, buffer, new ServerWriterCompletionHandler());
                buffer.clear();
                ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
                client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
            }
            else throw new DeleteChannelException();
        } catch (DeleteChannelException e) {
            Response response = new Response(NetCodes.DELETE_CHANNEL_FAILED, "Channel deletion failed");
            requestFailure(response, client);
        } catch (DeleteUserWhenChannelDeletedException e) {
            e.printStackTrace();
        }
    }

    public static void listOfRequests(String data) {
        logger.info("list of requests {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.adminName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet =
                    repository.responseJoinChannelRequestDB(username).orElseThrow(ResponseJoinChannelException::new);
            List<Map<String, String>> requests = new ArrayList<>();
            Map<String, String> usernameChannelName;
            while (resultSet.next()) {
                usernameChannelName = new HashMap<>();
                usernameChannelName.put(resultSet.getString(FieldsRequestName.channelName),
                        resultSet.getString(FieldsRequestName.userName));
                requests.add(usernameChannelName);
            }
            String responseData = GsonConfiguration.gson.toJson(requests,
                    CommunicationTypes.listMapChannelUsernameTypeData);
            Response response = new Response(NetCodes.LIST_REQUEST_JOIN_CHANNEL_SUCCEED, responseData);
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ResponseJoinChannelException e) {
            Response response = new Response(NetCodes.LIST_REQUEST_JOIN_CHANNEL_FAILED, "List requests to join channel failed");
            requestFailure(response, client);
        }
    }

    public static void responseRequests(String data) {
        logger.info("response to join channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        String acceptString = requestData.get(FieldsRequestName.accept);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            if (acceptString.equalsIgnoreCase("true")) {
                repository.joinChannelDB(channelName, username).orElseThrow(JoinChannelException::new);
                ResultSet resultSet =
                        repository.fetchAllUsersWithChannelName(channelName).orElseThrow(FetchAllUsersWithChannelNameException::new);
                Response broadcastResponse = new Response(NetCodes.JOIN_CHANNEL_BROADCAST_SUCCEED, username + " has " +
                        "joined " +
                        "the channel");
                Response response = new Response(NetCodes.JOIN_CHANNEL_SUCCEED, "Channel joined");
                String responseJson = GsonConfiguration.gson.toJson(response);
                ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
                client.write(attachment, attachment, new ServerWriterCompletionHandler());
                attachment.clear();
                ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
                client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
                String broadcastUsername;
                AsynchronousSocketChannel broadcastClient;
                while (resultSet.next()) {
                    broadcastUsername = resultSet.getString("username");
                    broadcastClient = listOfClients.get(broadcastUsername);
                    if (broadcastClient != null && !broadcastUsername.equalsIgnoreCase(username))
                        broadcastResponseClient(broadcastClient, broadcastResponse);
                }
            } else {
                Response response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "join channel refused");
                String responseJson = GsonConfiguration.gson.toJson(response);
                ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
                client.write(attachment, attachment, new ServerWriterCompletionHandler());
                attachment.clear();
            }
            repository.deleteRequestJoinChannelDB(channelName, username).orElseThrow(DeleteRequestJoinChannelException::new);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JoinChannelException | DeleteRequestJoinChannelException e) {
            Response response = new Response(NetCodes.JOIN_CHANNEL_FAILED, "joining channel failed");
            requestFailure(response, client);
        } catch (FetchAllUsersWithChannelNameException e) {
            Response response = new Response(NetCodes.JOIN_CHANNEL_BROADCAST_FAILED, "broadcasting message error");
            requestFailure(response, client);
        }
    }

    public static void listChannelsInServer(String data) {
        logger.info("list of channel in the server {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet = repository.listChannelsInServerDB().orElseThrow(ListOfUserInChannelException::new);
            List<Channel> channels = mapper.resultSetToChannel(resultSet);
            Map<String, List<Channel>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.listChannels, channels);
            Response response = new Response(NetCodes.LIST_CHANNELS_IN_SERVER_SUCCEED,
                    GsonConfiguration.gson.toJson(responseData, CommunicationTypes.mapListChannelJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfUserInChannelException e) {
            Response response = new Response(NetCodes.LIST_CHANNELS_IN_SERVER_FAILED, "list of channels failed");
            requestFailure(response, client);
        }


    }

    public static void listOfUserInChannel(String data) {
        logger.info("list of user in channel {} ", data);
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet =
                    repository.listOfUserInChannelDB(channelName).orElseThrow(ListOfUserInChannelException::new);
            List<User> users = mapper.resultSetToUser(resultSet);
            Map<String, List<User>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.userName, users);
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_SUCCEED,
                    GsonConfiguration.gson.toJson(responseData, CommunicationTypes.mapListUserJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfUserInChannelException e) {
            Response response = new Response(NetCodes.LIST_OF_USER_IN_CHANNEL_FAILED, "list of user in channel failed");
            requestFailure(response, client);
        }

    }

    public static void listOfMessageInChannel(String data) {
        logger.info("list of message in channel {} ", data);

        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        String channelName = requestData.get(FieldsRequestName.channelName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet resultSet =
                    repository.listOfMessageInChannelDB(channelName).orElseThrow(ListOfMessageInChannelException::new);
            List<Message> messages = mapper.resultSetToMessage(resultSet);
            Map<String, List<Message>> responseData = new HashMap<>();
            responseData.put(FieldsRequestName.listMessages, messages);
            Response response = new Response(NetCodes.List_Of_MESSAGE_IN_CHANNEL_SUCCEED,
                    GsonConfiguration.gson.toJson(responseData, CommunicationTypes.mapListMessageJsonTypeData));
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer attachment = ByteBuffer.wrap(responseJson.getBytes());
            client.write(attachment, attachment, new ServerWriterCompletionHandler());
            attachment.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ListOfMessageInChannelException e) {
            e.printStackTrace();
            Response response = new Response(NetCodes.List_Of_MESSAGE_IN_CHANNEL_FAILED, "list of message in channel " +
                    "failed");
            requestFailure(response, client);
        }
    }

    public static void consumeMessage(String data) {
        Message messageReceived = GsonConfiguration.gson.fromJson(data, Message.class);
        String channelName = messageReceived.getChannel().getChannelName();
        String username = messageReceived.getUser().getUsername();
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            repository.addMessageDB(messageReceived).orElseThrow(AddMessageException::new);
            ResultSet result =
                    repository.listOfUserInChannelDB(channelName).orElseThrow(ListOfUserInChannelException::new);
            Response responseSucceed = new Response(NetCodes.MESSAGE_CONSUMED, "Message consumption succeed");
            Response broadcastResponse = new Response(NetCodes.MESSAGE_BROADCAST_SUCCEED, data);
            ByteBuffer buffer = ByteBuffer.wrap(GsonConfiguration.gson.toJson(responseSucceed).getBytes());
            client.write(buffer, buffer, new ServerWriterCompletionHandler());
            buffer.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
            AsynchronousSocketChannel broadcastClient;
            String broadcastUsername;
            while (result.next()) {
                broadcastUsername = result.getString(SQLTablesInformation.clientChannelUsernameColumn);
                broadcastClient = listOfClients.get(broadcastUsername);
                if (broadcastClient != null && !broadcastUsername.equalsIgnoreCase(username))
                    broadcastResponseClient(broadcastClient, broadcastResponse);
            }
        } catch (AddMessageException e) {
            Response response = new Response(NetCodes.MESSAGE_CONSUMPTION_ERROR, "Message consumption error");
            requestFailure(response, client);
        } catch (ListOfUserInChannelException e) {
            Response response = new Response(NetCodes.MESSAGE_BROADCAST_FAILED, "Message broadcast failed");
            requestFailure(response, client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void listOfJoinedChannels(String data) {
        Map<String, String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet channels = repository.listOfJoinedChannels(username).orElseThrow(ListOfJoinedChannels::new);
            Map<String, List<Channel>> listOfChannels = new HashMap<>();
            List<Channel> joinedChannels = new ArrayList<>();
            while (channels.next()) {
                joinedChannels.add(new Channel(channels.getString(SQLTablesInformation.channelNameColumn)));
            }
            listOfChannels.put(FieldsRequestName.listChannels, joinedChannels);
            String dataJson = GsonConfiguration.gson.toJson(listOfChannels,
                    CommunicationTypes.mapListChannelJsonTypeData);
            Response response = new Response(NetCodes.LIST_OF_JOINED_CHANNELS_SUCCEEDED, dataJson);
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
            client.write(buffer, buffer, new ServerWriterCompletionHandler());
            buffer.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (ListOfJoinedChannels e) {
            Response response = new Response(NetCodes.LIST_OF_JOINED_CHANNELS_FAILED, "List of joined channels failed !");
            requestFailure(response, client);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void listOfUnJoinedChannels(String data){
        Map<String,String> requestData = GsonConfiguration.gson.fromJson(data, CommunicationTypes.mapJsonTypeData);
        String username = requestData.get(FieldsRequestName.userName);
        AsynchronousSocketChannel client = listOfClients.get(username);
        try {
            ResultSet channels = repository.listOfUnJoinedChannelsDB(username).orElseThrow(listOfUnJoinedChannelsException::new);
            Map<String, List<Channel>> listOfChannels = new HashMap<>();
            List<Channel> unJoinedChannels = new ArrayList<>();
            while (channels.next()) {

                unJoinedChannels.add(new Channel(channels.getString(SQLTablesInformation.channelNameColumn), channels.getBoolean(SQLTablesInformation.channelIsPublicChannelColumn)));
            }
            listOfChannels.put(FieldsRequestName.listChannels, unJoinedChannels);
            String dataJson = GsonConfiguration.gson.toJson(listOfChannels, CommunicationTypes.mapListChannelJsonTypeData);
            System.out.println(dataJson);
            Response response = new Response(NetCodes.LIST_OF_UN_JOINED_CHANNELS_SUCCEEDED, dataJson);
            String responseJson = GsonConfiguration.gson.toJson(response);
            ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
            client.write(buffer, buffer, new ServerWriterCompletionHandler());
            buffer.clear();
            ByteBuffer newByteBuffer = ByteBuffer.allocate(1024);
            client.read(newByteBuffer, newByteBuffer, new ServerReaderCompletionHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (listOfUnJoinedChannelsException e) {
            e.printStackTrace();
        }
    }

    public static void initListOfFunctions() {
        // initialisation of methods;
        listOfFunctions.put(NetCodes.CONNECTION, ServerImpl::connect);
        listOfFunctions.put(NetCodes.REGISTER, ServerImpl::register);
        listOfFunctions.put(NetCodes.CREATE_CHANNEL, ServerImpl::createChannel);
        listOfFunctions.put(NetCodes.JOIN_CHANNEL, ServerImpl::joinChannel);
        listOfFunctions.put(NetCodes.DELETE_MESSAGE, ServerImpl::deleteMessage);
        listOfFunctions.put(NetCodes.MODIFY_MESSAGE, ServerImpl::modifyMessage);
        listOfFunctions.put(NetCodes.DELETE_CHANNEL, ServerImpl::deleteChannel);
        listOfFunctions.put(NetCodes.LIST_CHANNELS_IN_SERVER, ServerImpl::listChannelsInServer);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL, ServerImpl::listOfUserInChannel);
        listOfFunctions.put(NetCodes.LIST_OF_USER_IN_CHANNEL, ServerImpl::listOfUserInChannel);
        listOfFunctions.put(NetCodes.CONSUME_MESSAGE, ServerImpl::consumeMessage);
        listOfFunctions.put(NetCodes.List_Of_MESSAGE_IN_CHANNEL, ServerImpl::listOfMessageInChannel);
        listOfFunctions.put(NetCodes.LIST_REQUEST_JOIN_CHANNEL, ServerImpl::listOfRequests);
        listOfFunctions.put(NetCodes.RESPONSE_JOIN_CHANNEL, ServerImpl::responseRequests);
        listOfFunctions.put(NetCodes.LIST_OF_JOINED_CHANNELS, ServerImpl::listOfJoinedChannels);
        listOfFunctions.put(NetCodes.LEAVE_CHANNEL, ServerImpl::leaveChannel);
        listOfFunctions.put(NetCodes.LIST_OF_UN_JOINED_CHANNELS, ServerImpl::listOfUnJoinedChannels);
    }

    public static Consumer<String> getFunctionWithRequestCode(Request request) {
        return listOfFunctions.get(request.getNetCode());
    }

    public static void addGuestClients(AsynchronousSocketChannel client) throws IOException {
        listOfGuests.put(client.getRemoteAddress().toString().split(":")[1], client);
    }

    private static void requestFailure(Response response, AsynchronousSocketChannel client) {
        String responseJson = GsonConfiguration.gson.toJson(response);
        ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
        client.write(buffer, buffer, new ServerWriterCompletionHandler());
        ByteBuffer bufferReader = ByteBuffer.allocate(1024);
        client.read(bufferReader, bufferReader, new ServerReaderCompletionHandler());
    }

    private static void broadcastResponseClient(AsynchronousSocketChannel broadcastClient, Response broadcastResponse) {
        String responseJson = GsonConfiguration.gson.toJson(broadcastResponse);
        ByteBuffer buffer = ByteBuffer.wrap(responseJson.getBytes());
        broadcastClient.write(buffer, buffer, new ServerWriterCompletionHandler());
    }

}
