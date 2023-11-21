package com.dataset.automation.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;

public interface AutomationService {
    public void createDataset4(int n);
    public void exportToCsv(HttpServletResponseWrapper response) throws IOException;

}
