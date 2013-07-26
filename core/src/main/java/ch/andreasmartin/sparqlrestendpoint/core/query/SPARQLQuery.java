package ch.andreasmartin.sparqlrestendpoint.core.query;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryException;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;
import com.hp.hpl.jena.query.Dataset;

/**
 *
 * @author Andreas Martin
 */
public class SPARQLQuery {
    
    public static SPARQLResult doQuery(String queryString, Dataset dataset) {
        return doQuery(queryString, dataset, null);
    }
    
    public static SPARQLResult doQuery(String queryString, Model model) {
        return doQuery(queryString, null, model);
    }
    
    private static SPARQLResult doQuery(String queryString, Dataset dataset, Model model)
    {
        Query query = null;
        Model resultModel;
        try {
            query = QueryFactory.create(queryString, Syntax.syntaxARQ);
        } catch (QueryException e) {
            throw e;
        }
        QueryExecution qexec;
        if(dataset==null)
            qexec = QueryExecutionFactory.create(query, model);
        else
            qexec = QueryExecutionFactory.create(query, dataset);

        if (query.isSelectType()) {
            ResultSet rs = qexec.execSelect();
            return new SPARQLResult(rs);
        }
        if (query.isConstructType()) {
            resultModel = qexec.execConstruct();
            return new SPARQLResult(resultModel);
        }

        if (query.isDescribeType()) {
            resultModel = qexec.execDescribe();
            return new SPARQLResult(resultModel);
        }

        if (query.isAskType()) {
            boolean b = qexec.execAsk();
            return new SPARQLResult(b);
        }
        return null;
    }
}
