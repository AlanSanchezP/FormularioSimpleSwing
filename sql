create database basedatos1;
use basedatos1;

create table tblgenero(
	generoID int primary key,
	genero varchar(50) not null
);

create table tblusuarios(
	usuarioID int primary key,
	nombre varchar(200) not null,
	apellidoPaterno varchar(100) not null,
	apellidoMaterno varchar(100) not null,
	sexoID int not null,
	email varchar(150) not null,
	FOREIGN KEY (sexoID) REFERENCES tblgenero(generoID)
);

insert into tblgenero(generoID, genero) values(1, 'Hombre');
insert into tblgenero(generoID, genero) values(2, 'Mujer');
insert into tblgenero(generoID, genero) values(3, 'Indefinido');

delimiter //

create procedure sp_TraeGeneros()
begin
	select generoID, genero from tblgenero;
end //

delimiter ;

delimiter //

create procedure sp_CreaUsuario(
	in recibeNombre varchar(200),
	in recibeApellidoP varchar(100),
	in recibeApellidoM varchar(100),
	in recibeGenero int,
	in recibeEmail varchar(150)
)
begin
	declare nuevoID int;
	declare existente int;

	set existente = (select count(*) from tblusuarios where email = recibeEmail);
	
	if(existente = 0) then
		set nuevoID = (select ifnull(max(usuarioID), 0) + 1 from tblusuarios);
		insert into tblusuarios(usuarioID, nombre, apellidoPaterno, apellidoMaterno, sexoID, email)
		values(nuevoID, recibeNombre, recibeApellidoP, recibeApellidoM, recibeGenero, recibeEmail);
		select nuevoID as 'Mensaje';
	else
		select 'Operación cancelada. Email registrado previamente' as 'Mensaje';
	end if;
	
	
end //

delimiter ;

select * from tblusuarios;

