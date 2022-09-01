select * from price
where start_date >= ? <= end_date
order by priority
limit 1;