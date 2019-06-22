package net.bigmir.services;

import net.bigmir.model.BusinessPlan;
import net.bigmir.model.Chat;
import net.bigmir.model.SimpleUser;
import net.bigmir.model.StartUp;
import net.bigmir.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class StartUpService {

    @Autowired
    private StartUpRepository startUpRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Transactional
    public void createNewStartUp(StartUp startUp, SimpleUser user) {
        user.addStartUpToCreator(startUp);
        user.addStartUpToUser(startUp);
        BusinessPlan plan = new BusinessPlan();
        startUp.setCreator(user);
        startUp.setPlan(plan);
        startUpRepository.save(startUp);
        Chat chat = new Chat(startUp.getName());

        user.addChat(chat);
        chatRepository.save(chat);
    }

    @Transactional
    public void deleteStartUpFromUser(SimpleUser user, StartUp startUp){
        chatRepository.deleteChatForUser(user.getId(), chatRepository.getChatByName(startUp.getName()).getId());
        startUpRepository.deleteStartUpFromUser(user.getId(), startUp.getId());
    }

    @Transactional
    public void updateStartUp(StartUp startUp) {
        if (startUpRepository.existsByName(startUp.getName())) {
//            if user inputs nothing, data from database doesn't change
            StartUp s = startUpRepository.findByName(startUp.getName());
            if (!("").equals(startUp.getLocation())) {
                s.setLocation(startUp.getLocation());
            }
            if (!("").equals(startUp.getIdea())) {
                s.setIdea(startUp.getIdea());
            }
            s.setPlan(startUp.getPlan());
            if (startUp.getStartCapital()!=null) {
                s.setStartCapital(startUp.getStartCapital());
            }

            startUpRepository.save(s);
        }
    }

    @Transactional
    public void deleteStartUp(StartUp startUp){
        taskRepository.deleteTasksByStartUp(startUp.getPlan());
        chatMessageRepository.deleteChatMessageByChat(chatRepository.getChatByName(startUp.getName()));
        chatRepository.deleteChatByName(startUp.getName());
        documentRepository.deleteDocumentByStartUp(startUp);
        startUpRepository.deleteStartup(startUp);
    }


    @Transactional
    public Set<StartUp> getMyStartUps(SimpleUser user) {
        return startUpRepository.findAllByUser(user);
    }

    @Transactional
    public StartUp findByName(String name) {
        return startUpRepository.findByName(name);
    }


}
