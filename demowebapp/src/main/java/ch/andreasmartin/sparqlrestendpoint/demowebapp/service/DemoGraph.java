/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.andreasmartin.sparqlrestendpoint.demowebapp.service;

import ch.andreasmartin.sparqlrestendpoint.demowebapp.domain.PersonEntity;
import ch.andreasmartin.sparqlrestendpoint.demowebapp.ejb.PersonEJB;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author andreas.martin
 * 
 * LOAD <http://localhost:8080/demowebapp/webresources/DemoGraph>
 * 
 * SELECT ?label
 * WHERE {
 * 	<http://somewhere/JohnSmith> rdfs:label ?label .
 * }
 * 
 */
@Path("DemoGraph")
@Stateless
public class DemoGraph {
    
    @EJB
    PersonEJB personEJB;

    @Produces({"application/rdf+xml", "text/turtle", "text/rdf+n3", "text/plain"})
    @GET
    public Model getGraph(@PathParam("graphUri") String graphUri) {
        Model model = ModelFactory.createDefaultModel();
        List<PersonEntity> personEntitys = personEJB.findAllPersonEntity();
        for(PersonEntity personEntity:personEntitys)
        {
            String personNameID = personEntity.getName();
            Resource r = model.createResource("http://somewhere/"+personNameID.replaceAll("\\s",""));
            r.addProperty(RDFS.label, model.createLiteral(personEntity.getName()));
        }
      
        return model;
    }
}
