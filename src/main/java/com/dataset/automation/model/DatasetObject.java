package com.dataset.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import java.sql.Types;

@Entity
@Data
public class DatasetObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long originalId;

    private long paraphraseId;

}
