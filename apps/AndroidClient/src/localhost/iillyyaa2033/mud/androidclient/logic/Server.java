package localhost.iillyyaa2033.mud.androidclient.logic;

public class Server extends Thread{
	
	private Core core;
	
	public Server(Core core){
		this.core = core;
	}

	@Override
	public void run() {
		
		super.run();
	}
	
/*	public synchronized void update(){
			updateScripts();
			updateUsers();
	}

	public synchronized void updateScripts(){
		scriptsmap = importer.importChatScripts();
		if(scriptsmap == null){
			send("Ошибка импорта команд.");
			scriptsmap = new HashMap<String, String>();
		} else {
			send("Команды загружены.");
		}
	}

	public synchronized void updateUsers(){
		users =  importer.importUsers();
		if(users == null){
			send("Ошибка импорта списка пользователей.");
			users = new HashMap<String, String>();
		} else {
			send("Список пользователей загружен.");
		}
	}

	public synchronized void save(){
		saveUsers();
	}

	public synchronized void saveUsers(){
		importer.saveUsers(users);
	}

	public void listScripts(){
		String[] scripts = new String[scriptsmap.size()];
		scriptsmap.keySet().toArray(scripts);

		for(String script : scripts){
			send(script);
		}
	}

	public void addScript(String name, String scr){
		scriptsmap.put(name, scr);
	}

	public void addUser(String user, String password){
		users.put(user, password);
	}

	public boolean checkUser(String user, String password){
		if(users.containsKey(user)){
			if(users.get(user).equals(password)) return true;
			else return false;
		} else{
			return false;
		}
	} */
}
