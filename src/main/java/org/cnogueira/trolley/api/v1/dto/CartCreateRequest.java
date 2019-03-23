package org.cnogueira.trolley.api.v1.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public final class CartCreateRequest {
    private final String name;
}
