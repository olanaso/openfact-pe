package org.openfact.models.jpa.entities;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class SerieNumeroController {

    @PersistenceContext
    private EntityManager em;

    public AbstractMap.SimpleEntry<Integer, Integer> getSiguienteSerieNumero(String organizationId, String documentType, String firstLetter) {
        return siguienteSerieNumero(organizationId, documentType, firstLetter);
    }

    private AbstractMap.SimpleEntry<Integer, Integer> siguienteSerieNumero(String organizationId, String documentType, String firstLetter) {
        Optional<SerieNumeroEntity> ultimaSerieNumero;

        TypedQuery<SerieNumeroEntity> query = em.createNamedQuery("selectSerieNumero", SerieNumeroEntity.class);
        query.setParameter("organizationId", organizationId);
        query.setParameter("documentType", documentType);
        query.setParameter("firstLetter", firstLetter);
        query.setMaxResults(1);
        List<SerieNumeroEntity> resultList = query.getResultList();
        ultimaSerieNumero = getFirstResult(resultList);

        if (ultimaSerieNumero.isPresent()) {
            SerieNumeroEntity serieNumeroEntity = ultimaSerieNumero.get();

            if (99_999_999 > serieNumeroEntity.getNumero() + 1) {
                serieNumeroEntity.setNumero(serieNumeroEntity.getNumero() + 1);
                em.merge(serieNumeroEntity);
                em.flush();
            } else {
                serieNumeroEntity.setSerie(serieNumeroEntity.getSerie() + 1);
                serieNumeroEntity.setNumero(1);
                em.merge(serieNumeroEntity);
                em.flush();
            }

            return new AbstractMap.SimpleEntry<>(serieNumeroEntity.getSerie(), serieNumeroEntity.getNumero());
        } else {
            TypedQuery<DocumentEntity> query1 = em.createQuery("select d from DocumentEntity d where d.organizationId=:organizationId and d.documentType=:documentType and upper(d.documentId) like :firstLetter order by d.documentId desc", DocumentEntity.class);
            query1.setParameter("organizationId", organizationId);
            query1.setParameter("documentType", documentType);
            query1.setParameter("firstLetter", firstLetter.toUpperCase() + "%");
            query1.setMaxResults(1);
            List<DocumentEntity> resultList1 = query1.getResultList();
            Optional<DocumentEntity> ultimoDocumento = getFirstResult(resultList1);

            int serie;
            int numero;
            if (ultimoDocumento.isPresent()) {
                DocumentEntity documentEntity = ultimoDocumento.get();
                String[] split = documentEntity.getDocumentId().split("-");
                serie = Integer.valueOf(split[0].replaceAll("\\D+", ""));
                numero = Integer.valueOf(split[1]);
                if (99_999_999 < numero + 1) {
                    serie++;
                    numero = 1;
                }
            } else {
                serie = 1;
                numero = 1;
            }

            SerieNumeroEntity serieNumeroEntity = new SerieNumeroEntity();
            serieNumeroEntity.setId(UUID.randomUUID().toString());
            serieNumeroEntity.setSerie(serie);
            serieNumeroEntity.setNumero(numero);
            serieNumeroEntity.setDocumentType(documentType);
            serieNumeroEntity.setFirstLetter(firstLetter);
            serieNumeroEntity.setOrganizationId(organizationId);
            em.persist(serieNumeroEntity);
            em.flush();

            return new AbstractMap.SimpleEntry<>(serieNumeroEntity.getSerie(), serieNumeroEntity.getNumero());
        }
    }

    public static <T> Optional<T> getFirstResult(List<T> list) {
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        } else if (list.isEmpty()) {
            return Optional.empty();
        } else {
            throw new IllegalStateException("Mas de un resultado encontrado");
        }
    }

}
