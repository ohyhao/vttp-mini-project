insert into user (email, password, name)
	values 
		('fred@gmail.com', sha1(12345678), 'Fred'),
        ('wilma@gmail.com', sha1(87654321), 'Wilma');

insert into assets (symbol, shares, share_price, date_traded, user_id)
	values
		('TSLA', 10, 742.36, '2022-04-12', '1'),
        ('AAPL', 10, 119.52, '2022-03-25', '1');