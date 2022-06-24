package edu.skku.cs.personalproject;

public class weather_res {
    private response response;

    public response getResponse() {
        return response;
    }

    public void setResponse(response response) {
        this.response = response;
    }
}

class response{
    private header header;
    private body body;

    public header getHeader() {
        return header;
    }

    public void setHeader(header header) {
        this.header = header;
    }

    public body getBody() {
        return body;
    }

    public void setBody(body body) {
        this.body = body;
    }
}

class header {
    public String resultCode;
    public String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}

class body {
    public String dataType;
    public items items;
    public int pageNo;
    public int numOfRows;
    public int totalCount;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public edu.skku.cs.personalproject.items getItems() {
        return items;
    }

    public void setItems(edu.skku.cs.personalproject.items items) {
        this.items = items;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}

class items{
    public item[] item;

    public item[] getItem() {
        return item;
    }

    public void setItem(item[] item) {
        this.item = item;
    }
}
class item{
    public String baseDate;
    public String baseTime;
    public String category;
    public String fcstDate;
    public String fcstTime;
    public String fcstValue;
    public int nx;
    public int ny;

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    public String getFcstValue() {
        return fcstValue;
    }

    public void setFcstValue(String fcstValue) {
        this.fcstValue = fcstValue;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }
}