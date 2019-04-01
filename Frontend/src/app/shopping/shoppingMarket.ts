import { shoppingProduct } from "./shoppingProduct";

export class shoppingMarket {
  name: string;
  x: number;
  y: number;
  z: number;
  products: Array<shoppingProduct> = [];

  constructor() {
    this.name = "PlaceHolderMarket";
    this.x = 100;
    this.y = 200;
    this.z = 300;
  }
  // Add product to this market available products
  addProduct(product: shoppingProduct) {
    let shouldReturn = false;
    if (this.products.includes(product)) {
      console.log(
        "You have added duplicate products to same market: " + product.name
      );
      return;
    } else {
      this.products.forEach(element => {
        if (product.name == element.name) {
          console.log(
            "You have added duplicate products to same market, not possible: " +
              product.name +
              " , " +
              this.name
          );
          shouldReturn = true;
          return;
        }
      });

      if (shouldReturn == false) {
        //console.log("Adding: " + product.name +" to " + this.name);
        this.products.push(product);
        product.addToAvailableMarketList(this);
      }

      shouldReturn = false;
    }
  }
  //Find and Return product which is given by name, or return NULL | NULL REFERANCE POSSIBLE
  findProductInMarket(productName: string): shoppingProduct {
    let productToReturn: shoppingProduct = null;
    if (this.products != null) {
      this.products.forEach(product => {
        if (product.name == productName) {
          productToReturn = product;
        }
      });
    }
    return productToReturn;
  }
}
