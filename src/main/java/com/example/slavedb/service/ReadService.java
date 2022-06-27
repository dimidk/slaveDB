package com.example.slavedb.service;

import com.example.slavedb.Slave;
import com.example.slavedb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ReadService {

    private static Logger logger = LogManager.getLogger(ReadService.class);

    @Autowired
    private Slave slaveDB;

    @Autowired
    public ReadService(Slave slaveDB) {
        this.slaveDB = slaveDB;
    }

    public Slave getSlaveDB() {
        return slaveDB;
    }

    public void setSlaveDB(Slave slaveDB) {
        this.slaveDB = slaveDB;
    }

    protected Student fromJson(int field) {

        Student student = null;
        String slave_dir = slaveDB.getSlaveDir();
        logger.info("common FileSystem is:"+slave_dir);

        Gson json = new Gson();
        try (Reader reader = new FileReader( slave_dir+Slave.COLLECTION_DIR + String.valueOf(field)+".json")) {

            student = json.fromJson(reader,Student.class);
            logger.info(student.getUuid());

        }catch (IOException e ){
            e.printStackTrace();
        }
        return student;
    }

    public void update_db() {

        logger.info("update database's indexes");
        String dir = slaveDB.getSlaveDir()+Slave.COLLECTION_DIR;
        slaveDB.getPropertyIndex().clear();
        slaveDB.getUniqueIndex().clear();

        logger.info(slaveDB.getPropertyIndex().size()+ " " +slaveDB.getUniqueIndex().size());
        List<Path> files = slaveDB.listFiles(Path.of(dir));
        files.forEach(s -> {
            Gson json = new Gson();
            try (Reader reader = new FileReader(String.valueOf(s))) {
                Student student = json.fromJson(reader, Student.class);
                logger.info(student.getUuid()+" "+student.getSurname());
                logger.info("student in index");

                slaveDB.addUniqueIndex(student);
                slaveDB.addPropertyIndex(student);

                /*if (!slaveDB.getUniqueIndex().contains(student.getUuid())) {

                    logger.info(slaveDB.getUniqueIndex().size());
                    logger.info(slaveDB.getPropertyIndex().size());
                    slaveDB.addPropertyIndex(student);
                    slaveDB.addUniqueIndex(student);
                    logger.info(slaveDB.getPropertyIndex().size());
                    logger.info(slaveDB.getUniqueIndex().size());
                }*/

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    protected String getKeyByValue(TreeMap<String,List<String>> tree, String value) {

        String findKey = null;


        for (Map.Entry<String, List<String>> e: tree.entrySet()) {
            String key = (String) e.getKey();
            logger.info(key);
            for (String s :e.getValue()) {
                if (s.equals(value)) {
                    findKey = key;
                    break;
                }
            }
        }

        return findKey;
    }

    public void update_json(String uuid) {

        logger.info("update property indexes");
        String dir = slaveDB.getSlaveDir()+Slave.COLLECTION_DIR;
        Student student = fromJson(Integer.valueOf(uuid));

        String old_surname = getKeyByValue(slaveDB.getPropertyIndex(),uuid);
        logger.info("find surname before update" +old_surname);
        boolean a = slaveDB.getPropertyIndex().containsKey(student.getUuid());

        slaveDB.addPropertyIndex(student);

        List<String> uuids = slaveDB.getPropertyIndex().get(old_surname);
        if (uuids.size() == 1 ) {

            //slaveDB.deletePropertyIndex();
            logger.info("Property name there is only once, therefor delete the record");
            slaveDB.getPropertyIndex().remove(old_surname,slaveDB.getPropertyIndex().get(old_surname));
            //List<String> temp = slaveDB.getPropertyIndex().get(old_surname);
            //slaveDB.getPropertyIndex().remove(student.getSurname(),temp);
            logger.info(slaveDB.getPropertyIndex().size());

            /*logger.info("insert new property name");
            slaveDB.getPropertyIndex().put(student.getSurname(),new ArrayList<>());
            slaveDB.getPropertyIndex().get(student.getSurname()).add(uuid);
            logger.info(slaveDB.getPropertyIndex().size());*/
        }
        else {
            logger.info("size of property index before update "+slaveDB.getPropertyIndex().size());
            for (String s:uuids) {
                if (s.equals(uuid) && !a) {
                    slaveDB.getPropertyIndex().get(old_surname).remove(uuid);
                    logger.info("remove an element from list with uuids for property");
                    logger.info(slaveDB.getPropertyIndex().get(old_surname).size());

                    /*logger.info("add new property name");
                    logger.info("size of property index before "+slaveDB.getPropertyIndex().size());
                    slaveDB.getPropertyIndex().put(student.getSurname(),new ArrayList<>());
                    slaveDB.getPropertyIndex().get(student.getSurname()).add(uuid);
                    logger.info("size of property index after "+slaveDB.getPropertyIndex().size());*/
                }


            }
        }
    }


    public List<Student> read() {

        List<Student> students = new ArrayList<>();
        logger.info("read uuid index for all students");
        TreeSet<Integer> uuids = slaveDB.getUniqueIndex();
        uuids.stream().sorted().forEach(s -> {
            logger.info("read each student");
            Student student = fromJson(s);
            students.add(student);
        });
        return students;
    }

    public Student read(String uuid) {

        Student student = null;
        logger.info("read student");
        student = fromJson(Integer.valueOf(uuid));
        return student;

    }

    public List<Student> studName(String name) {

        List<Student> students = new ArrayList<>();
        TreeMap<String,List<String>> propIndex = slaveDB.getPropertyIndex();
        List<String> uuids = propIndex.get(name);

        for(String uuid:uuids){
            logger.info("this is student with "+uuid);
            Student student = fromJson(Integer.valueOf(uuid));
            students.add(student);
        }
        return students;
    }


    public List<Student> studByName() {

        List<Student> studNames = new ArrayList<>();

        TreeMap<String,List<String>> propIndex = slaveDB.getPropertyIndex();
        for (Map.Entry s:propIndex.entrySet()) {
            String name = (String) s.getKey();
            List<String> uuids = (List<String>) s.getValue();

            for (String uuid:uuids){
                Student student = fromJson(Integer.valueOf(uuid));
                studNames.add(student);
                logger.info(student.getUuid() +" "+student.getSurname());
            }
        }

        return studNames;
    }

}
