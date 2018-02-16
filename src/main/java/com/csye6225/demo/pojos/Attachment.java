package com.csye6225.demo.pojos;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="attachment")
public class Attachment {
 //   @Id
//    @GeneratedValue(generator="system-uuid")
//    @GenericGenerator(name="system-uuid",strategy = "uuid")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
 //   private String attachmentId;
    private String path;


    @ManyToOne
    private Task task;

    public Attachment() {
        System.out.println("11111111111111111111111");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//
//    public String getAttachmentId() {
//        return attachmentId;
//    }
//
//    public void setAttachmentId(String attachmentId) {
//        this.attachmentId = attachmentId;
//    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getPath() { return path; }

    public void setPath(String path) {
        this.path = path;
    }
}
