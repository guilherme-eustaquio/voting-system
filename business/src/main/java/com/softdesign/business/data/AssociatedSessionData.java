package com.softdesign.business.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedSessionData {

    @NotNull
    private String associated;
    @NotNull
    private String session;
    @NotNull
    private String answerType;
}
