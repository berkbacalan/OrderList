import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from './product';
import { Observable } from 'rxjs';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Injectable()
export class ProductService {

  baseURL: string;

  constructor(private http: HttpClient) {
    this.baseURL = 'http://localhost:8080/api/product';
  }

  form: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
    price: new FormControl('', Validators.required),
    stock: new FormControl('', Validators.required)
  });


  getProducts(): Observable<Array<Product>> {
    return this.http.get<Array<Product>>(this.baseURL);
  }

  public createProduct(product) {
    return this.http.post<Product>(this.baseURL, product);
  }
  deleteProduct(id: number) {
    return this.http.delete(this.baseURL + '/' + id);
  }
  getProductById(arg0: number): any {
    throw new Error('Method not implemented.');
  }
  initializeFormGroup() {
    this.form.setValue({
      name: '',
      price: '',
      stock: ''
    });
  }


}
