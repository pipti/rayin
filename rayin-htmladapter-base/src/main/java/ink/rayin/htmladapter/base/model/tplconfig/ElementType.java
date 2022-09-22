package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ElementType枚举类
 *
 * @author wangzhu/王柱
 */
@Getter
@AllArgsConstructor
public enum ElementType {

	/**
	 * catalog
	 */
	CATALOG("catalog", "catalog");

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
	 * @return elementType
	 */
	public static ElementType of(String name) {
		if (name == null) {
			return null;
		}
		ElementType[] values = ElementType.values();
		for (ElementType elementType : values) {
			if (elementType.key.equals(name)) {
				return elementType;
			}
		}
		return null;
	}

}
