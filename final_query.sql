curl -X POST "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA" \
-H "Authorization: eyJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjIyQkNFMjQ3NyIsIm5hbWUiOiJBcnlhbiBrdW1hciIsImVtYWlsIjoicy5hcnlhbi5rdW1hcjA0MEBnbWFpbC5jb20iLCJzdWIiOiJ3ZWJob29rLXVzZXIiLCJpYXQiOjE3NTY0NDU4MDQsImV4cCI6MTc1NjQ0NjcwNH0.xpS0QUMxNhlFG9fEh6A2OomseoVJizfXpojdqwwsqWY" \
-H "Content-Type: application/json" \
-d '{
  "finalQuery": "SELECT p.amount AS salary, CONCAT(e.first_name, '\'' '\'', e.last_name) AS name, TIMESTAMPDIFF(YEAR, e.dob, CURDATE()) AS age, d.department_name FROM payments p JOIN employee e ON e.emp_id = p.emp_id JOIN department d ON d.department_id = e.department WHERE DAY(p.payment_time) <> 1 ORDER BY p.amount DESC LIMIT 1;"
}'
