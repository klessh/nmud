function chat(...)
	if #arg == 0 then
		local scripts = client:listScripts();
		client:send(scripts);
	else
		local cmd = client:getFullCommand(arg[1]);
		if cmd ~= nil then
			local help = client:doFunction(cmd,"help");
			client:send(help);
		else
			client:send("Справки по этой команде пока нет.");
		end
	end
end
