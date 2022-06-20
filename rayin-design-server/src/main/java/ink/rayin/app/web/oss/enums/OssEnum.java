package ink.rayin.app.web.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Oss枚举类
 *
 * @author Chill
 */
@Getter
@AllArgsConstructor
public enum OssEnum {

	/**
	 * minio
	 */
	MINIO("minio", "minio"),

	/**
	 * qiniu
	 */
	QINIU("qiniu", "qiniu"),

	/**
	 * ali
	 */
	ALIYUN("aliyunoss", "aliyunoss"),

	/**
	 * tencent
	 */
	TENCENT("tencentcos", "tencentcos"),

	/**
	 * huawei
	 */
	HUAWEI("huaweiobs", "huaweiobs");

	/**
	 * 名称
	 */
	final String key;
	/**
	 * 类型
	 */
	final String name;

	/**
	 * 匹配枚举值
	 *
	 * @param name 名称
	 * @return OssEnum
	 */
	public static OssEnum of(String name) {
		if (name == null) {
			return null;
		}
		OssEnum[] values = OssEnum.values();
		for (OssEnum ossEnum : values) {
			if (ossEnum.key.equals(name)) {
				return ossEnum;
			}
		}
		return null;
	}

}
