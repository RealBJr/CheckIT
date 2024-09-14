package EmailProcessing;

import java.util.Properties;
import javax.mail.*;

public class ReadMails {
	/**
	 * Read a specified message
	 * 
	 * @param pos,      position within the folder indexed starting from 1, index 1
	 *                  is the most recent email
	 * @param host,     URL of the hosting email server
	 * @param user,     email address of the user
	 * @param password, password of the user
	 * @return the specified message
	 */
	public static String readMsg(int pos, String host, String user, String password) {
		String msg = "";
		try {
			// Get a Properties object
			Properties props = System.getProperties();

			// Change props
			props.put("mail.from", user);
			props.put("mail.store.protocol", "imap");
			props.put("mail.imap.ssl.enable", "true");

			// Get a Session object
			Session session = Session.getInstance(props);
//			session.setDebug(true);

			// Get a Store object
			Store store = session.getStore();
			store.connect(host, user, password);

			// Open the Folder
			Folder folder = store.getFolder("[Gmail]/Important");
			folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();

			Message m = messages.length - pos >= 0 ? messages[messages.length - pos] : null;
			if (m == null) {
				System.out.println("User flag is empty");
			} else if (m.isMimeType("multipart/alternative")) {
				Multipart mp = (Multipart) m.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					if (mp.getBodyPart(i).isMimeType("text/plain")) {
						msg += (String) mp.getBodyPart(i).getContent();
					}
				}
			} else {
				System.out.println("Subject = " + m.getSubject());
			}

			store.close();
		} catch (Exception ex) {
			System.out.println("Oops, got exception! " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		return msg;
	}

	public static Message[] getImportantEmails(String host, String user, String password) {
		Message[] messages = null;
		try {
			// Get a Properties object
			Properties props = System.getProperties();

			// Change props
			props.put("mail.from", user);
			props.put("mail.store.protocol", "imap");
			props.put("mail.imap.ssl.enable", "true");

			// Get a Session object
			Session session = Session.getInstance(props);
//			session.setDebug(true);

			// Get a Store object
			Store store = session.getStore();
			store.connect(host, user, password);

			// Open the Folder
			Folder folder = store.getFolder("[Gmail]/Important");
			folder.open(Folder.READ_ONLY);
			messages = folder.getMessages();

		} catch (Exception ex) {
			System.out.println("Oops, got exception! " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		return messages;
	}

}
