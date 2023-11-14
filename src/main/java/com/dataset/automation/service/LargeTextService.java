package com.dataset.automation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Service
public class LargeTextService {

    private final EntityManager entityManager;

    @Autowired
    public LargeTextService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Long createLargeObject(String text) {
        // Use native query to call lo_from_bytea and return the OID
        String query = "SELECT lo_from_bytea(0, ?)";
        return (Long) entityManager.createNativeQuery(query)
                .setParameter(1, text.getBytes())
                .getSingleResult();
    }

    @Transactional
    public void appendTextToLargeObject(Long oid, String additionalText) {
        // Use native query to call lo_put and append text to the large object
        String query = "SELECT lo_put(?1, lo_lseek(?2, 0, 2), ?3)";
        entityManager.createNativeQuery(query)
                .setParameter(1, oid)
                .setParameter(2, oid)
                .setParameter(3, additionalText.getBytes())
                .executeUpdate();
    }

    @Transactional
    public String getLargeText(Long oid) {
        // Use native query to call lo_get and retrieve the text from the large object
        String query = "SELECT lo_get(?1)";
        byte[] result = (byte[]) entityManager.createNativeQuery(query)
                .setParameter(1, oid)
                .getSingleResult();

        return (result != null) ? new String(result) : null;
    }
}
