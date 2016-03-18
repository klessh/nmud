function on()
	showMenu();
	handleInput("main");
end

function showMenu()
	client:send(
[[Привет! Это главное меню, в котором ты можешь выбрать одно из следущих действий:
	
	1. Начать игру;
	2. Настройки приложения;
	3. Настройки профиля;
	4. Выход.
		
Чтобы выбрать действие, набери в поле ввода его номер, а затем нажми на кнопку справа.]]);
end

function handleInput(menuname)
	input = client:read();
	
	if menuname == "main" then
		if input == "1" then
			client:send("===========");
			client:doFunction("осмотреться","chat");
			return;
		elseif input == "2" then
			client:startPreferences();
			handleInput("main");
			return;
		elseif input == "3" then
			client:send("Настройки профиля будут, но попозже.");
			handleInput("main");
			return;
		elseif input == "4" then
			client:close();
			return;
		else
			output = handleInput(menuname);
		end
	else
		output = handleInput(menuname);
	end
	
	return;
end

function help(...)
	client:send("Команда: onClientConnected\n\tЭто системный скрипт.");
end
