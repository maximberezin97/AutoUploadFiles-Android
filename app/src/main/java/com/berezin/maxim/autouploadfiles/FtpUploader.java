package com.berezin.maxim.autouploadfiles;

import android.os.Process;

public class FtpUploader implements Runnable {
    private String hostname;
    private int port;
    private String username;
    private String password;

    public FtpUploader() {
        this.hostname = "";
        this.port = -1;
        this.username = "";
        this.password = "";
    }

    public FtpUploader(String hostname, int port, String username, String password) {
        this();
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        System.out.println(hostname+" "+port+" "+username+" "+password);
    }
}
