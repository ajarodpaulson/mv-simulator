package com.mvsim.model.observer;

import java.util.Map.Entry;
import java.util.Set;

import com.mvsim.model.ventilator.metrics.Metric;
import com.mvsim.model.ventilator.metrics.MetricName;
import com.mvsim.model.ventilator.metrics.Metrics;
import com.mvsim.model.ventilator.metrics.MostRecentTickData;

public interface SimMgrObserver {
     /**
     * This method should be called within Observable.notifyObservers()
     * @param data
     */
     void update(Metrics metrics);
}
