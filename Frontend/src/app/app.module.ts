import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { AppRoutingModule } from './app.routing';
import { AppComponent } from './app.component';
import { ProductComponent } from './product/product.component';
import { CategoryComponent } from './category/category.component';
import { CourierComponent } from './courier/courier.component';
import { ProductService } from './product/product.service';
import { CourierService } from './courier/courier.service';
import { FormsModule, FormGroup, ReactiveFormsModule} from '@angular/forms';
import { AgmCoreModule } from '@agm/core';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import 'hammerjs';
// tslint:disable-next-line:max-line-length
import { MatTableModule,MatInputModule,MatPaginatorModule,MatSortModule,MatFormFieldModule,MatSelectModule,MatButtonModule,MatCheckboxModule,MatBadgeModule ,MatMenuModule,MatIconModule,MatListModule,MatDividerModule} from  '@angular/material';
import { HomeComponent } from './home/home.component';
import { AddProductComponent } from './product/add-product/add-product.component';
import { ShoppingComponent } from './shopping/shopping.component';
import { AngularFireModule } from '@angular/fire';
import { environment } from 'src/environments/environment';
import {AngularFireAuthModule} from '@angular/fire/auth';
import {AngularFireDatabaseModule} from '@angular/fire/database';
import {MatDialogModule} from '@angular/material/dialog';




@NgModule({
   declarations: [
      AppComponent,
      ProductComponent,
      CategoryComponent,
      ShoppingComponent,
      HomeComponent,
      CourierComponent,
      AddProductComponent
   ],
   imports: [
      FormsModule,
      BrowserAnimationsModule,
      AngularFireModule.initializeApp(environment.firebase),
      AngularFireAuthModule,
      AngularFireDatabaseModule,
      MatTableModule,
      MatPaginatorModule,
      MatSortModule,
      MatInputModule,
      MatDialogModule,
      MatButtonModule,
      MatCheckboxModule,
      MatBadgeModule,
      MatMenuModule,
      MatIconModule,
      MatListModule,
      MatDividerModule,
      MatSelectModule,
      MatFormFieldModule,
      AgmCoreModule.forRoot({
         apiKey: ''// PUT MAPS API KEY HERE
      }),
      BrowserModule,
      AppRoutingModule,
      HttpClientModule,
      ReactiveFormsModule
   ],
   providers: [ProductService],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
