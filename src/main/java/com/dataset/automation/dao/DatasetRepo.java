package com.dataset.automation.dao;

import com.dataset.automation.model.DatasetObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatasetRepo extends JpaRepository<DatasetObject, Integer> {
}
