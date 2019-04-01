import { Component, OnInit } from '@angular/core';
import { Product } from './product';
import { ProductService } from './product.service';
import { MatDialog, MatDialogConfig, MatTableDataSource, MatSort, MatPaginator } from '@angular/material';
import { AddProductComponent } from './add-product/add-product.component';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
  providers: [ProductService]
})
export class ProductComponent implements OnInit {

  title = 'Products';
  products: Product[] = [];
  router: any;
  constructor(private productService: ProductService, private dialog: MatDialog) { }

  ngOnInit() {
    this.productService.getProducts()
    .subscribe( data => {
      this.products = data;
    });
  }

  getProducts() {
    this.productService.getProducts().subscribe(res => {
      this.products = res;
    });
  }
  deleteProduct(product: Product): void {
    this.productService.deleteProduct(product.id)
      .subscribe( data => {
        this.products = this.products.filter(u => u !== product);
      });
  }

  // editProduct(product: Product): void {
  //   window.localStorage.removeItem('editProductId');
  //   window.localStorage.setItem('editProductId', product.id.toString());
  //   this.router.navigate(['edit-product']);
  // }

  onCreate() {
    this.productService.initializeFormGroup();
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = '60%';
    this.dialog.open(AddProductComponent, dialogConfig);
  }


}
