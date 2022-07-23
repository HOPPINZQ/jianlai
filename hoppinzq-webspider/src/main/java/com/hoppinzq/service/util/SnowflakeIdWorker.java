package com.hoppinzq.service.util;

import org.springframework.stereotype.Component;

/**
 * 生成雪花ID 该类为百度开源
 */
@Component
public class SnowflakeIdWorker {
    /**
     * 开始时间戳(2020-01-01 00:00:00)
     */
    private long startTimeStamp = 1577808000000L;

    /**
     * 序列号占的位数
     */
    private long sequenceBites = 12;

    /**
     * 机器Id占的位数
     */
    private long machineIdBites = 10;

    /**
     * 时间戳位移位数
     */
    private long timestampOffset  = sequenceBites + machineIdBites;

    /**
     * 序列号
     */
    private volatile long sequence;

    /**
     * 序列号号最大值12位(0b111111111111=0xfff=4095)
     */
    private long maxSequence = ~(-1L << sequenceBites);

    /**
     * 工作机器Id(根据自己业务需要,可以拆成: 5位机房id(dataCenterId) + 5位机器id(workerId))
     */
    private int machineId;

    /**
     * 工作机器Id最大值10位(0d1111111111=0x3ff=1023)
     */
    private int maxMachineId = ~(-1<<machineIdBites);

    /**
     * 上次获取序列号的时间
     */
    private volatile long lastTimestamp;


    public SnowflakeIdWorker() {
        int machineId=12;
        if(machineId < 0 || machineId > maxMachineId){
            throw new IllegalArgumentException(String.format("machine Id can't be greater than %d or less than 0", maxMachineId));
        }
        this.machineId = machineId;
    }

    public SnowflakeIdWorker(int machineId) {
        //判断机器id是否达到上线
        if(machineId < 0 || machineId > maxMachineId){
            throw new IllegalArgumentException(String.format("machine Id can't be greater than %d or less than 0", maxMachineId));
        }
        this.machineId = machineId;
    }
 
    /**
     * 同一台服务器上同步获取序列号id
     * @return
     */
    public synchronized long getSequenceId(){
        long timestamp = System.currentTimeMillis();
        if(lastTimestamp == timestamp){
            //1ms内出现并发，增加序列号
            sequence++;
            //1ms内出现溢出
            if((maxSequence & sequence) == 0){
                //阻塞取下一个毫秒时间
                timestamp = getNextTimeMillis();
            }
        }else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        
        //移位加运算拼到一起组成64位的ID
        return (timestamp - startTimeStamp) << timestampOffset 
                | (machineId << sequenceBites) 
                | sequence;
    }
 
    /**
     * 获取下一个毫秒时间
     * @return
     */
    public long getNextTimeMillis(){
        long timestamp = System.currentTimeMillis();
        //当一毫秒内序列号溢出时，自旋取下一个毫秒
        while (timestamp <= lastTimestamp){
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

}