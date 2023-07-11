USE Projekat
go

DROP TABLE IF EXISTS [Promenljive]
go

DROP TABLE IF EXISTS [Transakcija]
go

DROP TABLE IF EXISTS [Sadrzi]
go

DROP TABLE IF EXISTS [Porudzbina]
go

DROP TABLE IF EXISTS [Put]
go

DROP TABLE IF EXISTS [Kupac]
go

DROP TABLE IF EXISTS [Artikal]
go

DROP TABLE IF EXISTS [Prodavnica]
go

DROP TABLE IF EXISTS [Saobracajnica]
go

DROP TABLE IF EXISTS [Grad]
go

CREATE TABLE [Artikal]
( 
	[IdArt]              integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Cena]               integer  NOT NULL ,
	[Kolicina]           integer  NOT NULL ,
	[IdProd]             integer  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdGrad]             integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL 
)
go

CREATE TABLE [Kupac]
( 
	[IdKup]              integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Racun]              decimal(10,3)  NOT NULL ,
	[PrometKupca]        decimal(10,3)  NOT NULL ,
	[IdGrad]             integer  NOT NULL 
)
go

CREATE TABLE [Porudzbina]
( 
	[IdPor]              integer  IDENTITY  NOT NULL ,
	[Status]             varchar(100)  NOT NULL ,
	[KolicinaPopusta]    integer  NOT NULL ,
	[Cena]               decimal(10,3)  NOT NULL ,
	[VremePrijema]       Datetime2(3)  NULL ,
	[VremeSlanja]        Datetime2(3)  NULL ,
	[Lokacija]           integer  NULL ,
	[PopustKupca]        decimal(10,3)  NOT NULL ,
	[IdKup]              integer  NOT NULL ,
	[IdPut]              integer  NULL 
)
go

CREATE TABLE [Prodavnica]
( 
	[IdProd]             integer  IDENTITY  NOT NULL ,
	[Ime]                varchar(100)  NOT NULL ,
	[Popust]             integer  NOT NULL ,
	[Promet]			 decimal(10,3)  NOT NULL ,
	[IdGrad]             integer  NOT NULL 
)
go

CREATE TABLE [Promenljive]
( 
	[IdProm]             integer  IDENTITY  NOT NULL ,
	[Vreme]              Datetime2(3)  NULL ,
	[Profit]             decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Put]
( 
	[IdPut]              integer  IDENTITY  NOT NULL ,
	[Dani]               integer  NOT NULL ,
	[Grad]				 integer NOT NULL,
	[IdNextPut]          integer  NULL,
	[Fork]				 integer   NOT NULL,
)
go

CREATE TABLE [Sadrzi]
( 
	[IdSad]              integer  IDENTITY  NOT NULL ,
	[Placeno]            binary  NOT NULL ,
	[IdArt]              integer  NOT NULL ,
	[IdPor]              integer  NOT NULL ,
	[Broj]               integer  NOT NULL 
)
go

CREATE TABLE [Saobracajnica]
( 
	[IdSaob]             integer  IDENTITY  NOT NULL ,
	[Udaljenost]         integer  NOT NULL ,
	[Grad2]              integer  NOT NULL ,
	[Grad1]              integer  NOT NULL 
)
go

CREATE TABLE [Transakcija]
( 
	[IdTrans]            integer  IDENTITY  NOT NULL ,
	[Vreme]              Datetime2(3)  NOT NULL ,
	[Kolicina]           decimal(10,3)  NOT NULL ,
	[IdPor]              integer  NULL ,
	[IdProd]             integer  NULL 
)
go

ALTER TABLE [Artikal]
	ADD CONSTRAINT [XPKArtikal] PRIMARY KEY  CLUSTERED ([IdArt] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

ALTER TABLE [Kupac]
	ADD CONSTRAINT [XPKKupac] PRIMARY KEY  CLUSTERED ([IdKup] ASC)
go

ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [XPKPorudzbina] PRIMARY KEY  CLUSTERED ([IdPor] ASC)
go

ALTER TABLE [Prodavnica]
	ADD CONSTRAINT [XPKProdavnica] PRIMARY KEY  CLUSTERED ([IdProd] ASC)
go

ALTER TABLE [Promenljive]
	ADD CONSTRAINT [XPKPromenljive] PRIMARY KEY  CLUSTERED ([IdProm] ASC)
go

ALTER TABLE [Put]
	ADD CONSTRAINT [XPKPut] PRIMARY KEY  CLUSTERED ([IdPut] ASC)
go

ALTER TABLE [Sadrzi]
	ADD CONSTRAINT [XPKSadrzi] PRIMARY KEY  CLUSTERED ([IdSad] ASC)
go

ALTER TABLE [Saobracajnica]
	ADD CONSTRAINT [XPKSaobracajnica] PRIMARY KEY  CLUSTERED ([IdSaob] ASC)
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [XPKTransakcija] PRIMARY KEY  CLUSTERED ([IdTrans] ASC)
go


ALTER TABLE [Artikal]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([IdProd]) REFERENCES [Prodavnica]([IdProd])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Kupac]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([IdKup]) REFERENCES [Kupac]([IdKup])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Porudzbina]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([IdPut]) REFERENCES [Put]([IdPut])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Prodavnica]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Sadrzi]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([IdArt]) REFERENCES [Artikal]([IdArt])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Sadrzi]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([IdPor]) REFERENCES [Porudzbina]([IdPor])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Saobracajnica]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([Grad2]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Saobracajnica]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([Grad1]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdPor]) REFERENCES [Porudzbina]([IdPor])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Transakcija]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IdProd]) REFERENCES [Prodavnica]([IdProd])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
