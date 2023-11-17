package main.model.mapper;

import org.springframework.stereotype.Component;

@Component
public class NameMapper {
    public String normalName(String name) {
        if (name.isEmpty()) {
            name = "-";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
