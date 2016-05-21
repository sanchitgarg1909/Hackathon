package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "org.example.androidsdk.hackathon.model");
        Entity friend = schema.addEntity("Friend");
//        friend.addIdProperty().primaryKey().autoincrement();
        friend.addStringProperty("name");
        friend.addStringProperty("fbId");
        friend.addBooleanProperty("isAdded");
        new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");
    }
}
