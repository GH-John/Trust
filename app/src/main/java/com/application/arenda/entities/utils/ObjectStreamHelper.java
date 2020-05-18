package com.application.arenda.entities.utils;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class ObjectStreamHelper {

    @NotNull
    public synchronized static Map ObjectInputStream(FileInputStream fileInputStream) {
        try (ObjectInputStream input = new ObjectInputStream(fileInputStream)) {
            return (Map) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Timber.tag("ObjectInputStream").d(e);
        }
        return new HashMap<>();
    }

    public synchronized static void ObjectOutputStream(@NotNull final Map map, @NotNull final FileOutputStream fileOutputStream) {
        new Thread(() -> {
            try (ObjectOutputStream output = new ObjectOutputStream(fileOutputStream)) {
                output.writeObject(map);
            } catch (IOException e) {
                Timber.tag("ObjectOutputStream").d(e);
            }
        }).start();
    }
}