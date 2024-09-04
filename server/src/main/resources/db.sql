-- 更新或添加时自动更新时间
create or replace function update_timestamp_column() returns trigger as
'
begin
    new.update_time := current_timestamp;
    return new;
end
' language plpgsql;
