package com.webdevelopment.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapper {

	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

	public static <Origin, Destiny> Destiny parseObject(Origin origin, Class<Destiny> destination) {
		return mapper.map(origin, destination);
	}

	public static <Origin, Destiny> List<Destiny> parseListObjects(List<Origin> originList,
			Class<Destiny> destination) {
		List<Destiny> destinyList = new ArrayList<Destiny>();

		for (Origin origin : originList) {
			destinyList.add(mapper.map(origin, destination));
		}

		return destinyList;
	}
}
