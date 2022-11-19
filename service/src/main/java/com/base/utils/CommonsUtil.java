package com.base.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Slf4j
public class CommonsUtil {

    /**
     * Fetch the page request object from offset, limit, Direction and sortBy This method is helpful to create
     * PageRequest object when you are calling the repository to fetch the page
     *
     * @param offset
     * @param limit
     * @param direction
     * @param sortBy
     * @return
     */
    public static PageRequest getPageRequest(int offset, int limit, Sort.Direction direction, String sortBy) {
        return PageRequest.of(offset / limit, limit, direction, sortBy);
    }

    /**
     * This method is being used to calculate the diff between two geolocation using lat long You can get the result in
     * any of the defined unit which you expect you can pass the Unit as K for kilometers N for Nautical miles
     *
     * @param sourceLat
     * @param sourceLong
     * @param destinationLat
     * @param destinationLong
     * @param unitOfMeasurement: K, N
     * @return double: distance
     */
    public static Double calculateGeographicalDistance(
            Double sourceLat,
            Double sourceLong,
            Double destinationLat,
            Double destinationLong,
            char unitOfMeasurement) {
        if (Objects.isNull(sourceLat)
                || Objects.isNull(sourceLong)
                || Objects.isNull(destinationLat)
                || Objects.isNull(destinationLong)) {
            return null;
        }
        double theta = sourceLong - destinationLong;

        double dist =
                Math.sin(deg2rad(sourceLat)) * Math.sin(deg2rad(destinationLat))
                        + Math.cos(deg2rad(sourceLat)) * Math.cos(deg2rad(destinationLat)) * Math.cos(deg2rad(theta));
        dist = BigDecimal.valueOf(dist).setScale(7, RoundingMode.HALF_EVEN).doubleValue();
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unitOfMeasurement == 'K') {
            dist = dist * 1.609344;
        } else if (unitOfMeasurement == 'N') {
            dist = dist * 0.8684;
        }
        if (Double.isNaN(dist)) {
            return null;
        }
        return (dist);
    }

    /**
     * This function converts decimal degrees to radians
     *
     * @param deg
     * @return double
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * This function converts radians to decimal degrees
     *
     * @param rad
     * @return double
     */
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
