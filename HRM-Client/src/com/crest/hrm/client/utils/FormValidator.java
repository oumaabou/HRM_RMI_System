package com.crest.hrm.client.utils;

public class FormValidator {

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

}
