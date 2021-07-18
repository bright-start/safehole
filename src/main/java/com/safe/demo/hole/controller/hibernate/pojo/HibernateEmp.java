package com.safe.demo.hole.controller.hibernate.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "emp")
public class HibernateEmp {
    private Integer empno;
    private String ename;
    @Id
    /**
     * 可选的主键的生成策略
     * 可选的值：
     TABLE,
     SEQUENCE, //主要用于Oracle数据库
     IDENTITY,
     AUTO; //根据数据库定义主键生成策略，myql底层是自动增长主列
     @GeneratedValue(strategy = GenerationType.AUTO)
     */
    /**
     * 如果主键是指派的，就不能用jpa注解
     */
    @GeneratedValue(generator = "empno") //定义一个主键生成策略
    @GenericGenerator(name = "empno",strategy = "assigned")
    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }
    @Column(name = "ename")
    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }
}
