package com.application.arenda.ServerInteraction.Cookies;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ObjectStreamHelper {

    @NotNull
    public synchronized static Map ObjectInputStream(FileInputStream fileInputStream) {
        try (ObjectInputStream input = new ObjectInputStream(fileInputStream)) {
            return (Map) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("ObjectInputStream", e.getMessage());
        }
        return new HashMap<>();
    }

    public synchronized static void ObjectOutputStream(@NotNull final Map map, @NotNull final FileOutputStream fileOutputStream) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (ObjectOutputStream output = new ObjectOutputStream(fileOutputStream)) {
                    output.writeObject(map);
                } catch (IOException e) {
                    Log.d("ObjectOutputStream", e.getMessage());
                }
            }
        }).start();
    }
}