package xingu.email;

public interface EmailManager
{
    void sendMessage(Email email)
        throws Exception;

    boolean isEmailSent(String type, String to);
}
