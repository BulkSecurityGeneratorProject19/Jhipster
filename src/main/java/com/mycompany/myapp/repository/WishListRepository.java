package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.WishList;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Cassandra repository for the WishList entity.
 */
@Repository
public class WishListRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<WishList> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public WishListRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(WishList.class);
        this.findAllStmt = session.prepare("SELECT * FROM wishList");
        this.truncateStmt = session.prepare("TRUNCATE wishList");
    }

    public List<WishList> findAll() {
        List<WishList> wishListsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                WishList wishList = new WishList();
                wishList.setId(row.getUUID("id"));
                wishList.setName(row.getString("name"));
                wishList.setCretion(row.getString("cretion"));
                return wishList;
            }
        ).forEach(wishListsList::add);
        return wishListsList;
    }

    public WishList findOne(UUID id) {
        return mapper.get(id);
    }

    public WishList save(WishList wishList) {
        if (wishList.getId() == null) {
            wishList.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<WishList>> violations = validator.validate(wishList);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(wishList);
        return wishList;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
