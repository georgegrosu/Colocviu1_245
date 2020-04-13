package ro.pub.cs.systems.eim.colocviu1_245.general;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.sql.Date;

public class ProcessingThread extends Thread {
    private Context context = null;
    private boolean isRunning = true;
    private int sum;

    public ProcessingThread(Context context, int sum) {
        this.context = context;
        this.sum = sum;
    }

    @Override
    public void run() {
        while (isRunning) {
            sleep();
            sendMessage();
        }
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction("test");
        intent.putExtra(Constants.BROADCAST_RECEIVER_EXTRA,
                new Date(System.currentTimeMillis()) + " " + sum);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
