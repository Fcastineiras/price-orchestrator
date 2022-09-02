select * from price
where brand_id = ?
and product_id = ?
and start_date <= ?
and ? <= end_date
order by priority
limit 1;