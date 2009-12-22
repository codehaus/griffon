import groovy.xml.MarkupBuilder;

import javax.mail.internet.MimeMultipart;

import java.util.Properties

import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage

/**
 * Sends email from a Griffon application.
 */
class MailService {
	
	/**
	 * Send an email using the arguments specified.  The following arguments are recognized:
	 *	<ul>
	 *		<li>transport - either 'smtp' for regular SMTP or 'smtps' for SMTP with SSL.  Defaults to 'smtp'</li>
	 * 		<li>mailhost  - (required) the address of the SMTP server, e.g. 'smtp.google.com'</li>
	 * 		<li>port      - the port of the SMTP server.  Defaults appropriately for the transport specified.</li>
	 * 		<li>auth      - true if authentication is required, false otherwise.  Defaults to false (no auth).</li>
	 *      <li>user      - the username for authenticating with the SMTP server.</li>
	 *      <li>password  - the password for authenticating with the SMTP server.</li>
	 * 		<li>from      - the message sender, e.g. 'foo@bar.com'</li>
	 * 		<li>to        - (required) the message recipients, e.g. 'foo@bar.com'.  Multiple addresses may be specified as a comma-separated list.</li>
	 * 		<li>cc        - the CC recipients, e.g. 'foo@bar.com'.  Multiple addresses may be specified as a comma-separated list.</li>
	 * 		<li>bcc       - the BCC recipients, e.g. 'foo@bar.com'.  Multiple addresses may be specified as a comma-separated list.</li>
	 * 		<li>subject   - the message subject.</li>
	 * 		<li>text      - the plain text message content.</li>
	 *		<li>html      - the html message content.</li>
	 *      <li>attachments - the list of file paths to attach to the message.</li>
	 * 	</ul>
	 * 
	 * Future versions of this service will support HTML and file attachments.
	 */
	static void sendMail(Map args) {
		if (!args.mailhost)		throw new RuntimeException('No mail host specified')
		if (!args.to) throw new RuntimeException('No recipient specified')
		
		// default to smtp if no explicit transport set
		String transport = args.transport ?: 'smtp'
		
		// set system properties
		Properties props = System.getProperties()
		if (args.mailhost)	props.put("mail.${transport}.host", args.mailhost)
		if (args.port)		props.put("mail.${transport}.port", args.port)
		if (args.auth)		props.put("mail.${transport}.auth", "true")

		// build our message
		Session session = Session.getInstance(props, null)
		Message message = new MimeMessage(session)
		if (args.from) {
			message.setFrom(new InternetAddress(args.from))
		} else {
			message.setFrom()
		}
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(args.to, false))
		if (args.cc)	message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(args.cc, false))
		if (args.bcc)	message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(args.bcc, false))
		message.subject = args.subject ?: ''
		
		// sets the message content
		def content = new MimeMultipart()
		if (args.text) {
			// handle text
			def textPart = new MimeBodyPart()
			textPart.setContent(args.text.toString(), 'text/plain')
			content.addBodyPart(textPart)
		}
		if (args.html) {
			// handle html
			def htmlPart = new MimeBodyPart()
			htmlPart.setContent(args.html.toString(), 'text/html')
			content.addBodyPart(htmlPart)
		}
		if (args.attachments) {
			// add all attachments
			args.attachments.each { a ->
				def attachmentPart = new MimeBodyPart()
				def src = new FileDataSource(a)
				attachmentPart.dataHandler = new DataHandler(src)
				attachmentPart.fileName = new File(a).name
				content.addBodyPart(attachmentPart)
			}
		}
		message.content = content
		
		message.setHeader("X-Mailer", args.mailer ?: "Griffon Mail Service")
		message.sentDate = new Date()
		
		// send the mail
		def t = session.getTransport(transport)
		if (args.auth) {
			t.connect(args.mailhost, args.user, args.password)
		} else {
			t.connect()
		}
		t.sendMessage(message, message.getAllRecipients())
	}
}