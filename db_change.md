# 「顺丰专送」数据库修改记录

## 2017-07-15 v0.1

```sql
alter table sftc_evaluate modify column user_id int(11);
```

## 2017-09-30 v1.0

```sql
alter table sftc_address_book modify column id bigint(11);
```

## 2017-10-14 v1.0.1

```sql
use sftc;

update sftc_order set sender_user_id = 0 where sender_user_id = NULL;
update sftc_order_express set is_use = 0 where is_use = NULL;
update sftc_order_express set ship_user_id = 0 where sender_user_id = NULL;
update sftc_order_express set sender_user_id = 0 where ship_user_id = NULL;
update sftc_order_express_transform set express_id = 0 where express_id = NULL;

alter table sftc_evaluate modify column user_id int(11);

alter table sftc_address_book modify column id bigint(11);

alter table sftc_order alter column sender_user_id set default 0;
alter table sftc_order modify column id varchar(16);

alter table sftc_order_express alter column is_use set default 0;
alter table sftc_order_express alter column ship_user_id set default 0;
alter table sftc_order_express alter column sender_user_id set default 0;
alter table sftc_order_express add directed_code varchar(11) COMMENT '取件码';
alter table sftc_order_express add is_directed int(1) COMMENT '是否面对面下单';
alter table sftc_order_express alter column is_directed set default 0;
alter table sftc_order_express modify column order_id varchar(16);

alter table sftc_order_express_transform alter column express_id set default 0;

alter table sftc_order_cancel drop foreign key sftc_order_cancel_ibfk_1;
alter table sftc_order_cancel modify column order_id varchar(16);
alter table sftc_order_cancel add constraint sftc_order_cancel_ibfk_1 foreign key (order_id) references sftc_order(id) ON UPDATE RESTRICT;
```

### 设置字段 NOT NULL

- sftc_order_express：
    - is_use    
    - ship_user_id    
    - sender_user_id    
    - is_directed
- sftc_order
    - sender_user_id
- sftc_order_cancel
    - order_id

