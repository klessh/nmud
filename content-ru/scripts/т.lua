function chat()
	client:doFunction("т","test");
end

function test(arg)
	if arg == nil then
		client:send("no args");
	else
		client:send(arg);
	end
end
