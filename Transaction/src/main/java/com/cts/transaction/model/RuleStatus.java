package com.cts.transaction.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public enum RuleStatus {
    ALLOWED,
    DENIED
}

