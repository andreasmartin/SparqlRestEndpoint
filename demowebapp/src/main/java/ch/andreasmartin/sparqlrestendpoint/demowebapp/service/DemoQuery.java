/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.andreasmartin.sparqlrestendpoint.demowebapp.service;

import ch.andreasmartin.sparqlrestendpoint.core.query.SPARQLQuery;
import ch.andreasmartin.sparqlrestendpoint.demowebapp.domain.PersonEntity;
import ch.andreasmartin.sparqlrestendpoint.demowebapp.ejb.PersonEJB;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author andreas.martin
 * 
 *  SELECT ?label
 *  WHERE {
 *      SERVICE <http://localhost:8080/demowebapp/webresources/DemoQuery>
 *      {
 *      	<http://somewhere/JohnSmith> rdfs:label ?label .
 *      }
 *   }
 */
@Path("DemoQuery")
@Stateless
public class DemoQuery {
    
    @EJB
    PersonEJB personEJB;

    @GET
    @Produces("application/sparql-results+xml, application/sparql-results+json, application/rdf+xml, text/turtle, text/rdf+n3, text/csv,text/plain")
    public SPARQLResult getQuery(@QueryParam("query") String queryString) {
        return doQuery(queryString);
    }

    @Produces("application/sparql-results+xml, application/sparql-results+json, application/rdf+xml, text/turtle, text/rdf+n3, text/csv,text/plain")
    @POST
    public SPARQLResult postQuery(@FormParam("query") String queryString) {
        return doQuery(queryString);
    }

    private SPARQLResult doQuery(String queryString) {
        Model model = ModelFactory.createDefaultModel();
        List<PersonEntity> personEntitys = personEJB.findAllPersonEntity();
        for(PersonEntity personEntity:personEntitys)
        {
            String personNameID = personEntity.getName();
            Resource r = model.createResource("http://somewhere/"+personNameID.replaceAll("\\s",""));
            r.addProperty(RDFS.label, model.createLiteral(personEntity.getName()));
        }
        return SPARQLQuery.doQuery(queryString, model);
    }
}
