package com.application.arenda.UI.ContainerImg;

import android.content.Context;
import android.content.Intent;

import java.util.Map;

public interface AdapterContainer {
    void setActivityResult(final Intent data, final Context context);
    void setContainer(Container container);
    void inflateContainer();

    Map getCurrentMap();
    void removeFromCurrentMap(final Object key);

    int getSize();
}