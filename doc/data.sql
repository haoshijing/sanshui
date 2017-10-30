Insert into t_admin(id,userName ,password ,saltPassword,insertTime,lastUpdateTime ,status)
select
 null , 'superadmin','74cb1c732d55c3aa93bad590ffe1ee8b','123',  unix_timestamp()*1000, unix_timestamp()*1000,1;