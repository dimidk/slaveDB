package com.example.slavedb;

import com.example.slavedb.schema.Student;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class Slave {


    public static final String DATABASE_DIR = "db/";
    public static final String COLLECTION_DIR = "db/student/";
    //protected static final String COLLECTION_DIR = "db/student";

    private TreeSet<Integer> uniqueIndex = new TreeSet<>();
    private TreeMap<String, List<String>> propertyIndex = new TreeMap<>();

    private String slaveDir;

    public Slave(String slaveDir) {
        this.slaveDir = slaveDir;
    }

    public TreeSet<Integer> getUniqueIndex() {
        return uniqueIndex;
    }

    public void setUniqueIndex(TreeSet<Integer> uniqueIndex) {
        this.uniqueIndex = uniqueIndex;
    }

    public TreeMap<String, List<String>> getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(TreeMap<String, List<String>> propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public String getSlaveDir() {
        return slaveDir;
    }

    public void setSlaveDir(String slaveDir) {
        this.slaveDir = slaveDir;
    }

    public void addPropertyIndex(Student stud) {

        if (stud == null) {
            //    logger.info("ERROR");
            throw new IllegalArgumentException();
        }
        //    if (this.getPropertyIndex().containsKey(stud.getSurname()))
        if (propertyIndex.containsKey(stud.getSurname()))
            propertyIndex.get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        else {
            propertyIndex.put(stud.getSurname(),new ArrayList<>());
            propertyIndex.get(stud.getSurname()).add(String.valueOf(stud.getUuid()));
        }
    }

    public void deletePropertyIndex(Student stud) {

        if (stud == null)
            throw new IllegalArgumentException();

        List<String> temp = this.getPropertyIndex().get(stud.getSurname());
        propertyIndex.remove(stud.getSurname(),temp);

        temp.remove(String.valueOf(stud.getUuid()));
        propertyIndex.put(stud.getSurname(),temp);
    }

    public void addUniqueIndex(Student stud) {

        if (stud == null) {
            throw new IllegalArgumentException();
        }
        uniqueIndex.add(stud.getUuid());
    }

    public void deleteUniqueIndex(Student stud) {


        if (stud == null) {
            throw new IllegalArgumentException();
        }
        uniqueIndex.remove(stud.getUuid());

    }
    public List<Path> listFiles(Path path){
        return null;
    }
    public void loadDatabase(String dir) throws IOException {

    }

}
