package com.howtodoinjava.demo.dao;

import java.util.List;

import com.howtodoinjava.demo.model.EmployeeVO;

public interface EmployeeDAO 
{
	public List<EmployeeVO> getAllEmployees();

	public Object getOraclePlayers();

	public Object getMySQLPlayers();

	public Object getCosmosPlayers() throws Exception;
}