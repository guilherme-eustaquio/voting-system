package com.softdesign.votingsystem.application.util;

import com.softdesign.business.data.AssociatedData;
import com.softdesign.business.domain.Associated;
import com.softdesign.business.response.AssociatedResponse;

import java.time.LocalDateTime;

public class AssociatedCreator {
    public static Associated createDataAssociated() {
        Associated associated = new Associated();
        associated.setCreatedAt(LocalDateTime.now());
        associated.setId("606a4c8b75adf05a9009ae37");
        associated.setCreatedAt(LocalDateTime.now());
        associated.setCpf("23009378084");
        return associated;
    }

    public static AssociatedResponse createDataAssociatedResponse() {
        AssociatedResponse associatedResponse = new AssociatedResponse();
        associatedResponse.setCreatedAt(LocalDateTime.now());
        associatedResponse.setId("606a4c8b75adf05a9009ae37");
        associatedResponse.setCreatedAt(LocalDateTime.now());
        associatedResponse.setCpf("23009378084");
        return associatedResponse;
    }

    public static AssociatedData createDataAssociatedData() {
        AssociatedData associatedData = new AssociatedData();
        associatedData.setCpf("23009378084");
        return associatedData;
    }
}
