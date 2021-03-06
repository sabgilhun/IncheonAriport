package ado.sabgil.incheonariport.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ado.sabgil.incheonariport.data.model.FlightInformation;
import ado.sabgil.incheonariport.data.model.Terminal1Congestion;
import ado.sabgil.incheonariport.data.model.Terminal1Notice;
import ado.sabgil.incheonariport.data.remote.openapi.IcnAirportApiHelper;
import ado.sabgil.incheonariport.data.remote.openapi.IcnAirportApiHelperImpl;
import ado.sabgil.incheonariport.data.remote.openapi.request.CongestionRequest;
import ado.sabgil.incheonariport.data.remote.openapi.request.FlightRequest;
import ado.sabgil.incheonariport.data.remote.openapi.request.NoticeRequest;
import ado.sabgil.incheonariport.data.remote.openapi.response.CongestionResponse;
import ado.sabgil.incheonariport.data.remote.openapi.response.FlightResponse;
import ado.sabgil.incheonariport.data.remote.openapi.response.NoticeResponse;
import ado.sabgil.incheonariport.util.TimeUtils;
import androidx.annotation.NonNull;

import static ado.sabgil.incheonariport.Constant.NOTICE_DURATION;

public class DataManagerImpl implements DataManager {

    private IcnAirportApiHelper icnAirportApiHelper;

    private static class LazyHolder {
        private static final DataManagerImpl INSTANCE = new DataManagerImpl();
    }

    public static DataManagerImpl getInstance() {
        return DataManagerImpl.LazyHolder.INSTANCE;
    }

    private DataManagerImpl() {
        icnAirportApiHelper = IcnAirportApiHelperImpl.getInstance();
    }

    @Override
    public void getT1Congestion(@NonNull OnResponseListener<Terminal1Congestion> responseListener,
                                @NonNull OnFailureListener onFailureListener) {
        CongestionRequest request;
        request = new CongestionRequest.Builder()
                .terminalNo("1")
                .build();

        getCongestion(request.getRequestMap(),
                result -> {
                    CongestionResponse.Item item = result.getBody().getItems().getItems().get(0);
                    Terminal1Congestion terminal1Congestion = Terminal1Congestion.from(item);
                    responseListener.onResponse(terminal1Congestion);
                },
                onFailureListener);
    }

    @Override
    public void getFlightInfo(@NonNull OnResponseListener<List<FlightInformation>> onResponseListener,
                              @NonNull OnFailureListener onFailureListener) {
        final int noticeDuration = 6;

        FlightRequest request;
        String fromTime = TimeUtils.getCurrentHourString();
        String toTime = TimeUtils.getAfterHour(fromTime, noticeDuration);
        request = new FlightRequest.Builder()
                .fromTime(fromTime)
                .toTime(toTime)
                .build();

        getFlight(request.getRequestMap(),
                result -> {
                    List<FlightInformation> flightInformations = new ArrayList<>();
                    for (FlightResponse.Item item : result.getBody().getItems().getItems()) {
                        flightInformations.add(FlightInformation.from(item));
                    }
                    onResponseListener.onResponse(flightInformations);
                },
                onFailureListener);
    }

    @Override
    public void getFlightInfoWithId(@NonNull String flightId,
                                    @NonNull OnResponseListener<List<FlightInformation>> onResponseListener,
                                    @NonNull OnFailureListener onFailureListener) {
        FlightRequest request;
        String fromTime = TimeUtils.getCurrentHourString();
        String toTime = TimeUtils.getAfterHour(fromTime, 6);
        request = new FlightRequest.Builder()
                .fromTime(fromTime)
                .toTime(toTime)
                .flightId(flightId)
                .build();

        getFlight(request.getRequestMap(),
                result -> {
                    List<FlightInformation> flightInformations = new ArrayList<>();
                    for (FlightResponse.Item item : result.getBody().getItems().getItems()) {
                        flightInformations.add(FlightInformation.from(item));
                    }
                    onResponseListener.onResponse(flightInformations);
                },
                onFailureListener);
    }

    @Override
    public void getT1PassengerNotice(@NonNull OnResponseListener<Terminal1Notice> onResponseListener,
                                     @NonNull OnFailureListener onFailureListener) {
        int currentHour = TimeUtils.getCurrentHourInteger();

        if (currentHour + NOTICE_DURATION < 24) {
            NoticeRequest request;
            request = new NoticeRequest.Builder()
                    .selectDate("0")
                    .build();

            getNotice(request.getRequestMap(),
                    result -> {
                        Terminal1Notice response;
                        List<NoticeResponse.Item> today = result.getBody().getItems().getItems();
                        response = Terminal1Notice.of(currentHour, NOTICE_DURATION, today);
                        onResponseListener.onResponse(response);
                    },
                    onFailureListener);
        } else {
            NoticeRequest requestToday;
            requestToday = new NoticeRequest.Builder()
                    .selectDate("0")
                    .build();

            NoticeRequest requestTomorrow;
            requestTomorrow = new NoticeRequest.Builder()
                    .selectDate("1")
                    .build();

            getNotice(requestToday.getRequestMap(),
                    resultToday -> {

                        List<NoticeResponse.Item> today =
                                resultToday.getBody().getItems().getItems();

                        getNotice(requestTomorrow.getRequestMap(),
                                resultTomorrow -> {
                                    Terminal1Notice response;

                                    List<NoticeResponse.Item> tomorrow =
                                            resultTomorrow.getBody().getItems().getItems();

                                    response = Terminal1Notice.of(
                                            currentHour, NOTICE_DURATION,
                                            today, tomorrow);

                                    onResponseListener.onResponse(response);
                                },
                                onFailureListener);
                    },
                    onFailureListener);
        }
    }

    @Override
    public void getCongestion(@NonNull Map<String, String> queries,
                              @NonNull OnResponseListener<CongestionResponse> onResponseListener,
                              @NonNull OnFailureListener onFailureListener) {
        icnAirportApiHelper.getCongestion(queries, onResponseListener, onFailureListener);
    }

    @Override
    public void getFlight(@NonNull Map<String, String> queries,
                          @NonNull OnResponseListener<FlightResponse> onResponseListener,
                          @NonNull OnFailureListener onFailureListener) {
        icnAirportApiHelper.getFlight(queries, onResponseListener, onFailureListener);
    }

    @Override
    public void getNotice(@NonNull Map<String, String> queries,
                          @NonNull OnResponseListener<NoticeResponse> onResponseListener,
                          @NonNull OnFailureListener onFailureListener) {
        icnAirportApiHelper.getNotice(queries, onResponseListener, onFailureListener);
    }
}
