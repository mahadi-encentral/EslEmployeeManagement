package com.encentral.scaffold.commons;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.regex.Pattern;

public class ControllerUtils {
    public static String getJSONStringField(JsonNode node, String field) {
        JsonNode data = node.get(field);
        if (data == null)
            return null;

        return data.asText();
    }

    /**
     * Validates an email address
     * @param email the email to validate
     * @return true if the email is valid. false otherwise
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
