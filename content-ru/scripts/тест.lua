function chat(...)
	if arg.n == 0 then 
		client:send("no arg");
	else
		client:send("Аргументов: "..#arg);
		for i = 1, #arg do
			client:send(arg[i]);
		end
	end
end

function help(...)
	client:send("Команда: тест\n\tЭто тестовый эхо-скрипт для проверки аргументов.");
end
