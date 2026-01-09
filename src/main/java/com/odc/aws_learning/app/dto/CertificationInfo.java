package com.odc.aws_learning.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationInfo {
    private Long id;
    private String title;
    private LocalDateTime issuedDate;
}
