package org.example.projectmanagerapp.priority;

import org.example.projectmanagerapp.priority.PriorityLevel;

// Klasa implementująca niski poziom priorytetu dla zadań
public class LowPriority implements PriorityLevel {
    @Override
    public String getPriority() {
        return "LOW";
    }
}