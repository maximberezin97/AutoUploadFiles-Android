package com.berezin.maxim.autouploadfiles;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Locale;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;

/**
 * AsyncTask has three genetic parameters, <params, progress, result>.
 * The params are the values needed for the task to begin.
 * Progress is a value representing progress 0%-100%, or -1 if failed.
 * Result indicates some final product of the task.
 * In this case, params are the hostname, port, username, and password.
 * Progress is a Double between 0.0 and 100.0, or -1.
 * Result is a Double for how many seconds the upload took.
 */
public class FtpUploaderTask extends AsyncTask<String, Double, Double> {
    @Override
    protected Double doInBackground(String... strings) {
        String hostname = strings[0];
        int port = Integer.valueOf(strings[1]);
        String username = strings[2];
        String password = strings[3];

        String result = "";
        FTPSClient ftp = getAltFtpsClient(false);
        try {
            ftp.connect(hostname, port);
        } catch(IOException e) {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException f) {}
            }
            result = "Could not connect to server.";
            e.printStackTrace();
        }
        if(!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            result = "FTP server refused connection.";
        } else {
            try {
                if(!ftp.login(username, password)) {
                    result = "Incorrect username/password.";
                } else {
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp.setFileTransferMode(FTP.BINARY_FILE_TYPE);
                    ftp.execPBSZ(0);
                    ftp.execPROT("P");
                    ftp.enterLocalPassiveMode();
                    //ftp.changeWorkingDirectory(uploadPath);
                    //InputStream fileStream;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }


        return -1.0;
    }

    private FTPSClient getAltFtpsClient(boolean isExplicit) {
        return new FTPSClient(isExplicit) {
            @Override
            protected void _prepareDataSocket_(final Socket socket) throws IOException {
                if (socket instanceof SSLSocket) {
                    final SSLSession session = ((SSLSocket) _socket_).getSession();
                    final SSLSessionContext context = session.getSessionContext();
                    try {
                        final Field sessionHostPortCache = context.getClass().getDeclaredField("sessionHostPortCache");
                        sessionHostPortCache.setAccessible(true);
                        final Object cache = sessionHostPortCache.get(context);
                        final Method putMethod = cache.getClass().getDeclaredMethod("put", Object.class, Object.class);
                        putMethod.setAccessible(true);
                        final Method getHostMethod = socket.getClass().getDeclaredMethod("getHost");
                        getHostMethod.setAccessible(true);
                        Object host = getHostMethod.invoke(socket);
                        final String key = String.format("%s:%s", host, String.valueOf(socket.getPort())).toLowerCase(Locale.ROOT);
                        putMethod.invoke(cache, key, session);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
