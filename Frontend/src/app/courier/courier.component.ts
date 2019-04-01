import { Component, OnInit } from '@angular/core';
import { CourierService } from './courier.service';
import { Courier } from './courier';

@Component({
  selector: 'app-courier',
  templateUrl: './courier.component.html',
  styleUrls: ['./courier.component.scss'],
  providers: [CourierService]
})
export class CourierComponent implements OnInit {

  title = 'Orders';
  orders: Courier[] = [];
  constructor(private courierService: CourierService) { }
  userPosition: Position;
  lat = 0;
  lng = 0;
  ngOnInit() {
    this.findMe();
    this.courierService.getCourier()
    .subscribe( data => {
      this.orders = data;
    });
  }

  getCourier() {
    this.courierService.getCourier().subscribe(res => {
      this.orders = res;
    });
  }
  findMe() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        position => {
          this.userPosition = position;
          this.lng = position.coords.longitude;
          this.lat = position.coords.latitude;
        },
        error => {},
        { enableHighAccuracy: true }
      );
    } else {
      alert('Geolocation is not supported by this browser.');
    }
  }

}
