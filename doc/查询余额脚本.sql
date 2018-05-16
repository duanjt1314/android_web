select *,
(
	select 
	SUM(case when 
		Save_Type='1000200001' 
		then Price 
		else -Price 
		end) 
	from Bank_Card where Bank_Type=a.Bank_Type and (TIME<a.TIME or (TIME=a.TIME and Create_Time<=a.Create_Time)) 
) bal2
from Bank_Card a where Bank_Type='1000100001' order by TIME desc,Create_Time desc