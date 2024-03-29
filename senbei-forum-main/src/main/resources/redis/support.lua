local infoKey=KEYS[1]
local countKey=KEYS[2]
local supportedId =ARGV[1]
local userId =ARGV[2]
-- local hashKey=supportedId.."::"..userId
-- 拼接key
local temp={supportedId,"::",userId}
local hashKey=table.concat(temp,"")
-- 去除双引号防止出现""10"::"11""
hashKey=string.gsub(hashKey,'%"','')
supportedId=string.gsub(supportedId,'%"','')
local flag=redis.call('HGET',infoKey,hashKey)
-- 为1则为已经点赞，则该次动作为取消点赞
if flag == '1' then
    redis.call('HSET',infoKey,hashKey,0)
    redis.call('HINCRBY',countKey,supportedId,-1)
else
  -- 为空或0则为未点赞，则该次动作为点赞
      redis.call('HSET',infoKey,hashKey,1)
      redis.call('HINCRBY',countKey,supportedId,1)
end
return flag
