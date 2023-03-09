package com.example.player.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.player.model.Player;
import com.example.player.model.PlayerRowMapper;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;


@Service
public class PlayerH2Service{
    
    @Autowired
    private JdbcTemplate db;
    
    public ArrayList<Player> getPlayers() {
       return (ArrayList<Player>) db.query("SELECT * FROM team",new PlayerRowMapper());
    }
    public Player getPlayerById(int playerId){
        try{
      return db.queryForObject("SELECT * FROM team where playerId=?",new PlayerRowMapper(),playerId);
    }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public Player addPlayer(Player player){
        db.update("INSERT INTO team(playerName,jerseyNumber,role) values(?,?,?)",player.getPlayerName(),player.getJerseyNumber(),player.getRole());
        return db.queryForObject("select * from team where playerName=? and jerseyNumber=?",new PlayerRowMapper(),player.getPlayerName(),player.getJerseyNumber());
    }
    public Player updatePlayer(int playerId,Player player){
        if(player.getPlayerName()!=null){
            db.update("Update team set playerName=? where playerId=?",player.getPlayerName(),playerId);
        }
         if(player.getJerseyNumber()!=0){
            db.update("Update team set jerseyNumber=? where playerId=?",player.getJerseyNumber(),playerId);
        }
         if(player.getRole()!=null){
            db.update("Update team set role=? where playerId=?",player.getRole(),playerId);
        }
       return getPlayerById(playerId);
    }

    public void deletePlayer(int playerId){
      db.update("delete from team where playerId=?",playerId);
    }
}