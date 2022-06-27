package com.example.slavedb.controllers;

import com.example.slavedb.schema.Student;
import com.example.slavedb.SlaveDB;
import com.example.slavedb.service.ReadService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ReadControllers {

    private static Logger logger = LogManager.getLogger(ReadControllers.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReadService service;

    @Autowired
    public ReadControllers(ReadService service) {
        this.service = service;
    }

    @GetMapping("/read")
    public List<Student> read() {

        logger.info("read all students");
        List<Student> students = service.read();
        return students;

    }

    @GetMapping("/read/{uuid}")
    public Student read(@PathVariable String uuid) {

        logger.info("read student with uuid:"+uuid);
        Student student = service.read(uuid);
        return student;
    }

    @GetMapping("/read/stud-name")
    public List<Student> read_name(){

        logger.info("read students in alphabetical order");
        List<Student> lStud = service.studByName();
        return lStud;
    }

    @GetMapping("/update-db")
    public void update_db(){

        logger.info("update replica indexes");
        service.update_db();
    }

    @GetMapping("/update-json/{uuid}")
    public void update_json(@PathVariable String uuid){

        logger.info("update replica indexes");
        service.update_json(uuid);
    }

    @GetMapping("/read/stud-name/{surname}")
    public List<Student> read_name(@PathVariable String surname){

        logger.info("read students in alphabetical order with given name:"+surname);
        List<Student> students = service.studName(surname);
        return students;
    }
}
