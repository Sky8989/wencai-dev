package com.sftc.web.mapper;

import com.sftc.web.model.EditOrder;


public interface EditOrderMapper {
    void editOrder(EditOrder editOrder);

    EditOrder selectOrderById(int id);
}
