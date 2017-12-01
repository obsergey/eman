package org.osergey.dept.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginationResponse {
    private long total;
    private int pages;
    private int current;

    public PaginationResponse(long count, int page, int size) {
        total = count;
        pages = (int) (count / size) + (count % size == 0 ? 0 : 1);
        current = page;
    }
}
