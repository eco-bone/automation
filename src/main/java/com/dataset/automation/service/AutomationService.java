package com.dataset.automation.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AutomationService {
    public void createDataset(int n);
    public void exportToCsv(HttpServletResponse response) throws IOException;

}
