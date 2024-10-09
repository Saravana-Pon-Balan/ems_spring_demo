package com.e5.ems.mapper;

import com.e5.ems.dto.PassportDTO;
import com.e5.ems.model.Passport;
import org.springframework.stereotype.Component;


@Component
public class PassportMapper {

    /**
     * <p>
     *     It is a method for converting the Passport to passportDTO
     * </p>
     * @param passport
     *          is used for convert PassportDto object to Passport
     * @return PassportDTO
     *          converted PassportDTO
     */
    public static PassportDTO passportToPassportDto(Passport passport) {
        return PassportDTO.builder()
                .placeOfBirth(passport.getPlaceOfBirth())
                .passportNumber(passport.getPassportNumber())
                .dateOfExpiry(passport.getDateOfExpiry())
                .build();
    }

    /**
     * <p>
     *     It is a method for converting the PassportDTO to Passport
     * </p>
     * @param passportDto
     *          is used for convert passportDto object to passport
     * @return Passport
     *          converted Passport
     */
    public static Passport passportDtoToPassport(PassportDTO passportDto) {
        return Passport.builder()
                .id(passportDto.getId())
                .placeOfBirth(passportDto.getPlaceOfBirth())
                .passportNumber(passportDto.getPassportNumber())
                .dateOfExpiry(passportDto.getDateOfExpiry())
                .build();
    }

}
