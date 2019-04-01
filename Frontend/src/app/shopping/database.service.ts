
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { shoppingProduct } from './shoppingProduct';


@Injectable()
export class DatabaseService {

  databaseURI: string;

  constructor(private http: HttpClient) {
    this.databaseURI = 'http://localhost:8080/api/product';
  }


  getProducts(): Observable<Array<shoppingProduct>> {
    return this.http.get<Array<shoppingProduct>>(this.databaseURI);
  }
}