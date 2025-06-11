package com.techelevator.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.dao.JdbcPrizeDao;
import com.techelevator.model.Prize;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/prizes")
public class PrizeController {

    private final JdbcPrizeDao jdbcPrizeDao;

    public PrizeController(JdbcPrizeDao jdbcPrizeDao) {
        this.jdbcPrizeDao = jdbcPrizeDao;
    }

    @GetMapping(path = "")
    public List<Prize> getAllPrizes() {
        try {
            return jdbcPrizeDao.getAllPrizes();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to fetch prizes", e);
        }
    }

    @GetMapping("/{id}")
    public Prize getPrizeById(@PathVariable int id) {
        Prize prize = jdbcPrizeDao.getPrizeById(id);
        if (prize == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prize not found");
        }
        return prize;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Prize addPrize(@RequestBody Prize prize) {
        try {
            jdbcPrizeDao.addPrize(prize);
            return prize;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add prize", e);
        }
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Prize updatePrizeById(@PathVariable int id, @RequestBody Prize prize) {
        try {
            return jdbcPrizeDao.updatePrizeById(id, prize);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid prize ID or prize data", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update prize", e);
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrizeById(@PathVariable int id) {
        try {
            jdbcPrizeDao.deletePrizeById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete prize", e);
        }
    }
}