package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.model.PaginationResponse;

@Data
@NoArgsConstructor
public class AggPaginationResponse {
    long total;
    int pages;
    int current;

    public AggPaginationResponse(PaginationResponse pagination) {
        total = pagination.getTotal();
        pages = pagination.getPages();
        current = pagination.getCurrent();
    }
}
