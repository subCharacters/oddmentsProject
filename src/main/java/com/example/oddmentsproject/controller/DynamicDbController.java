package com.example.oddmentsproject.controller;

import com.example.oddmentsproject.dto.DbConfigRequest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
@RequestMapping("/db")
public class DynamicDbController {
    private HikariDataSource dataSource;

    @PostMapping("/connect")
    public ResponseEntity<String> connect(@RequestBody DbConfigRequest config) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(config.getUrl());
            hikariConfig.setUsername(config.getUsername());
            hikariConfig.setPassword(config.getPassword());
            this.dataSource = new HikariDataSource(hikariConfig);
            return ResponseEntity.ok("DB connected");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed: " + e.getMessage());
        }
    }

    @GetMapping("/time")
    public ResponseEntity<String> getTime() {
        if (dataSource == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not connected to DB");
        }
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CURRENT_TIMESTAMP")) {
            if (rs.next()) {
                return ResponseEntity.ok(rs.getString(1));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No data");
    }

}
