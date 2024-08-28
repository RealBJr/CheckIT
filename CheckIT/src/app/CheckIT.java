package app;

import EmailProcessing.ReadMails;

public class CheckIT {
	public static void main(String[] args) {
		String msg = ReadMails.readMsg(1, "imap.gmail.com", "junibonix770@gmail.com", args[0]);
		System.out.println(msg);
	}
}
