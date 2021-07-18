package com.safe.demo.hole.controller.hibernate.dao;

import com.safe.demo.hole.controller.hibernate.pojo.HibernateEmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HibernateDao extends JpaRepository<HibernateEmp, String> {

    public List<HibernateEmp> findEmpByNo(String empno);

    @Query("select e from HibernateEmp e where e.ename= :ename")
    public List<HibernateEmp> findEmpByNo2(@Param("ename") String ename);

    @Query(value = "select e from HibernateEmp e where e.ename=?", nativeQuery = true)
    public List<HibernateEmp> findEmpByNo3(@Param("ename") String ename);
}
