package com.softdesign.business.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedSessionData {
    private String associated;
    private String session;
    private String answerType;
}
