package com.howtodoinjava.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.howtodoinjava.demo.service.EmployeeManager;

@Controller
@RequestMapping("")
public class EmployeeController 
{
	@Autowired
	EmployeeManager manager;

	@RequestMapping(value = "/postgres/getPlayers", method = RequestMethod.GET)
	public String getPlayersPostgres(Model model)
	{
		model.addAttribute("employees", manager.getAllEmployees());
		return "employeesListDisplay";
	}
	
	@RequestMapping(value = "/oracle/getPlayers", method = RequestMethod.GET)
	public String getPlayersOracle(Model model)
	{
		model.addAttribute("employees", manager.getOraclePlayers());
		return "employeesListDisplay";
	}
	
	@RequestMapping(value = "/mysql/getPlayers", method = RequestMethod.GET)
	public String getPlayersMySQL(Model model)
	{
		model.addAttribute("employees", manager.getMySQLPlayers());
		return "employeesListDisplay";
	}
	
	@RequestMapping(value = "/cosmos/getPlayers", method = RequestMethod.GET)
	public String getPlayersCosmosDB(Model model) throws Exception
	{
		model.addAttribute("employees", manager.getCosmosPlayers());
		return "employeesListDisplay";
	}
}