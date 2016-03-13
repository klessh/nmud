function chat(...)
	if #arg == 0 then
		local scripts = client:listScripts();
		client:send(scripts);
	else
		if client:getFullCommand(arg[1]) ~= nil then
			local help = client:doFunction(arg[1],"help");
			client:send(help);
		else
			client:send("Справки по этой команде пока нет.");
		end
	end
end
