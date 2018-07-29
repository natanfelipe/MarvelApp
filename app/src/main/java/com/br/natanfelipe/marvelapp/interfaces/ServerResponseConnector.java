package com.br.natanfelipe.marvelapp.interfaces;

public interface ServerResponseConnector {

    void onConnectionResult(int statusCode, Object responseData);
}
