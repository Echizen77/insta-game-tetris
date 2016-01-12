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

import rx.Single;

/** Convenience and beauty wrapper for NetworkCallable **/
public class NetworkOperation<T> {

    private Class<T> clazz; // Hack the get the type's class

    private final String url;
    private final String parameter;

    public NetworkOperation(String url, Class<T> clazz) {
        this(url, null, clazz);
    }

    public NetworkOperation(String url, String parameter, Class<T> clazz) {
        this.clazz = clazz;

        this.url = url;
        this.parameter = parameter != null && !parameter.isEmpty() ? parameter : null;
    }

    public Single<T> execute() {
        return new NetworkCallable<T>(url, parameter, clazz).toObservable();
    }
}
