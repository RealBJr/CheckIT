package EmailProcessing;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

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
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			Message[] messages = folder.getMessages();
			Message m = messages[messages.length - pos];

			if (m.isMimeType("multipart/alternative") || m.isMimeType("text/*")) {
				Multipart mp = (Multipart) m.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					if (mp.getBodyPart(i).isMimeType("text/plain")) {
						msg += (String) mp.getBodyPart(i).getContent();
					}
				}
			}

			store.close();
		} catch (Exception ex) {
			System.out.println("Oops, got exception! " + ex.getMessage());
			ex.printStackTrace();
			System.exit(1);
		}
		return msg;
	}

}
