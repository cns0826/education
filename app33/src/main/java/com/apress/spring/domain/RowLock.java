package com.apress.spring.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class RowLock{

    @Id
    private Long id;

    public RowLock(Long id )
            throws ParseException {
        this.id = id;
    }

    RowLock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        StringBuilder value = new StringBuilder("RowLockEntry(");
        value.append("id: ");
        value.append(id);
        return value.toString();
    }
}
