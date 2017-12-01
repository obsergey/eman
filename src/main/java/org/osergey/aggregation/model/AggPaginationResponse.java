package org.osergey.aggregation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.osergey.dept.model.PaginationResponse;

@Data
@NoArgsConstructor
public class AggPaginationResponse {
    private long total;
    private int pages;
    private int current;

    public AggPaginationResponse(PaginationResponse pagination) {
        total = pagination.getTotal();
        pages = pagination.getPages();
        current = pagination.getCurrent();
    }
}
