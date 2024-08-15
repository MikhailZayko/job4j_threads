package ru.job4j.thread;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Wget implements Runnable {

    private final String url;

    private final int speed;

    private final String out;

    public Wget(String url, int speed, String out) {
        this.url = url;
        this.speed = speed;
        this.out = out;
    }

    @Override
    public void run() {
        try (InputStream input = new URL(url).openStream();
             FileOutputStream output = new FileOutputStream(out)) {
            byte[] dataBuffer = new byte[speed];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer)) != -1) {
                long downloadAt = System.currentTimeMillis();
                output.write(dataBuffer, 0, bytesRead);
                long downloadTime = System.currentTimeMillis() - downloadAt;
                if (downloadTime < 1000) {
                    Thread.sleep(1000 - downloadTime);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void validateArgs(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("There must be 3 arguments");
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String outName = args[2];
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("Please enter the correct URL");
        }
        if (speed < 0 || speed > 2000000) {
            throw new IllegalArgumentException("Download speed must be greater than 0 and less than 2000000 byte/sec");
        }
        if (!outName.contains(".")) {
            throw new IllegalArgumentException("Please enter a valid file name");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        validateArgs(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        String out = args[2];
        Thread wget = new Thread(new Wget(url, speed, out));
        wget.start();
        wget.join();
    }
}
