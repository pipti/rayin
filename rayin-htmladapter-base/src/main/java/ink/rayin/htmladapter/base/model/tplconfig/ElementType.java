/**
 * Copyright (c) 2022-2030, Janah Wang / 王柱 (wangzhu@cityape.tech).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ElementType枚举类
 *
 * @author Janah Wang / 王柱 2022-09-22
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
