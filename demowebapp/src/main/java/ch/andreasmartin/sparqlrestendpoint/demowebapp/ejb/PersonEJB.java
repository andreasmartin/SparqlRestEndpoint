/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.andreasmartin.sparqlrestendpoint.demowebapp.ejb;

import ch.andreasmartin.sparqlrestendpoint.demowebapp.domain.PersonEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author andreas.martin
 */
@Stateless
public class PersonEJB {

    @PersistenceContext(unitName = "ch.andreasmartin.sparqlrestendpoint_demowebapp_war_1.0PU")
    private EntityManager em;

    public PersonEntity findPersonById(Long id) {
        return em.find(PersonEntity.class, id);
    }

    public List<PersonEntity> findAllPersonEntity() {
        Query query = em.createNamedQuery("findAllPersonEntity");
        return (List<PersonEntity>) query.getResultList();
    }

    public PersonEntity createPerson(PersonEntity personEntity) {
        em.persist(personEntity);
        return personEntity;
    }

    public void deletePerson(PersonEntity personEntity) {
        em.remove(em.merge(personEntity));
    }

    public PersonEntity updatePerson(PersonEntity personEntity) {
        return em.merge(personEntity);
    }
}
