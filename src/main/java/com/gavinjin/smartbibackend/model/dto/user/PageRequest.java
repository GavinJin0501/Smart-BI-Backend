package com.gavinjin.smartbibackend.model.dto.user;

import com.gavinjin.smartbibackend.util.constant.CommonConstant;
import lombok.Data;

/**
 * Page request
 *
 */
@Data
public class PageRequest {

    /**
     * Current page number
     */
    private long currentPageNumber = 1;

    /**
     * Page size
     */
    private long pageSize = 10;

    /**
     * Field to sort
     */
    private String sortField;

    /**
     * Sort order (asc by default)
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}