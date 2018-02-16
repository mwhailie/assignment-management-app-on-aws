package com.csye6225.demo.controllers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csye6225.demo.ingore.HardCodeEnum;
import com.csye6225.demo.pojos.Attachment;
import com.csye6225.demo.pojos.Task;
import com.csye6225.demo.repositories.AttachmentRepository;
import com.csye6225.demo.repositories.TaskRepository;
import com.csye6225.demo.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.List;

@Controller
public class FileIOController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;


    private final static Logger logger = LoggerFactory.getLogger(FileIOController.class);

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.POST)
    @ResponseBody
    public String attachFile(@PathVariable String id,  @RequestParam("file") MultipartFile file,HttpServletResponse response) throws Exception {
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        JsonObject jsonObject = new JsonObject();
        Task task;
        try {
            task = taskRepository.findOne(id);
        }catch (Exception e){
            jsonObject.addProperty("message", "task does not exist");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        String folder = "/myFile";
        String relativePath = System.getProperty("user.dir");
        //getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String filePath = saveFile(file, "/home/ubuntu"/*relativePath + folder*/);

        Attachment attachment = new Attachment();
        attachment.setPath(filePath);
        attachment.setTask(task);
        attachmentRepository.save(attachment);

        //Upload to S3
        String bucketName     = "csye6225bucket-cloudformation1.com";
        String keyName        = task.getId() + ":" + attachment.getId().toString();
        File fileToUpload;
        try {
            fileToUpload = transferFile(file, "/home/ubuntu"/*relativePath + folder*/);
        }catch (IOException e){
            jsonObject.addProperty("Error Message: " , e.getMessage());
            jsonObject.addProperty("Error Type       " , e.getClass().toString());
            return jsonObject.toString();
        }
        catch (Exception e){
            jsonObject.addProperty("Error Message: " , e.getMessage());
            jsonObject.addProperty("Error Type       " , e.getClass().toString());
            return jsonObject.toString();
        }

        try {
            AmazonS3 s3client = new AmazonS3Client(DefaultAWSCredentialsProviderChain.getInstance());
            bucketName = s3client.listBuckets().get(2).getName();
            s3client.putObject(new PutObjectRequest(bucketName, keyName, fileToUpload));
        } catch (AmazonServiceException ase) {
            jsonObject.addProperty("Status", "Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            jsonObject.addProperty("Error Message    ",ase.getMessage());
            jsonObject.addProperty("HTTP Status Code ",  ase.getStatusCode());
            jsonObject.addProperty("AWS Error Code   " , ase.getErrorCode());
            jsonObject.addProperty("Error Type       " , ase.getErrorType().toString());
            jsonObject.addProperty("Request ID       " , ase.getRequestId());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();

        } catch (AmazonClientException ace) {
            jsonObject.addProperty("Status", "Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            jsonObject.addProperty("Error Message: " , ace.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }catch (Exception e) {
            jsonObject.addProperty("Error Type: " , e.getClass().toString());
            jsonObject.addProperty("Error Message: " , e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return jsonObject.toString();
        }

        jsonObject.addProperty("path", attachment.getPath());
        jsonObject.addProperty("task", attachment.getTask().toString());
        jsonObject.addProperty("attachment_id", attachment.getId());
        response.setStatus(HttpServletResponse.SC_OK);
        //jsonObject.addProperty("description", task.getDescription());
        return jsonObject.toString();
    }

    //save file
    private String saveFile(MultipartFile file, String path) throws IOException {

        if(!file.isEmpty()) {
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);

            //if path not exist, create the folder
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            String finalPath = path + File.separator + filename;

            //transfer the files into the target folder
            file.transferTo(new File(finalPath));
            return finalPath;
        } else {
            return "file not exist";
        }
    }

    //transfer file
    private File transferFile(MultipartFile file, String path) throws IOException {
        if(!file.isEmpty()) {
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);
            logger.error("size of file is--------------------------------------" + filepath.length());
            return filepath;
        } else {
            logger.error("empty multipartfile");
            throw new IOException("empty multipartfile");
        }
    }

    @RequestMapping(value = "/tasks/{id}/attachments", method = RequestMethod.GET)
    @ResponseBody
    public String listFile(@PathVariable String id) throws Exception {
        Task task = taskRepository.findOne(id);
        //List<Attachment> attachList = task.getAttachmentList();
        List<Attachment> attachList = attachmentRepository.findByTask(task);

        JsonArray array = new JsonArray();
        if (attachList.size() > 0) {
            for (int i = 0; i < attachList.size(); i++) {
                try {
                    JsonObject e = new JsonObject();
                    Attachment curAttachment = attachList.get(i);
                    e.addProperty("id", curAttachment.getId());
                    e.addProperty("path", curAttachment.getPath());
                    array.add(e);
                } catch (Exception e) {
                    logger.error("Error in deserializing received constraints", e);
                    return null;
                }
            }
        }
        return array.toString();
    }

    @RequestMapping(value = "/tasks/{id}/attachments/{idAttachments}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteFile(@PathVariable String id, @PathVariable String idAttachments, HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        Attachment attachment = attachmentRepository.findOne(new Integer(idAttachments));
        Task task = taskRepository.findOne(id);
        String filePath = attachment.getPath();
        JsonObject jsonObject = new JsonObject();
        boolean deleteSuccess = false;
        if(attachment.getTask() == task) {
            deleteSuccess = delete(filePath);
            if(deleteSuccess){
                attachmentRepository.delete(new Integer(idAttachments));
            }
            jsonObject.addProperty("message", deleteSuccess ? "Delete Successfully" : "Delete Failed: no such file");
        }
        else{
            jsonObject.addProperty("message", "Delete Failed: Attachment not match the task");
        }
        return jsonObject.toString();
    }

    private static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("[log] Delete File failed:" + fileName + "not exist！");
            return false;
        } else {
            if (file.isFile())
                return file.delete();
        }
        return false;

    }


}
