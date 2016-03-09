function on()
	showMenu();
	handleInput("main");
end

function showMenu()
	client:send("Привет! Это главное меню, в котором ты можешь выбрать одно из следущих действий:\n\n1. Начать игру;\n2. Настройки приложения;\n3. Настройки профиля.\n\nЧтобы выбрать действие, набери в поле ввода его название или номер, а затем нажми на кнопку справа.");
end

function handleInput(menuname)
	input = client:read();
	
	if menuname == "main" then
		if input == "1" then
			client:send("Игру можно считать начавшейся, но дальше, к сожалению, ничего не работает.");
			return;
		elseif input == "2" then
			client:startPreferences();
			handleInput("main");
			return;
		elseif input == "3" then
			client:send("Будет проф.");
			return;
		else
			output = handleInput(menuname);
		end
	else
		output = handleInput(menuname);
	end
	
	return;
end
