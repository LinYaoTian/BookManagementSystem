package com.rdc.bms.dto;

import com.rdc.bms.base.BaseDTO;
import com.rdc.bms.entity.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDTO extends BaseDTO<List<BorrowRecord>> {

    private List<BorrowRecord> data;

    @Override
    public String toString() {
        return "BorrowRecordDTO{" +
                "data=" + getData() +
                ", code=" + getCode() +
                ", msg='" + getMsg() + '\'' +
                '}';
    }

    public List<BorrowRecord> getData() {
        return data == null ? new ArrayList<BorrowRecord>():data;
    }

    public void setData(List<BorrowRecord> data) {
        this.data = data;
    }

    @Override
    public List<BorrowRecord> transform() {
        return getData();
    }
}
