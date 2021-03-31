package com.softdesign.business.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionData {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-3")
    private LocalDateTime time;

    @NotNull
    private String theme;
}
