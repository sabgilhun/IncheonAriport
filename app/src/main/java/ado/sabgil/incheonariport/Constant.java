package ado.sabgil.incheonariport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import static ado.sabgil.incheonariport.Constant.BottomNavigationIndex.CONGESTION_FRAGMENT_INDEX;
import static ado.sabgil.incheonariport.Constant.BottomNavigationIndex.HOME_FRAGMENT_INDEX;
import static ado.sabgil.incheonariport.Constant.BottomNavigationIndex.MY_PLANE_FRAGMENT_INDEX;
import static ado.sabgil.incheonariport.Constant.BottomNavigationIndex.SETTINGS_FRAGMENT_INDEX;
import static ado.sabgil.incheonariport.Constant.OpenApiResponseCode.APPLICATION_ERROR;
import static ado.sabgil.incheonariport.Constant.OpenApiResponseCode.DB_ERROR;
import static ado.sabgil.incheonariport.Constant.OpenApiResponseCode.HTTP_ERROR;
import static ado.sabgil.incheonariport.Constant.OpenApiResponseCode.NORMAL_SERVICE;
import static ado.sabgil.incheonariport.Constant.OpenApiResponseCode.NO_DATA_ERROR;
import static ado.sabgil.incheonariport.Constant.OpenApiResponseCode.SERVICE_TIME_OUT_ERROR;

public class Constant {
    public static final int NOTICE_DURATION = 6;
    public static final int SEARCH_REQUEST_CODE = 1000;

    public static final String FLIGHT_INFO_ARGUMENT_KEY = "flight_info";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({NORMAL_SERVICE, APPLICATION_ERROR, DB_ERROR,
            NO_DATA_ERROR, HTTP_ERROR, SERVICE_TIME_OUT_ERROR})
    public @interface OpenApiResponseCode {
        String NORMAL_SERVICE = "00";
        String APPLICATION_ERROR = "01";
        String DB_ERROR = "02";
        String NO_DATA_ERROR = "03";
        String HTTP_ERROR = "04";
        String SERVICE_TIME_OUT_ERROR = "05";
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HOME_FRAGMENT_INDEX, CONGESTION_FRAGMENT_INDEX,
            MY_PLANE_FRAGMENT_INDEX, SETTINGS_FRAGMENT_INDEX})
    public @interface BottomNavigationIndex {
        int HOME_FRAGMENT_INDEX = 0;
        int CONGESTION_FRAGMENT_INDEX = 1;
        int MY_PLANE_FRAGMENT_INDEX = 2;
        int SETTINGS_FRAGMENT_INDEX = 3;

    }
}
