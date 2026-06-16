package home_accounting.controller;


import home_accounting.dto.response.ReportResponseDTO;
import home_accounting.entity.enums.TransactionType;
import home_accounting.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportResponseDTO> getReport(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            Authentication auth ) {
        return ResponseEntity.ok(
                reportService.getReport(auth.getName(), type, categoryIds, from, to)
        );
    }

}
