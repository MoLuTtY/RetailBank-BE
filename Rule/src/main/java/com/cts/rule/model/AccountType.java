package com.cts.rule.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public enum AccountType {
    SAVINGS,
    CURRENT
}
