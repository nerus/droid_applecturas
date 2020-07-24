package com.nerus.apparquos.tasks;

public interface TasksCallBacks {
    void onRequestBeforeStart(String fromTask);
    void onProgressUpdate(String fromTask, Integer... progress);
    void onRequestError(String fromTask, Exception error);
    void onRequestCancel(String fromTask, Exception error);
    void onRequestSuccess(String fromTask, Object response);
}
