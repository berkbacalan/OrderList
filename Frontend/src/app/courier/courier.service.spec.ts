/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CourierService } from './courier.service';

describe('Service: Courier', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CourierService]
    });
  });

  it('should ...', inject([CourierService], (service: CourierService) => {
    expect(service).toBeTruthy();
  }));
});
