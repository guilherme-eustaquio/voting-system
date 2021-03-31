package com.softdesign.business.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedData {
    @NotNull
    private String cpf;
    @NotNull
    private String name;
}
