package peacemaker.oneplayer.listener;

/**
 * Created by peace on 2018/5/16.
 */

public class SocketThread extends Thread{
    private Boolean isStop = false;
    public Boolean isReciving = true;
    public Boolean isInitSuccess = false;
    private SocketThreadListener socketThreadListener;
    public void setSocketThreadListener(SocketThreadListener socketThreadListener){
        this.socketThreadListener = socketThreadListener;
    }
    public void stopThread(){
        isStop = true;
    }
    @Override
    public void run() {
        super.run();
        if(socketThreadListener!=null) {
            socketThreadListener.onInit();
            if (isInitSuccess) {
                while (!isStop) {
                    socketThreadListener.onCycling();
                }
            }
        }
    }
}
