package ado.sabgil.incheonariport.data.remote.openapi;

import java.util.Map;

import ado.sabgil.incheonariport.data.remote.openapi.response.CongestionResponse;
import ado.sabgil.incheonariport.data.remote.openapi.response.FlightResponse;
import ado.sabgil.incheonariport.data.remote.openapi.response.NoticeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {

    @GET(value = "StatusOfDepartures/getDeparturesCongestion")
    Call<CongestionResponse> getCongestion(
            @Query(value = "ServiceKey", encoded = true) String key,
            @QueryMap Map<String, String> queries);

    @GET(value = "StatusOfPassengerWeahter/getPassengerDeparturesW")
    Call<FlightResponse> getFlight(
            @Query(value = "ServiceKey", encoded = true) String key,
            @QueryMap Map<String, String> queries);

    @GET(value = "PassengerNoticeKR/getfPassengerNoticeIKR")
    Call<NoticeResponse> getNotice(
            @Query(value = "ServiceKey", encoded = true) String key,
            @QueryMap Map<String, String> queries);
}
