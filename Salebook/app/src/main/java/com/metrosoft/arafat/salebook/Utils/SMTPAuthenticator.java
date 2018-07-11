package com.metrosoft.arafat.salebook.Utils;

/**
 * Created by hp on 11/16/2017.
 */

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/*
* not that for authenticatin purpose go to
* https://myaccount.google.com/lesssecureapps
* and turn this ON for enable sending
* */
import static com.metrosoft.arafat.salebook.app.AppConfig.email;
import static com.metrosoft.arafat.salebook.app.AppConfig.emial_pass;

public class SMTPAuthenticator extends Authenticator {
    public SMTPAuthenticator() {

        super();
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        String username = email;
        String password = emial_pass;
        if ((username != null) && (username.length() > 0) && (password != null)
                && (password.length() > 0)) {

            return new PasswordAuthentication(username, password);
        }

        return null;
    }
}