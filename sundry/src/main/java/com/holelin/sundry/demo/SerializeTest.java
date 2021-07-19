package com.holelin.sundry.demo;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class SerializeTest {

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setName("HoleLin");
        user.setAge("18");

        serialize(user);
        log.info("Java序列化前的结果:{} ", user);

        User deserializeUser = deserialize();
        log.info("Java反序列化的结果:{} ", deserializeUser);
    }

    /**
     * 序列化
     */
    private static void serialize(User user) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("D:\\UserSerialize.txt")));
        oos.writeObject(user);
        oos.close();
    }

    /**
     * 反序列化
     */
    private static User deserialize() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("D:\\UserSerialize.txt")));
        return (User) ois.readObject();
    }
}