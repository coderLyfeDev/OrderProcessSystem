package com.order.process.system.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response for UupdateStatus endpoint.
 * Returns the code is used internally to
 * tell if status was updated, has already been completed
 * or if the order doesn't exist.
 *Also returns a message relaying the outcome of the call.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusResponse {
    private int code;
    private String message;
}
