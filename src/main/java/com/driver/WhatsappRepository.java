package com.driver;

import java.sql.Timestamp;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;

    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();

        this.customGroupCount = 0;
        this.messageId = 0;
    }


    public void addUser(User user){
        String mobileNo = user.getMobile();
        userMobile.add(mobileNo);
    }

    public boolean MobileExists(String mobile){
        return userMobile.contains(mobile);
    }

    public int createMessage(String content){
        int id = messageId+1;
        Message msg = new Message(id,content);
        this.messageId = id;
        return id;
    }

    public Group createGroup(List<User> users){
        List<Message> messageList = new ArrayList<>();
        if(users.size()==2){
            Group group = new Group(users.get(1).getName(),2);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            groupMessageMap.put(group,messageList);
            return group;
        }
        else {
            customGroupCount++;
            Group group = new Group("Group "+customGroupCount,users.size());
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            groupMessageMap.put(group,messageList);
            return group;
        }
    }

    public List<User> getUsersList(Group group){
        return groupUserMap.get(group);
    }

    public boolean groupExists(Group group){
        return groupUserMap.containsKey(group);
    }

    public int sendMessage(Message message, User sender, Group group){
        List<Message> messageList = groupMessageMap.get(group);
        int number = messageList.size();
        messageList.add(message);
        senderMap.put(message,sender);
        return number+1;
    }

    public String changeAdmin(User user, Group group) throws Exception{
        List<User> userList = groupUserMap.get(group);

        userList.remove(user);
        userList.add(0,user);

        adminMap.put(group,user);

        return "SUCCESS";
    }

    public User getAdmin(Group group){
        return adminMap.get(group);
    }
}
