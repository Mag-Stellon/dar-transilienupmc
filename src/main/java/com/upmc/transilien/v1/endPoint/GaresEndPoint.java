package com.upmc.transilien.v1.endPoint;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Text;
import com.upmc.transilien.parse.JsonToObject;
import com.upmc.transilien.v1.model.Gare;
import com.upmc.transilien.v1.repository.GareRepository;

/**
 * EndPoint des Gares
 * 
 * @author Kevin Coquart && Mag-Stellon Nadarajah
 *
 */
@Api(name = "gares", version = "v1")
public class GaresEndPoint {
	/**
	 * @return toutes les gares existantes
	 */
	@ApiMethod(name = "getGares", httpMethod = ApiMethod.HttpMethod.GET, path = "list")
	public Collection<Gare> getGares() {
		return GareRepository.getInstance().findGares();
	}

	/**
	 * Recherche une gare par son codeUIC
	 * 
	 * @param codeUIC
	 *            le codeUIC d'une gare
	 * @return la gare ou null
	 */
	@ApiMethod(name = "getGareByCode", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByCode")
	public Gare getGareByCode(@Named("codeUIC") Integer codeUIC) {
		return GareRepository.getInstance().findGareByCode(codeUIC);
	}

	/**
	 * Recherche une gare par son nom
	 * 
	 * @param nom
	 *            le nom de la gare
	 * @return la gare ou null
	 */
	@ApiMethod(name = "getGareByName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareByName")
	public Gare getGareByName(@Named("nom") String nom) {
		return GareRepository.getInstance().findGareByName(nom);
	}

	/**
	 * @return le nom de toutes les gares disponibles
	 */
	@ApiMethod(name = "getGareName", httpMethod = ApiMethod.HttpMethod.GET, path = "getGareName")
	public List<String> getGareName() {
		return GareRepository.getInstance().findGaresName();
	}

	// @ApiMethod(name = "create", httpMethod = ApiMethod.HttpMethod.POST, path = "create")
	// public Gare create(Gare gare) {
	// return GareRepository.getInstance().create(gare);
	// }

	/**
	 * Charge les gares depuis un fichier statique présent sur le serveur.<br>
	 * La fonction est voué à disparaître pour fonctionner de manière transparente à l'initialisation du serveur.
	 * 
	 * @return OK si cela c'est bien passé, le message d'erreur sinon
	 */
	@ApiMethod(name = "loadGare", httpMethod = ApiMethod.HttpMethod.POST, path = "loadGare")
	public Text loadGare() {
		try {
			JsonToObject.loadGare("ressources/sncf-gares-et-arrets-transilien-ile-de-france.json");
			return new Text("OK");
		} catch (IOException | ParseException e) {
			return new Text(System.getProperties().get("user.dir") + "\n" + e.getMessage());
		}
	}

	// TODO a voir si on modifie les gares
	// @ApiMethod(name = "update", httpMethod = ApiMethod.HttpMethod.PUT, path =
	// "update")
	// public Gare update(Gare editedGare) {
	// return Gares.getInstance().update(editedGare);
	// }

	// @ApiMethod(name = "remove", httpMethod = ApiMethod.HttpMethod.DELETE, path = "remove")
	// public void remove(@Named("id") Long id) {
	// GareRepository.getInstance().remove(id);
	// }
}