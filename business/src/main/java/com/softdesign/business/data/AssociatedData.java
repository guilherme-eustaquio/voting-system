package com.softdesign.business.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedData {
    @NonNull
    private String cpf;
    @NonNull
    private String name;
}
