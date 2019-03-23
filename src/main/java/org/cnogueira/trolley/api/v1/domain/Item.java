package org.cnogueira.trolley.api.v1.domain;

import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class Item {
    private final UUID id;
    private final String name;
}
