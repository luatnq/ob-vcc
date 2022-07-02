package com.vcc.ob.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMoneyRequestDTO {
    @JsonProperty("from_user_id")
    private String fromUserId;

    @JsonProperty("to_user_id")
    private String toUserId;

    @JsonProperty("money")
    private long money;
}
