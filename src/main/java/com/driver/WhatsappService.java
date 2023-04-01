package com.driver;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WhatsappService {
    WhatsappRepository whatsappRepository = new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        if(whatsappRepository.MobileExists(mobile)) throw new Exception("User already exists");
        else {
            User user = new User(name,mobile);
            whatsappRepository.addUser(user);
            return "SUCCESS";
        }
    }

    public int createMessage(String content){
       return whatsappRepository.createMessage(content);
    }

    public Group createGroup(List<User> users){

        return whatsappRepository.createGroup(users);

    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        if(!whatsappRepository.groupExists(group)) throw  new Exception("Group does not exist");

        List<User> UserList = whatsappRepository.getUsersList(group);
        boolean flag = false;
        for(User u : UserList){
            flag = ((u==sender)||flag);
        }

        if (!flag) throw new Exception("You are not allowed to send message");
        else {
            return whatsappRepository.sendMessage(message, sender,group);
        }
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{

        if(!whatsappRepository.groupExists(group)) throw new Exception("Group does not exist");
        User admin = whatsappRepository.getAdmin(group);
        if(admin!=approver) throw new Exception("Approver does not have rights");

        List<User> UserList = whatsappRepository.getUsersList(group);
        boolean flag = false;
        for(User u : UserList){
            flag = ((u==user)||flag);
        }
        if (!flag) throw new Exception("User is not a participant");

        return whatsappRepository.changeAdmin(user,group);
    }
}
