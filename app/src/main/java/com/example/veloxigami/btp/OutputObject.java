package com.example.veloxigami.btp;

/**
 * Created by Anil on 21-08-2018.
 */

public class OutputObject {
    private int sizeOfTask, batteryLevel;
    private long numberOfProcesses, offlineTime, offloadTime, timeOnServer, networkLatency;
    private double usageRAM, totalRAM;
    private int option;

    public OutputObject(int sizeOfTask, int batteryLevel, long numberOfProcesses, long offlineTime, long offloadTime, long networkLatency, double usageRAM, double totalRAM, int option) {
        this.sizeOfTask = sizeOfTask;
        this.batteryLevel = batteryLevel;
        this.numberOfProcesses = numberOfProcesses;
        this.offlineTime = offlineTime;
        this.offloadTime = offloadTime;
        this.timeOnServer = timeOnServer;
        this.networkLatency = networkLatency;
        this.usageRAM = usageRAM;
        this.totalRAM = totalRAM;
        this.option = option;
    }

    public long getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(long offlineTime) {
        this.offlineTime = offlineTime;
    }

    public long getOffloadTime() {
        return offloadTime;
    }

    public void setOffloadTime(long offloadTime) {
        this.offloadTime = offloadTime;
    }

    public long getTimeOnServer() {
        return timeOnServer;
    }

    public void setTimeOnServer(long timeOnServer) {
        this.timeOnServer = timeOnServer;
    }

    public int getSizeOfTask() {
        return sizeOfTask;
    }

    public void setSizeOfTask(int sizeOfTask) {
        this.sizeOfTask = sizeOfTask;
    }

    public long getNumberOfProcesses() {
        return numberOfProcesses;
    }

    public void setNumberOfProcesses(long numberOfProcesses) {
        this.numberOfProcesses = numberOfProcesses;
    }

    public long getNetworkLatency() {
        return networkLatency;
    }

    public void setNetworkLatency(long networkLatency) {
        this.networkLatency = networkLatency;
    }

    public double getUsageRAM() {
        return usageRAM;
    }

    public void setUsageRAM(double usageRAM) {
        this.usageRAM = usageRAM;
    }

    public double getTotalRAM() {
        return totalRAM;
    }

    public void setTotalRAM(double totalRAM) {
        this.totalRAM = totalRAM;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

      public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}
