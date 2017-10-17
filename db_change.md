- 2017-07-15 修改评价表sftc_evaluate的字段

```sql
alter table sftc_evaluate modify column user_id int(11);
```

- 2017-09-30 修改地址表sftc_address_book的字段

```sql
alter table sftc_address_book modify column id bigint(11);
```

- 2017-10-14 修改快递表sftc_order_express的字段

```sql
alter table sftc_order_express alter column is_use set default 0;
alter table sftc_order_express alter column ship_user_id set default 0;
alter table sftc_order_express alter column sender_user_id set default 0;
```

- 2017-10-14 修改订单表id

```sql
use sftc;
alter table sftc_evaluate modify column user_id int(11);
alter table sftc_address_book modify column id bigint(11);
update sftc_order_express set is_use = 0 where is_use = NULL;
update sftc_order_express set ship_user_id = 0 where sender_user_id =NULL;
update sftc_order_express set sender_user_id = 0 where ship_user_id=NULL;
alter table sftc_order_express alter column is_use set default 0;
alter table sftc_order_express alter column ship_user_id set default 0;
alter table sftc_order_express alter column sender_user_id set default 0;
alter table sftc_order_express add directed_code varchar(11);
ALTER TABLE sftc_order_express ADD is_directed INT(1)  NOT NULL  DEFAULT '0'  COMMENT '是否面对面下单'  AFTER directed_code;
alter table sftc_order_express alter column is_directed set default 0;
alter table sftc_order modify column id varchar(16);
alter table sftc_order_express modify column order_id varchar(16);
alter table sftc_order_cancel drop foreign key sftc_order_cancel_ibfk_1;
alter table sftc_order_cancel modify column order_id varchar(16);
alter table sftc_order modify column id varchar(16);
alter table sftc_order_cancel add constraint sftc_order_cancel_ibfk_1 foreign key (order_id) references sftc_order(id) ON UPDATE RESTRICT;
```

- 2017-10-14 设置字段 NOT NULL ：
    - sftc_order_express：
        - is_use    
        - ship_user_id    
        - sender_user_id    
        - is_directed


