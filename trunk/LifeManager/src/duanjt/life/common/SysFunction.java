package duanjt.life.common;

import android.content.Intent;

/**
 * 系统功能枚举
 * @author Administrator
 *
 */
public enum SysFunction {
	/**
	 * 领用出库
	 */
	picToolOut,
	/**
	 * 工具入库
	 */
	picToolIn,
	/**
	 * 工具送校
	 */
	picTestIn,
	/**
	 * 工具交接
	 */
	picToolHandover,
	/**
	 * 工具报废
	 */
	picToolScrap,
	/**
	 * 库房转移
	 */
	picHouseTransfer,
	/**
	 * 试验合格
	 */
	picTestQualified,
	/**
	 * 试验出库
	 */
	picTestOut,
	/**
	 * 工具检查
	 */
	picToolCheck,
	/**
	 * 批量检查
	 */
	picBatchSearch,
	/**
	 * 工具新增
	 */
	picToolAdd,
	/**
	 * 归还检查
	 */
	picWaitReturn,
	/**
	 * 领取试验
	 */
	picOutTest
}
