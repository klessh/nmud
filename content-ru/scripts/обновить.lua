function chat(...)
	client:send("Обновляемся...");
	server:update();
	client:send("Обновились.");
end
