redis-server master/redis.conf &
redis-server master/sentinel.conf --sentinel &
redis-server slave1/redis.conf &
redis-server slave1/sentinel.conf --sentinel &
redis-server slave2/redis.conf &
redis-server slave2/sentinel.conf --sentinel &