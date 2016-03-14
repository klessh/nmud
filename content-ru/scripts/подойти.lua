function chat(...)
	if  #arg == 0 then
		client:send("Подойти к чему?");
	elseif #arg == 2 then
		local res = client:moveTo(arg[1],arg[2]);
		client:send(res);
	else
		client:send("Вы не можете ходить.");
	end
end
