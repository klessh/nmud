function choose_encoding()
	encoding = "UTF-8";

	client:send("Please, enter number of your encoding:\n\r1. UTF-8\n\r2. KOI8-R\n\r>");
	entered = client:read();
	entered = string.reverse(entered);
	entered = string.sub(entered, 0, 1);
	
	if entered == "1" then
		encoding = "UTF-8";
	elseif entered == "2" then
		encoding = "KOI8-R";
	else
		encoding = "close";
	end
	return encoding;
end

function choose_profile()
	login = "close";
	client:send("Введите логин (\"новый\" для создания профиля): ");
	login = client:read();
	
	if login == "новый" then
		login = create_profile();
	else

		client:send("Введите пароль: ");
		password = client:read();

		status = server:checkUser(login, password);

		if status == true then
			client:send("Вход выполнен.");
		else
			login = "close";
		end
	end
	
	client:send("\n\r>");
	return login;
end

function create_profile()
	client:send("Логин:");
	login = client:read();

	client:send("Вас устраивает логин "..login.."?[д/н]\n\r>");
	answer = client:read();
	if answer == "д" then
		password1 = "1234567890";
		password2 = "0987654321";
		
		while password1 ~= password2 do
			client:send("Введите пароль: ");
			password1 = client:read();
			client:send("Повторите пароль:");
			password2 = client:read();
		end
		
		server:addUser(login, password1);
		return login;
	elseif answer == "н" then
		return create_profile();
	else
		return create_profile();
	end

	return "test";
end

function help(...)
	client:send("Команда: loginmanager\n\tЭто системный скрипт.");
end
