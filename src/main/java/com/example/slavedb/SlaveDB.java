package com.example.slavedb;

import com.example.slavedb.schema.Student;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SlaveDB extends Slave {
    private static Logger logger = LogManager.getLogger(SlaveDB.class);

    public SlaveDB(String slaveDir) {
        super(slaveDir);
    }


    public List<Path> listFiles(Path path) {

        List<Path> result = null;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void loadDatabase(String dir) throws IOException {

        dir = this.getSlaveDir() + Slave.COLLECTION_DIR;

        logger.info("directory to read from "+dir);

        TreeSet<Integer> tempTree = this.getUniqueIndex();
        TreeMap<String, List<String>> tempMap = this.getPropertyIndex();

        logger.info(tempTree.size());
        logger.info(tempMap.entrySet().size());

        if (this.getPropertyIndex().size() == 0 || this.getUniqueIndex().size() == 0) {
            if (Files.exists(Path.of(dir))) {
                logger.info("directory exists");
                List<Path> files = listFiles(Path.of(dir));
                files.forEach(s -> {
                    Gson json = new Gson();
                    try (Reader reader = new FileReader(String.valueOf(s))) {
                        Student student = json.fromJson(reader, Student.class);

                        addPropertyIndex(student);
                        addUniqueIndex(student);
                        logger.info(getPropertyIndex().size());
                        logger.info(getUniqueIndex().size());

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            else {
                logger.info("There is any directory for database");
            }
        }

    }

}
