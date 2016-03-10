package com.iillyyaa2033.nmud.abstractserver;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractImporter {
	
	abstract HashMap<String, String> importScripts();
	
	abstract HashMap<String, String> importUsers();
	
	abstract ArrayList<String> importObjects();
	
	abstract void saveUsers(HashMap<String, String> users);
	
	abstract void saveReport(ArrayList<String> report);
}
