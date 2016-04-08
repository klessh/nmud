function chat(...)
	if #arg == 0 then
		scripts = client:listScripts();
		client:send("Список доступных команд: \n======\n"..scripts.."\n======");
	else
		cmd = client:getFullCommand(arg[1]);
		
		if cmd ~= nil then
			client:doFunction(cmd,"help");
		else
			client:send("Справки по этой команде пока нет.");
		end
	end
end

function help(...)
	client:send([[Команда: справка
	Синтаксис: "справка"  или "справка" <имя команды>
	- Без аргументов выводит список скриптов.
	- С одним аргументом выводит справку об указанном в аргументе скрипте.]]);
end
