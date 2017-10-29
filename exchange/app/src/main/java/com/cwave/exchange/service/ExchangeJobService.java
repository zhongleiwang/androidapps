package com.cwave.exchange.service;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ExchangeJobService extends JobService {

    private static final String TAG = "ExchangeJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
