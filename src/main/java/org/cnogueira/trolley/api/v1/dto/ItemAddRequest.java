package org.cnogueira.trolley.api.v1.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "withName")
public final class ItemAddRequest {
    private final String name;
}
