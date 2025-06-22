package com.example.smartlab.Models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static List<Analyzes> analyses = new ArrayList<>();

    public static void addAnalysis(Analyzes analysis) {
        analyses.add(analysis);
    }

    public static List<Analyzes> getAnalyses() {
        return new ArrayList<>(analyses);
    }

    public static void clearCart() {
        analyses.clear();
    }
}