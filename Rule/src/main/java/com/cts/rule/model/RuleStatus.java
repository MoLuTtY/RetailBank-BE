package com.cts.rule.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public enum RuleStatus {
    ALLOWED,
    DENIED
}

