package com.allclearlecture.domain.fileupload.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FileEventSource implements  Runnable{
    public static final Logger logger = LoggerFactory.getLogger(FileEventSource.class.getName());

    public boolean keepRunning = true;
    private int updateInterval;
    //무슨파일을 모니터링할거냐
    private File file;

    //파일을어디까지 읽었냐
    private long filePointer = 0;

    // 파일의 이전 내용을 저장하는 리스트
    private List<String> previousLines = new ArrayList<>();
    private EventHandler eventHandler;

    public FileEventSource(int updateInterval, File file, EventHandler eventHandler) {
        this.updateInterval = updateInterval;
        this.file = file;
        this.eventHandler = eventHandler;
    }

    @Override
    public void run() {
        try {
            while (this.keepRunning) {
                Thread.sleep(this.updateInterval); // 업데이트 주기 설정

                long len = this.file.length();

                if (len < this.filePointer) {
                    logger.info("log file was reset as filePointer is longer than len");
                    filePointer = 0; // 파일 포인터를 0으로 설정하여 파일 전체를 다시 읽음
                } else if (len != this.filePointer) { // 파일이 수정된 경우
                    readAppendAndSend();
                } else {
                    // 변화가 없을 경우
                    continue;
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void readAppendAndSend() throws IOException, ExecutionException, InterruptedException {
        List<String> currentLines = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                currentLines.add(line);
            }
        }

        // 비교하여 변경된 부분만 처리
        int minLength = Math.min(previousLines.size(), currentLines.size());

        for (int i = 0; i < minLength; i++) {
            if (!Objects.equals(previousLines.get(i), currentLines.get(i))) {
                sendMessage(currentLines.get(i));
            }
        }

        // 새롭게 추가된 라인 처리
        if (currentLines.size() > previousLines.size()) {
            for (int i = previousLines.size(); i < currentLines.size(); i++) {
                sendMessage(currentLines.get(i));
            }
        }

        // 이전 라인 리스트 업데이트
        previousLines = currentLines;
    }


    private void sendMessage(String line) throws ExecutionException, InterruptedException {
        // Split the line by commas
        String[] tokens = line.split(",");

        //키 추출
        String key = tokens[0].trim();


        StringBuffer value = new StringBuffer();
        for (int i = 1; i < tokens.length; i++) {
            if (i != (tokens.length - 1)) {
                value.append(tokens[i].trim()).append(",");
            } else {
                value.append(tokens[i].trim());
            }
        }

        //MessageEvent 객체 생성
        MessageEvent messageEvent = new MessageEvent(key, value.toString());

        this.eventHandler.onMessage(messageEvent);
    }

}
