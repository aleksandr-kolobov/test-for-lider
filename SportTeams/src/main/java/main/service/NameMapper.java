package main.service;

import org.springframework.stereotype.Service;

@Service
public class NameMapper {
    public String normalName(String name) {
        if (name.isEmpty()) {
            name = "-";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
