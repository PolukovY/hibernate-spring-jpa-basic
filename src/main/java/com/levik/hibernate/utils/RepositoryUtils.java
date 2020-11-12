package com.levik.hibernate.utils;

import com.levik.hibernate.utils.exception.PersistenceException;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;


@UtilityClass
public class RepositoryUtils {

    private EntityManagerFactory entityManagerFactory;

    public void execute(Consumer<EntityManager> entityManagerOperation) {
        executeWithResult(it -> {
            entityManagerOperation.accept(it);
            return null;
        });
    }

    public <T> T executeWithResult(Function<EntityManager, T> entityManagerFunction) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            T result = entityManagerFunction.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception exe) {
            entityManager.getTransaction().rollback();
            String errorDetails = String.format("Can't performing repository operation message %s", exe.getMessage());
            throw new PersistenceException(errorDetails, exe);
        } finally {
            entityManager.close();
        }
    }

    public void creteEntityManagerFactory(String persistenceUnitName) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    public Statistics getStatistics() {
        SessionFactory unwrap = entityManagerFactory.unwrap(SessionFactory.class);
        return unwrap.getStatistics();
    }

    public void closeEntityManagerFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
