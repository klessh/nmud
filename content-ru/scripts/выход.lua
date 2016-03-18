function chat(...)
	client:send("===========");
	client:doFunction("onClientConnected","on");
end

function help(...)
	client:send("Команда: выход\n\tВыход в главное меню.");
end
