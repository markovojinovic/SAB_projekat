use Projekat;
go

drop procedure if exists SP_FINAL_PRICE;
go
create procedure SP_FINAL_PRICE
	@orderId int,
	@finalPrice decimal(10,3) output
as
begin
	declare @buyerId as integer;

	select @buyerId = (select IdKup from [Porudzbina] where IdPor = @orderId)

	select @finalPrice = (select Cena from Porudzbina where IdPor = @orderId) - (select KolicinaPopusta from Porudzbina where IdPor = @orderId);
	
	if (select sum(Cena) from [Porudzbina] where IdKup = @buyerId and DATEDIFF(day,VremeSlanja, (select Vreme from [Promenljive])) <= 30 )  > 10000 begin
		update [Porudzbina] set PopustKupca = @finalPrice*0.02;
		set @finalPrice = @finalPrice*0.98;
	end

	update [Porudzbina] set Cena = @finalPrice where IdPor = @orderId;

end

go

drop trigger if exists TR_TRANSFER_MONEY_TO_SHOPS;
go
create trigger TR_TRANSFER_MONEY_TO_SHOPS
	on [Porudzbina]
	after insert, delete, update
as
begin
	declare @cursor as cursor;
	declare @cursor2 as cursor;
	declare @idO as int;
	declare @payed as int;
	declare @idOC as int;
	declare @articlePayed as int;
	declare @idS as int;
	declare @time as datetime;

	set @cursor = cursor for
	select i.IdPor, i.Cena, i.VremePrijema from inserted as i join deleted as d on i.IdPor = d.IdPor where i.status = 'arrived'; 

	open @cursor;

	fetch from @cursor
	into @idO, @payed, @time
	while @@FETCH_STATUS=0 begin

		set @cursor2 = cursor for
		select sum(OC.Placeno), S.IdProd from (Sadrzi as OC join Artikal as A on OC.IdArt = A.IdArt) join Prodavnica as S on A.IdProd = S.IdProd where IdPor = @idO group by S.IdProd;
		open @cursor2;
		fetch from @cursor2
		into @articlePayed, @idS
		while @@FETCH_STATUS=0 begin
			
			insert into [Transakcija](Vreme, Kolicina, IdPor, IdProd) values(
				@time, @articlePayed*0.95, @idO,  @idS
			);

			fetch from @cursor2
			into @articlePayed, @idS
		end

		fetch from @cursor
		into @idO, @payed, @time
	end
end

go
use master;