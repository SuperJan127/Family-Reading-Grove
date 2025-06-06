package com.techelevator.dao;

import com.techelevator.model.Family;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcFamilyDaoTest extends BaseDaoTest {
    private JdbcFamilyDao sut;

    @BeforeEach
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcFamilyDao(jdbcTemplate);
    }

    @Test
    public void getAllFamilies_returns_all_families() {
        List<Family> families = sut.getAllFamilies();

        assertNotNull(families);
        assertFalse(families.isEmpty());
    }

   
    @Test
    public void getFamilyById_given_valid_id_returns_family() {
        Family family = sut.getFamilyById(1);

        assertNotNull(family);
        assertEquals(1, family.getFamilyId());
    }

    @Test
    public void getFamilyById_given_invalid_id_returns_null() {
        Family family = sut.getFamilyById(-1);

        assertNull(family);
    }

    @Test
    public void addFamily_creates_a_family() {
        Family family = new Family();
        family.setFamilyName("Test Family");

        sut.addFamily(family);

        Family createdFamily = sut.getFamilyById(family.getFamilyId());
        assertNotNull(createdFamily);
        assertEquals("Test Family", createdFamily.getFamilyName());
    }

    @Test
    public void updateFamily_updates_existing_family() {
        Family family = sut.getFamilyById(1);
        family.setFamilyName("Updated Family");

        sut.updateFamily(family);

        Family updatedFamily = sut.getFamilyById(1);
        assertEquals("Updated Family", updatedFamily.getFamilyName());
    }

    @Test
    public void deleteFamily_removes_family() {
        sut.deleteFamily(1);

        Family family = sut.getFamilyById(1);
        assertNull(family);
    }
}
  
