package com.softdesign.business.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedSessionData {
    @NonNull
    private String associated;
    @NonNull
    private String session;
    @NonNull
    private String answerType;
}
