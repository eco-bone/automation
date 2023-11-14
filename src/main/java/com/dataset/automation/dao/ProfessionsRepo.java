package com.dataset.automation.dao;

import com.dataset.automation.model.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionsRepo extends JpaRepository<Profession,Integer> {
}
