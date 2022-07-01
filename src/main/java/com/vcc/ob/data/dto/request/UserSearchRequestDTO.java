package com.vcc.ob.data.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;

import java.util.List;

@Data
public class UserSearchRequestDTO {

    @JsonProperty("user_ids")
    private List<String> userIds;
}
