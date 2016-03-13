function chat(...)
	if  #arg == 0 then
		client:send("Подойти к чему?");
	else
		client:send("Вы не можете ходить.");
	end
end
