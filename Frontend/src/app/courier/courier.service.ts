import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Courier } from './courier';
import { Observable } from 'rxjs';

@Injectable()
export class CourierService {

  baseURL: string;
  lat: any;
  lng: any;

  constructor(private http: HttpClient) {
    this.baseURL = 'https://localhost:8080/api/orders';
    if (navigator) {
    navigator.geolocation.getCurrentPosition( pos => {
        this.lng = +pos.coords.longitude;
        this.lat = +pos.coords.latitude;
      });
    }
  }


getCourier(): Observable<Array<Courier>> {
    return this.http.get<Array<Courier>>(this.baseURL);
  }

}
