package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.AdresseDTO;
import com.ilisi.jobfinder.model.Adresse;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class AdresseMapper {
    public static Adresse toEntity(AdresseDTO adresseDTO){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        /**
         * X -> Longitude
         * Y -> Latitude
         */
        Point point = null;
        if(adresseDTO.getLatitude() != null && adresseDTO.getLatitude() != null){
            point = geometryFactory.createPoint(new Coordinate(adresseDTO.getLongitude(), adresseDTO.getLatitude()));
        }
        return Adresse.builder()
                .adresse(adresseDTO.getAdress())
                .ville(adresseDTO.getCity())
                .coordiantes(point)
                .build();
    }

    public static AdresseDTO toDto(Adresse adresse) {
        Double longitude = null;
        Double latitude = null;
        if (adresse.getCoordiantes() != null) {
            longitude = adresse.getCoordiantes().getX();
            latitude = adresse.getCoordiantes().getY();
        }
        return AdresseDTO.builder()
                .adress(adresse.getAdresse())
                .city(adresse.getVille())
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
