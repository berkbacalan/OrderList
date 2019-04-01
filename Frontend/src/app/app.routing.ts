import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProductComponent } from './product/product.component';
import { HomeComponent } from './home/home.component';
import { CourierComponent } from './courier/courier.component';
import { AddProductComponent } from './product/add-product/add-product.component';
import { ShoppingComponent } from './shopping/shopping.component';

export const routes: Routes = [

    {
      path: 'product',
      redirectTo: 'products',
      pathMatch: 'full',
    },
    {
      path: 'courier',
      component: CourierComponent,
      pathMatch: 'full',
    },
    {
      path: 'products',
      component: ProductComponent,
      pathMatch: 'full',
    },
    {
      path: 'add-product',
      component: AddProductComponent,
      pathMatch: 'full',
    },
    {
      path: 'user',
      component: ShoppingComponent,
      pathMatch: 'full',
    },
    // {
    //   path: 'edit-product',
    //   component: EditProductComponent,
    //   pathMatch: 'full',
    // },
    {
      path: '',
      component: HomeComponent,
      pathMatch: 'full',
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }
