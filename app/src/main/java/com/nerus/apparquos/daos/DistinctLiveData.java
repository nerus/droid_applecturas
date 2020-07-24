package com.nerus.apparquos.daos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public abstract class DistinctLiveData<ResultType> {

    private final MediatorLiveData<ResultType> result = new MediatorLiveData<>();

    public DistinctLiveData() {
        final LiveData<ResultType> data = load();
        result.addSource(data, new Observer<ResultType>() {
            boolean initialized = false;
            ResultType lastObj;

            @Override
            public void onChanged(ResultType obj) {
                if (!initialized) {
                    initialized = true;
                    lastObj = obj;
                    result.postValue(lastObj);
                } else if ((obj == null && lastObj != null) ||
                        !DistinctLiveData.this.equals(obj, lastObj)) {
                    lastObj = obj;
                    result.postValue(lastObj);
                }
            }
        });
    }

    protected boolean equals(ResultType newObj, ResultType lastObj) { return newObj == lastObj; }

    protected abstract LiveData<ResultType> load();

    public LiveData<ResultType> asLiveData() { return result; }
}