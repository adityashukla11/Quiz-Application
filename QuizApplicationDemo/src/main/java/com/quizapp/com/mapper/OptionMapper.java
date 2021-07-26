package com.quizapp.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.quizapp.com.domain.Option;
import com.quizapp.com.payload.OptionDTO;

@Mapper
public interface OptionMapper {

	OptionMapper INSTANCE = Mappers.getMapper(OptionMapper.class);
	
	OptionDTO optionToOptionDTO(Option option);
	Option optionDTOtoOption(OptionDTO optionDTO);
}
