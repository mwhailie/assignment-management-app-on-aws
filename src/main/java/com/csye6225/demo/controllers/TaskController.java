package com.csye6225.demo.controllers;

import com.csye6225.demo.pojos.Attachment;
import com.csye6225.demo.pojos.Task;
import com.csye6225.demo.pojos.User;
import com.csye6225.demo.repositories.UserRepository;

import com.csye6225.demo.repositories.TaskRepository;
import com.csye6225.demo.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;


@Controller
public class TaskController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    private final static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @RequestMapping(value = "/tasks", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createTasks(@RequestBody String sTask, Principal principal, HttpServletRequest request, HttpServletResponse response) {

        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        if(gson.fromJson(sTask,Task.class).getDescription().length() >= 4096){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonObject.addProperty("message", "task: bad request : size too large");
            return jsonObject.toString();
        }
        Task task = gson.fromJson(sTask,Task.class);

        User user;
        try{
            user = userRepository.findByEmail(principal.getName());
        }catch (Exception e){
            jsonObject.addProperty("message", "user does not exist");
            return jsonObject.toString();
        }
        task.setUser(user);

        taskRepository.save(task);
        response.setStatus(HttpServletResponse.SC_CREATED);
        jsonObject.addProperty("message", "task: "+task.getId());
        return jsonObject.toString();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public String updateTasks(@PathVariable("id") String id, Principal principal,@RequestParam String description, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();

        Task task ;
        try{
            task = taskRepository.findOne(id);
        }catch (Exception e){
            jsonObject.addProperty("message", "task does not exist");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }
        User user;
        try{
            user = userRepository.findByEmail(principal.getName());
        }catch (Exception e){
            jsonObject.addProperty("message", "user does not exist");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }
        if(user != task.getUser()){
            jsonObject.addProperty("message", "user does not match");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return jsonObject.toString();
        }
        task.setDescription(description);
        taskRepository.save(task);
        jsonObject.addProperty("task_id", task.getId());
        jsonObject.addProperty("description", task.getDescription());
        return jsonObject.toString();
    }


    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteTask (@PathVariable String id, Principal principal,HttpServletResponse response) {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        JsonObject jsonObject = new JsonObject();
        Task task ;
        try{
            task = taskRepository.findOne(id);
        }catch (Exception e){
            jsonObject.addProperty("message", "task does not exist");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }
        User user;
        try{
            user = userRepository.findByEmail(principal.getName());
        }catch (Exception e){
            jsonObject.addProperty("message", "user does not exist");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }
        if(user != task.getUser()){
            jsonObject.addProperty("message", "user does not match");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return jsonObject.toString();
        }
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        taskRepository.delete(id);
        jsonObject.addProperty("message", "Delete task " + id +" successfully! ");
        return jsonObject.toString();
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    @ResponseBody
    public String listTasks( Principal principal,HttpServletResponse response) throws Exception {
        JsonObject jsonObject = new JsonObject();
        User user;
        try{
            user = userRepository.findByEmail(principal.getName());
        }catch (Exception e){
            jsonObject.addProperty("message", "user does not exist");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        List<Task> tasks = taskRepository.findTaskByUser_id(user.getId());
        JsonArray array = new JsonArray();
        if (tasks.size() > 0) {
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    JsonObject e = new JsonObject();
                    Task curTask = tasks.get(i);
                    e.addProperty("id", curTask.getId());
                    e.addProperty("path", curTask.getDescription());
                    array.add(e);
                } catch (Exception e) {
                    logger.error("Error in deserializing received constraints", e);
                    return null;
                }
            }
        }
        return array.toString();
    }

}
