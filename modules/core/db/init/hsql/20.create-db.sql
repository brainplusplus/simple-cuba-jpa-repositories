-- begin SAMPLE_SALES_ORDER
alter table SAMPLE_SALES_ORDER add constraint FK_SAMPLE_SALES_ORDER_ON_CUSTOMER foreign key (CUSTOMER_ID) references SAMPLE_CUSTOMER(ID)^
create index IDX_SAMPLE_SALES_ORDER_ON_CUSTOMER on SAMPLE_SALES_ORDER (CUSTOMER_ID)^
-- end SAMPLE_SALES_ORDER