package com.paguemob.utils;

import com.paguemob.exception.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable getPageable(Integer page, Integer size, String sortBy) {
        if (size == null)
            return Pageable.unpaged();
        else if (size < 1)
            throw new BusinessException("pageable.filter.validation.size");
        return PageRequest.of(page, size, Sort.by(sortBy));
    }
}
