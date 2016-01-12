/**
 * This file is part of WANTED: Bad-ou-Alyve.
 *
 * WANTED: Bad-ou-Alyve is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WANTED: Bad-ou-Alyve is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WANTED: Bad-ou-Alyve.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.badoualy.badoualyve.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import rx.Observable;
import rx.Single;

/** Utility class to wrap a Gdx net call to a callable to use in a synchronous way (and with ReactiveX) */
class NetworkCallable<T> implements Callable<T>, Net.HttpResponseListener {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Class<T> clazz; // Hack the get the type's class

    private final Lock lock;
    private Condition condition;

    private final String url;
    private final String parameter;

    private T result;
    private Throwable error;

    public NetworkCallable(String url, Class<T> clazz) {
        this(url, null, clazz);
    }

    public NetworkCallable(String url, String parameter, Class<T> clazz) {
        this.clazz = clazz;

        lock = new ReentrantLock();
        condition = lock.newCondition();

        this.url = url;
        this.parameter = parameter != null && !parameter.isEmpty() ? parameter : null;
    }

    @Override
    public T call() throws Exception {
        Net.HttpRequest request = new HttpRequestBuilder().newRequest()
                                                          .method(Net.HttpMethods.GET)
                                                          .url(parameter != null ? String.format(url, parameter) : url)
                                                          .build();

        lock.lock();
        Gdx.net.sendHttpRequest(request, this);
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (error != null)
                error = e;
        }
        lock.unlock();
        return result;
    }

    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        String resultContent = httpResponse.getResultAsString();
        T result = gson.fromJson(resultContent, clazz);
        System.out.println("Received: " + resultContent);
        lock.lock();
        // Smallest and fastest critical block
        this.result = result;
        condition.signalAll();
        lock.unlock();
    }

    @Override
    public void failed(Throwable t) {
        lock.lock();
        this.error = t;
        condition.notifyAll();
        lock.unlock();
    }

    @Override
    public void cancelled() {

    }

    public Throwable getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public Single<T> toObservable() {
        return Observable.fromCallable(this).toSingle();
    }
}
