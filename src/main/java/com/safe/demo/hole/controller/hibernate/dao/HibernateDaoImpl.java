package com.safe.demo.hole.controller.hibernate.dao;

import com.safe.demo.hole.controller.hibernate.pojo.HibernateEmp;
import org.hibernate.query.Query;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public  class HibernateDaoImpl implements HibernateDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<HibernateEmp> findEmpByNo(String ename) {
        String sql = "select * from emp e where e.ename= '" + ename+"'";
        Query dataQuery = (Query) entityManager.createNativeQuery(sql);

        List<HibernateEmp> content = dataQuery.getResultList();
        return content;
    }

    @Override
    public List<HibernateEmp> findEmpByNo2(String ename) {
        return null;
    }

    @Override
    public List<HibernateEmp> findEmpByNo3(String ename) {
        return null;
    }

    @Override
    public List<HibernateEmp> findAll() {
        return null;
    }

    @Override
    public List<HibernateEmp> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<HibernateEmp> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<HibernateEmp> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(HibernateEmp entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends HibernateEmp> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends HibernateEmp> S save(S entity) {
        return null;
    }

    @Override
    public <S extends HibernateEmp> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<HibernateEmp> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends HibernateEmp> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<HibernateEmp> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public HibernateEmp getOne(String s) {
        return null;
    }

    @Override
    public <S extends HibernateEmp> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends HibernateEmp> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends HibernateEmp> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends HibernateEmp> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends HibernateEmp> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends HibernateEmp> boolean exists(Example<S> example) {
        return false;
    }
}
