package com.allclearlecture.domain.fileupload.event;

import java.util.concurrent.ExecutionException;

public interface EventHandler {
    void onMessage(MessageEvent messageEvent) throws InterruptedException, ExecutionException;
}
