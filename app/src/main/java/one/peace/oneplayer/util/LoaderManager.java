package one.peace.oneplayer.util;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

public class LoaderManager {
    private Class clazz;
    protected OneTimeWorkRequest mOneTimeWorkRequest;

    public LoaderManager(Class clazz) {
        this.clazz = clazz;
    }

    public void startLoading() {
        mOneTimeWorkRequest = new OneTimeWorkRequest.Builder(clazz).build();
        WorkManager.getInstance().enqueue(mOneTimeWorkRequest);
    }

    public void stopLoading() {
        if (mOneTimeWorkRequest != null) {
            WorkManager.getInstance().cancelWorkById(mOneTimeWorkRequest.getId());
        }
    }
}
