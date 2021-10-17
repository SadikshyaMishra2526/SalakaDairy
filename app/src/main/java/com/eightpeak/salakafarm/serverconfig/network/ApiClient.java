//package com.eightpeak.salakafarm.serverconfig.network;
//
//import com.eightpeak.salakafarm.utils.EndPoints;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.moshi.MoshiConverterFactory;
//
//
//public class ApiClient {
//
//   private final static OkHttpClient client = buildClient();
//   private final static Retrofit retrofit = buildRetrofit(client);
//
//
//    private static OkHttpClient buildClient(){
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request request = chain.request();
//
//                        Request.Builder builder = request.newBuilder()
//                                .addHeader("apiconnection","appmobile")
//                                .addHeader("apikey","bc1039fd2d3b46fbf5027c069068b869")
//                                .addHeader("Accept", "application/json")
//                                .addHeader("Connection", "close");
//
//                        request = builder.build();
//
//                        return chain.proceed(request);
//
//                    }
//                });
//        return builder.build();
//
//    }
//
//    private static Retrofit buildRetrofit(OkHttpClient client){
//        return new Retrofit.Builder()
//                .baseUrl(EndPoints.BASE_URL)
//                .client(client)
//                .addConverterFactory(MoshiConverterFactory.create())
//                .build();
//    }
//
//    public static <T> T createService(Class<T> service){
//        return retrofit.create(service);
//    }
//
//    public static <T> T createServiceWithAuth(Class<T> service, final TokenManager tokenManager){
//
//        OkHttpClient newClient = client.newBuilder().addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//
//                Request request = chain.request();
//
//                Request.Builder builder = request.newBuilder();
//
//                if(tokenManager.getToken().getAccessToken() != null){
//                    builder.addHeader("Authorization", "Bearer " + tokenManager.getToken().getAccessToken());
//                }
//                request = builder.build();
//                return chain.proceed(request);
//            }
//        }).authenticator(CustomAuthenticator.getInstance(tokenManager)).build();
//
//        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
//        return newRetrofit.create(service);
//
//    }
//
//    public static Retrofit getRetrofit() {
//        return retrofit;
//    }
//}
//
