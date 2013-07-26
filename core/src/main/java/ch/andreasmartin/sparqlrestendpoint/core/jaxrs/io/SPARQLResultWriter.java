package ch.andreasmartin.sparqlrestendpoint.core.jaxrs.io;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.resultset.SPARQLResult;
import com.hp.hpl.jena.sparql.resultset.ResultsFormat;

/**
 *
 * @author Andreas Martin
 * Based on: https://github.com/norcalrdf/Tenuki/blob/master/src/main/java/com/oreilly/rdf/tenuki/jaxrs/io/ModelWriter.java
 */
@Provider
@Produces("application/sparql-results+xml, application/sparql-results+json, application/rdf+xml, text/turtle, text/rdf+n3, text/plain, text/csv")
public class SPARQLResultWriter implements MessageBodyWriter<SPARQLResult> {

    @Override
    public long getSize(SPARQLResult t, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
        return SPARQLResult.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(SPARQLResult result, Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) throws IOException,
            WebApplicationException {
        if (result.isGraph()) {
            Model model = result.getModel();
            RDFModelWriter modelWriter = new RDFModelWriter();
            modelWriter.writeTo(model, type, genericType, annotations, mediaType, httpHeaders, entityStream);
        }
        if (result.isResultSet()) {
            ResultSet rs = result.getResultSet();
            ResultsFormat fmt = null;
            if (mediaType.isCompatible(MediaType
                    .valueOf("application/sparql-results+xml"))) {
                fmt = ResultsFormat.FMT_RS_XML;
                httpHeaders.putSingle("Content-Type", "application/sparql-results+xml");
            }
            if (mediaType.isCompatible(MediaType.valueOf("text/plain"))) {
                fmt = ResultsFormat.FMT_TEXT;
                httpHeaders.putSingle("Content-Type", "text/plain");
            }
            if (mediaType.isCompatible(MediaType.valueOf("text/csv"))) {
                fmt = ResultsFormat.FMT_RS_CSV;
                httpHeaders.putSingle("Content-Type", "text/csv");
            }
            if (mediaType.isCompatible(MediaType.valueOf("application/sparql-results+json"))) {
                fmt = ResultsFormat.FMT_RS_JSON;
                httpHeaders.putSingle("Content-Type", "application/sparql-results+json");
            }
            ResultSetFormatter.output(entityStream, rs, fmt);
        }
    }
}