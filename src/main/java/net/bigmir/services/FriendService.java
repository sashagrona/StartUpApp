package net.bigmir.services;

import net.bigmir.model.Friend;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.repositories.FriendRepository;
import net.bigmir.repositories.SimpleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class FriendService {

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Transactional
    public boolean isFriend(SimpleUser simpleUser, String friendEmail){
        Set<Friend> friends = simpleUserRepository.findFriends(simpleUser);
        for (Friend f: friends) {
            if (f.getEmail().equals(friendEmail)){
                return true;
            }
        }
        return false;
    }


    @Transactional
    public boolean isParticipant(StartUp startUp, String friendEmail){
        Set<SimpleUser> users = startUp.getUsers();
        for (SimpleUser u:users) {
            if(u.getEmail().equals(friendEmail)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteFriendByUser(SimpleUser user, String email){
        friendRepository.deleteFriendByUser(email, user);

    }
}
