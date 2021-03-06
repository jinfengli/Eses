package com.example.lijinfeng.eses.bean;

/*
 *  TODO: 记录实体类
 *
 *  @author：Jinfeng Lee
 *
 */
public class RecordBean {

    public int _id;

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_id() {
        return _id;
    }
    /** 记录编号 */
    private String RecordNo;
    /** 开始日期 */
    private String startDate;
    /** 开始时间 */
    private String startTime;
    /** 结束日期 */
    private String sleepDate;
    /** 结束时间 */
    private String sleepTime;
    /** 睡眠时间（凌晨后） */
    private String sleepTimeSecond;

    /** 记录类型 */
    private String recordType;


    private String timeDiff;

    /** 记录备注 */
    private String recordComment;
    /** 异常标识 （通宵等）*/
    private String exceptionFlag;

    public RecordBean() {
    }

    public RecordBean(int _id,String recordNo, String startDate, String startTime, String sleepDate, String sleepTime, String sleepTimeSecond, String recordType, String recordComment, String exceptionFlag) {
        this._id = _id;
        this.RecordNo = recordNo;
        this.startDate = startDate;
        this.startTime = startTime;
        this.sleepDate = sleepDate;
        this.sleepTime = sleepTime;
        this.sleepTimeSecond = sleepTimeSecond;
        this.recordType = recordType;
        this.recordComment = recordComment;
        this.exceptionFlag = exceptionFlag;
    }

    public String getRecordNo() {
        return RecordNo;
    }

    public void setRecordNo(String recordNo) {
        RecordNo = recordNo;
    }

    public String getExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(String exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSleepDate() {
        return sleepDate;
    }

    public void setSleepDate(String sleepDate) {
        this.sleepDate = sleepDate;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getSleepTimeSecond() {
        return sleepTimeSecond;
    }

    public void setSleepTimeSecond(String sleepTimeSecond) {
        this.sleepTimeSecond = sleepTimeSecond;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(String timeDiff) {
        this.timeDiff = timeDiff;
    }


    public String getRecordComment() {
        return recordComment;
    }

    public void setRecordComment(String recordComment) {
        this.recordComment = recordComment;
    }

    @Override
    public String toString() {
        return "RecordBean{" +
            "_id=" + _id +
            ", RecordNo='" + RecordNo + '\'' +
            ", startDate='" + startDate + '\'' +
            ", startTime='" + startTime + '\'' +
            ", sleepDate='" + sleepDate + '\'' +
            ", sleepTime='" + sleepTime + '\'' +
            ", sleepTimeSecond='" + sleepTimeSecond + '\'' +
            ", recordType='" + recordType + '\'' +
            ", timeDiff='" + timeDiff + '\'' +
            ", recordComment='" + recordComment + '\'' +
            ", exceptionFlag='" + exceptionFlag + '\'' +
            '}';
    }
}
