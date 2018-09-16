package com.example.veloxigami.btp;

/**
 * Created by Anil on 21-08-2018.
 */

public class OutputObject {
    private int sizeOfTask, batteryLevel;
    private long numberOfProcesses, timeInvested, networkLatency;
    private double usageRAM, totalRAM;
    private int option;

    public OutputObject(int sizeOfTask, long numberOfProcesses, long timeInvested, long networkLatency, double usageRAM, double totalRAM, int batteryLevel,int option) {
        this.sizeOfTask = sizeOfTask;
        this.numberOfProcesses = numberOfProcesses;
        this.timeInvested = timeInvested;
        this.networkLatency = networkLatency;
        this.usageRAM = usageRAM;
        this.totalRAM = totalRAM;
        this.batteryLevel = batteryLevel;
        this.option = option;
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

    public long getTimeInvested() {
        return timeInvested;
    }

    public void setTimeInvested(long timeInvested) {
        this.timeInvested = timeInvested;
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
