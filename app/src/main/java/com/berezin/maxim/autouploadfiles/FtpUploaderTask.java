package com.berezin.maxim.autouploadfiles;

import android.os.AsyncTask;
import android.os.Environment;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
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
        //TODO: Either create a way to explore FTP server directories, or create a field to manually enter upload path.
        String uploadPath = ""; //Temporarily empty until a file explorer is implemented.
        List<File> files = null;
        File currentFile;
        double timeStart = 0.0;
        double timeEnd = 0.0;
        boolean fileStored = false;

        String result = "";
        FTPSClient ftp = getAltFtpsClient(false);

        //TODO: Print FTP command console somewhere using FTP.ProtocolCommandListener.
        //PrintStream printStream = newPrintStream();
        //autoUploadFiles.redirectOutput(printStream);
        //ftp.addProtocolCommandListener(new PrintCommandListener(printStream, true));
        ftp.setCopyStreamListener(new CopyStreamAdapter() {
            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                publishProgress(Long.valueOf(bytesTransferred).doubleValue(), Integer.valueOf(bytesTransferred).doubleValue());
            }
        });

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
                    ftp.changeWorkingDirectory(uploadPath);
                    InputStream fileStream;
                    timeStart = System.currentTimeMillis();
                    for(Iterator<File> it = files.iterator(); it.hasNext();) {
                        currentFile = it.next();
                        if(!isCancelled()) {
                            fileStream = new FileInputStream(currentFile);
                            fileStored = ftp.storeFile(currentFile.getName(), fileStream);
                            fileStream.close();
                        }
                        if(!fileStored) break;
                    }
                    timeEnd = System.currentTimeMillis();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        //TODO: Determine a way to show results, considering success timelapse or the different ways it can fail.
        return timeEnd-timeStart;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        double totalBytesTransferred = values[0];
        double bytesTransferred = values[1];
        System.out.println("totalBytesTransferred = "+totalBytesTransferred+" bytesTransferred = "+bytesTransferred);
        //TODO: Make a progress bar and update it with total transfer and current transfer values.

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
