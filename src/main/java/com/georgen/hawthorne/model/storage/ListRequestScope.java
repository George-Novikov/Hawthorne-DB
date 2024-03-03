package com.georgen.hawthorne.model.storage;

public class ListRequestScope {
    private int startPartition;
    private int startPartitionCount;
    private int numberOfMiddlePartitions;
    private int sizeOfMiddlePartitions;
    private int endPartition;
    private int endPartitionCount;

    public ListRequestScope() {}

    public ListRequestScope(int startPartition,
                            int startPartitionCount,
                            int numberOfMiddlePartitions,
                            int sizeOfMiddlePartitions,
                            int endPartitionNumber,
                            int endPartition) {
        this.startPartition = startPartition;
        this.startPartitionCount = startPartitionCount;
        this.numberOfMiddlePartitions = numberOfMiddlePartitions;
        this.sizeOfMiddlePartitions = sizeOfMiddlePartitions;
        this.endPartition = endPartitionNumber;
        this.endPartitionCount = endPartition;
    }

    public int getStartPartition() {
        return startPartition;
    }

    public void setStartPartition(int startPartition) {
        this.startPartition = startPartition;
    }

    public int getStartPartitionCount() {
        return startPartitionCount;
    }

    public void setStartPartitionCount(int startPartitionCount) {
        this.startPartitionCount = startPartitionCount;
    }

    public int getNumberOfMiddlePartitions() {
        return numberOfMiddlePartitions;
    }

    public void setNumberOfMiddlePartitions(int numberOfMiddlePartitions) {
        this.numberOfMiddlePartitions = numberOfMiddlePartitions;
    }

    public int getSizeOfMiddlePartitions() {
        return sizeOfMiddlePartitions;
    }

    public void setSizeOfMiddlePartitions(int sizeOfMiddlePartitions) {
        this.sizeOfMiddlePartitions = sizeOfMiddlePartitions;
    }

    public int getEndPartition() {
        return endPartition;
    }

    public void setEndPartition(int endPartition) {
        this.endPartition = endPartition;
    }

    public int getEndPartitionCount() {
        return endPartitionCount;
    }

    public void setEndPartitionCount(int endPartitionCount) {
        this.endPartitionCount = endPartitionCount;
    }
}
