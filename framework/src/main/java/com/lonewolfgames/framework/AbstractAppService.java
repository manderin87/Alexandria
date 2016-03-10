package com.lonewolfgames.framework;

/**
 * Created by jhyde on 11/9/2015.
 * Copyright (C) 2015 Jesse Hyde Lone Wolf Games
 */

public abstract class AbstractAppService {

    public static enum ServiceError {
        Success(""),
        NetworkConnectionError("Please check your network connection and try again.");

        private String mErrorMessage;

        private ServiceError(String message) {
            mErrorMessage = message;
        }

        public String errorMessage() { return mErrorMessage; }
    }
}
