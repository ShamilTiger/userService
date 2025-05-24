package ru.ilyin.userservice.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.ilyin.userservice.entity.User;
import ru.ilyin.userservice.util.HibernateUtil;

import java.util.List;

public class UserDaoImpl implements UserDao{

    @Override
    public User save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            return user;
        } catch (Exception e){
            if(transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException( "Failed to save user: " + e.getMessage(), e);
        }
    }

    @Override
    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.get(User.class, id);
        } catch (Exception e){
            throw new RuntimeException("Failed find user by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<User> query = session.createQuery("From User", User.class);
            return query.list();
        } catch (Exception e){
            throw new RuntimeException("Failed to find all users: "+ e.getMessage(), e);
        }
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.update(user);
            return user;
        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new RuntimeException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null){
                session.delete(user);
            }
            transaction.commit();
        }catch (Exception e){
            if(transaction != null){
                transaction.rollback();
            }
            throw new RuntimeException("Failed to delete user: " + e.getMessage() , e);
        }

    }
}
