package com.odc.aws_learning.app.constante;

public enum CourseStatus {
    BROUILLON, // Draft, not visible to learners
    IN_REVIEW, // Submitted for validation, awaiting admin approval
    PUBLIE,    // Published and visible to learners
    ARCHIVE    // Archived, not visible in main lists but accessible via direct link or for enrolled users
}
