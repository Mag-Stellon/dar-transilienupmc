package com.upmc.transilien.v1.endPoint;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.upmc.transilien.v1.model.Train;
import com.upmc.transilien.v1.repository.ItineraireRepository;

/**
 * EndPoint des Itinéraires
 * 
 * @author Kevin Coquart &amp; Mag-Stellon Nadarajah
 *
 */
@Api(name = "itineraire", version = "v1")
public class ItineraireEndPoint {
	/**
	 * Recherche les trains au prochain départ de la gare
	 * 
	 * @param codeUIC
	 *            le codeUIC de la gare
	 * @return les trains qui passent prochainement par la gare
	 * @throws Exception
	 */
	@ApiMethod(name = "prochainDepart", httpMethod = ApiMethod.HttpMethod.GET, path = "prochainDepart")
	public Collection<Train> prochainDepart(@Named("codeUIC") int codeUIC) throws Exception {
		return ItineraireRepository.getInstance().prochainDepart(codeUIC);
	}

	/**
	 * Recherche les trains au prochain départ de la gare et qui passe par la destination.
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 * @return la liste des trains au départ de gare donné et à la destination voulue
	 * @throws Exception
	 */
	@ApiMethod(name = "prochainDepartVers", httpMethod = ApiMethod.HttpMethod.GET, path = "prochainDepartVers")
	public Collection<Train> prochainDepartVers(@Named("departCodeUIC") int departUIC, @Named("destinationCodeUIC") int destinationUIC) throws Exception {
		return ItineraireRepository.getInstance().prochainDepart(departUIC, destinationUIC);
	}

	/**
	 * Recherche un itinéraire entre deux gares, les gares peuvent appartenir à des lignes différentes.
	 * 
	 * @param departUIC
	 *            le codeUIC de la gare de départ
	 * @param destinationUIC
	 *            le codeUIC de la gare de destination
	 * @return la liste des gares à parcourir pour atteindre la destination depuis la gare de départ
	 * @throws Exception
	 */
	@ApiMethod(name = "itineraire", httpMethod = ApiMethod.HttpMethod.GET, path = "itineraire")
	public Map<String, Object> itineraire(@Named("departCodeUIC") int departUIC, @Named("destinationCodeUIC") int destinationUIC) throws Exception {
		return ItineraireRepository.getInstance().itineraire(departUIC, destinationUIC);
	}

	@ApiMethod(name = "envoyerMail", httpMethod = ApiMethod.HttpMethod.GET, path = "envoyerMail")
	public void envoyerMail(/* Map<String, Object> map */) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "...";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("kevin.coquart@etu.upmc.fr", "K.Coquart"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress("kevcoqs@gmail.com", "Kevin"));
			msg.setSubject("[Transilien-UPMC] Récap trajet");
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
