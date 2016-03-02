package com.everdessoares.treinamentojediandroid.interfaces;

import com.everdessoares.treinamentojediandroid.model.bean.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/*
 * Created by Everdes on 27/02/2016.
 */
public interface PersonAPI {

    @GET("WS/Person/Listar")
    Call<List<Person>> list();

    @GET("WS/Person/BuscarPorId/{id}")
    Call<Person> selectById(int id);

    @POST("WS/Person/Inserir")
    Call<Person> insert(@Body Person person);

}
