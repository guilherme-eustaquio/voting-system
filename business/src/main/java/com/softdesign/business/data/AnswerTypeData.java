package com.softdesign.business.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AnswerTypeData {
    @NotNull
    private String answer;
}
