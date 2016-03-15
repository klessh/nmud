function chat(...)
	if #arg == 0 then
		scripts = client:listScripts();
		client:send(scripts);
	else
		cmd = client:getFullCommand(arg[1]);
		
		if cmd ~= nil then
			client:doFunction(cmd,"help");
		else
			client:send("Справки по этой команде пока нет.");
		end
	end
end
