function chat(...)
	u = client:getDescription();
	client:send(u);
end

function help(...)
	client:send("Команда: осмотреться\n\tПолучить информацию о том, что находится вокруг вас.");
end
