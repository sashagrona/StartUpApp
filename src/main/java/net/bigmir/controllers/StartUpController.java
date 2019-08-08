package net.bigmir.controllers;

import com.google.gson.Gson;
import net.bigmir.dto.StartUpDTO;
import net.bigmir.dto.TaskDTO;
import net.bigmir.dto.result.BadRequestResult;
import net.bigmir.dto.result.ResultDTO;
import net.bigmir.dto.result.SuccessResult;
import net.bigmir.model.*;
import net.bigmir.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/startup")
public class StartUpController {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private StartUpService startUpService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private TaskService taskService;

    @RequestMapping("/add")
    public ResponseEntity<ResultDTO> addStartUp(Authentication authentication, @RequestBody StartUpDTO startUpDTO){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        StartUp startUp = StartUp.fromDTO(startUpDTO);
        startUpService.createNewStartUp(startUp,user);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/{name}")
    public String modifyStartUp(@PathVariable("name") String startUpName, Authentication authentication, Model model){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        StartUp startUp = startUpService.findByName(startUpName);
        Chat chat = chatService.getChatByName(startUpName);
        if (simpleUserService.ifUserBelongsChat(chat,user)) {
            model.addAttribute("login", user.getLogin());
            model.addAttribute("photo", user.getPictureURL());
            model.addAttribute("sIdea", startUp.getIdea());
            model.addAttribute("sName", startUpName);
            model.addAttribute("sLocation", startUp.getLocation());
            model.addAttribute("sPlan", startUp.getPlan());
            model.addAttribute("sCapital", startUp.getStartCapital());
            model.addAttribute("participants", startUp.getUsers());
            model.addAttribute("creator", startUp.getCreator().getEmail());
            if (user.getEmail().equals(startUp.getCreator().getEmail())){
                model.addAttribute("isCreator", true);
            }
            return "startup_page";
        }else {
            return "redirect:/forbidden";
        }
    }

    @RequestMapping("{name}/update/")
    public String updateStartUp(@PathVariable("name") String name, String idea, String location,Long capital){
        StartUp startUp = new StartUp.StartUpBuilder()
                .mergeByName(name)
                .addIdea(idea)
                .addStartCapital(capital)
                .addLocation(location)
                .addPlan(startUpService.findByName(name).getPlan())
                .build();
        startUpService.updateStartUp(startUp);
        return "redirect:/startup/" + name;
    }

    @RequestMapping("/{name}/add_participant")
    public ResponseEntity<ResultDTO> addParticipant(@PathVariable("name") String startUpName, @RequestParam("json") String friendEmailJson){
        StartUp startUp = startUpService.findByName(startUpName);
        Gson gson = new Gson();
        String friendEmail = gson.fromJson(friendEmailJson, String.class);
        SimpleUser userFriend = simpleUserService.findByEmail(friendEmail);
        userFriend.addStartUpToUser(startUp);
        Chat chat = chatService.getChatByName(startUpName);
        userFriend.addStartUpToUser(startUp);
        userFriend.addChat(chat);
        startUpService.updateStartUp(startUp);
        simpleUserService.saveUser(userFriend);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/{name}/document")
    public String showDocuments(@PathVariable("name") String startUpName, Authentication authentication, Model model){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        Chat chat = chatService.getChatByName(startUpName);
        List<Document> documents = documentService.getDocumentsFromStartUp(startUpService.findByName(startUpName));
        if (simpleUserService.ifUserBelongsChat(chat,user)){
            model.addAttribute("login", user.getLogin());
            model.addAttribute("pictureURL", user.getPictureURL());
            model.addAttribute("sName", startUpName);
            if (!documents.isEmpty()){
                model.addAttribute("documents", documents);
            }
            return "documents";
        }
        return "redirect:/forbidden";
    }

    @RequestMapping("/{name}/plan")
    public String showBusinessPlan(@PathVariable("name") String startUpName, Model model, Authentication authentication){
        SimpleUser user = simpleUserService.getSimpleUserFromAuth(authentication);
        StartUp startUp = startUpService.findByName(startUpName);
        BusinessPlan plan = startUp.getPlan();
        List<Task> tasks = taskService.getTasksFromPlan(plan);
        List<TaskDTO> taskDTOS = new ArrayList<>();
        tasks.forEach(t -> taskDTOS.add(t.toDTO()));
        if (simpleUserService.ifUserBelongsChat(chatService.getChatByName(startUpName),user)){
            model.addAttribute("login", user.getLogin());
            model.addAttribute("pictureURL", user.getPictureURL());
            model.addAttribute("sName", startUpName);
            if (!taskDTOS.isEmpty()) {
                model.addAttribute("tasks", taskDTOS);
            }
            return "tasks";
        }
        return "redirect:/forbidden";
    }

    @RequestMapping("/{name}/plan/add")
    public ResponseEntity<ResultDTO> addTaskToPlan(@PathVariable("name") String startUpName, @RequestBody TaskDTO taskDTO){
        StartUp startUp = startUpService.findByName(startUpName);
        BusinessPlan plan = startUp.getPlan();
        Task task = taskDTO.fromDTO();
        task.setDone(false);
        task.setPlan(plan);
        taskService.saveTask(task);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/delete")
    public ResponseEntity<ResultDTO> deleteStartUp(@RequestParam("json") String json){
        Gson gson = new Gson();
        String name = gson.fromJson(json, String.class);
        StartUp startUp = startUpService.findByName(name);
        startUpService.deleteStartUp(startUp);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/{name}/delete_user")
    public ResponseEntity<ResultDTO> deleteUserFromStartUp(@PathVariable("name") String startUpName, @RequestParam("json") String json){
        Gson gson = new Gson();
        String email = gson.fromJson(json, String.class);
        StartUp startUp = startUpService.findByName(startUpName);
        startUpService.deleteStartUpFromUser(simpleUserService.findByEmail(email), startUp);
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/task/delete")
    public ResponseEntity<ResultDTO> deleteTasks(@RequestParam("json") String json){
        Gson gson= new Gson();
        Long [] deleted = gson.fromJson(json, Long[].class);
        List<Long> list = removeButtonId(deleted);
        if (!list.isEmpty()) {
            taskService.deleteTasksById(list);
        }
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    @RequestMapping("/task/done")
    public ResponseEntity<ResultDTO> doneTasks(@RequestParam("json") String json){
        Gson gson= new Gson();
        Long [] doneTasks = gson.fromJson(json, Long[].class);
        List<Long> list = removeButtonId(doneTasks);
        if (!list.isEmpty()){
            taskService.matchAsDone(list);
        }
        return new ResponseEntity<>(new SuccessResult(), HttpStatus.OK);
    }

    private List<Long> removeButtonId(Long [] tasks){
        List<Long> list = new ArrayList<>();
        for (Long i : tasks){
            list.add(i);
        }
        //To remove the first "1", cause button has checked as well
        for (Long l : list){
            if (l==1){
                list.remove(l);
                break;
            }
        }
        return list;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultDTO> handleException() {
        return new ResponseEntity<>(new BadRequestResult(), HttpStatus.BAD_REQUEST);
    }
}
