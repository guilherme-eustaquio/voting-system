package com.softdesign.votingsystem.application.util;

import com.softdesign.business.data.AssociatedSessionData;
import com.softdesign.business.data.SessionData;
import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.response.AssociatedSessionResponse;
import com.softdesign.business.response.SessionResponse;

import java.time.LocalDateTime;

public class SessionCreator {
    public static Session createDataSession() {
        Session session = new Session();
        session.setCreatedAt(LocalDateTime.now());
        session.setId("606a4c8b75adf05a9009ae35");
        session.setCreatedAt(LocalDateTime.now());
        session.setTheme("606a4c8b75adf05b9009a334");
        return session;
    }

    public static SessionResponse createDataSessionResponse() {
        SessionResponse sessionResponse = new SessionResponse();
        sessionResponse.setCreatedAt(LocalDateTime.now());
        sessionResponse.setId("606a4c8b75adf05a9009ae35");
        sessionResponse.setCreatedAt(LocalDateTime.now());
        sessionResponse.setTheme("606a4c8b75adf05b9009a334");
        return sessionResponse;
    }

    public static SessionData createDataSessionData() {
        SessionData sessionData = new SessionData();
        sessionData.setTheme("606a4c8b75adf05b9009a334");
        return sessionData;
    }

    public static AssociatedSession createDataAssociatedSession() {
        AssociatedSession associatedSession = new AssociatedSession();
        associatedSession.setCreatedAt(LocalDateTime.now());
        associatedSession.setSession("606a4c8b75adf05a9009ae35");
        associatedSession.setAnswerType("606a4c8b75adf05a9009ae37");
        associatedSession.setAssociated("606a4c8b75adf05a9009ae34");
        return associatedSession;
    }

    public static AssociatedSessionResponse createDataAssociatedSessionResponse() {
        AssociatedSessionResponse associatedSessionResponse = new AssociatedSessionResponse();
        associatedSessionResponse.setCreatedAt(LocalDateTime.now());
        associatedSessionResponse.setSession("606a4c8b75adf05a9009ae35");
        associatedSessionResponse.setAnswerType("606a4c8b75adf05a9009ae37");
        associatedSessionResponse.setAssociated("606a4c8b75adf05a9009ae34");
        return associatedSessionResponse;
    }

    public static AssociatedSessionData createDataAssociatedSessionData() {
        AssociatedSessionData associatedSessionData = new AssociatedSessionData();
        associatedSessionData.setSession("606a4c8b75adf05a9009ae35");
        associatedSessionData.setAnswerType("606a4c8b75adf05a9009ae37");
        associatedSessionData.setAssociated("606a4c8b75adf05a9009ae34");
        return associatedSessionData;
    }
}
