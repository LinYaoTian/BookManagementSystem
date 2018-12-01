package com.rdc.bms.dto;

import com.rdc.bms.base.BaseDTO;
import com.rdc.bms.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchBookDTO extends BaseDTO<List<Book>> {

    private List<Book> data;

    public List<Book> getData() {
        return data == null ? new ArrayList<Book>():data;
    }

    public void setData(List<Book> data) {
        this.data = data;
    }

    @Override
    public List<Book> transform() {
        return getData();
    }
}
