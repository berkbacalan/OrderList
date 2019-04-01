import { Component, OnInit, ViewChild } from "@angular/core";
import { shoppingProduct } from "./shoppingProduct";
import { shoppingMarket } from "./shoppingMarket";
import {
  MatPaginator,
  MatTableDataSource,
  MatFormFieldControl,
  MatSort
} from "@angular/material";
import { SelectionModel, DataSource } from "@angular/cdk/collections";
import { DatabaseService } from './database.service';
import { AngularFireAuth } from '@angular/fire/auth';
import { auth } from 'firebase';

@Component({
  selector: "app-shopping",
  templateUrl: "./shopping.component.html",
  styleUrls: ["./shopping.component.css"],
  providers: [DatabaseService]

})
export class ShoppingComponent implements OnInit {
  marketList: Array<shoppingMarket> = []; //All available markets list
  orderedProductList: Array<shoppingProduct> = []; //Ordered product list which will be visible by end user. There won't be duplicate products in this list.

  myCart: Array<shoppingProduct> = []; // Current MyCart list
  myCartPrice: number = 0;
  improvedCart: Array<shoppingProduct> = [];
  improvedCartPossibleCheapestPrice: number = 0;
  cheapestMarket: shoppingMarket;

  userPosition: Position;
  nearestMarket: shoppingMarket;

  email: string = "";
  password: string = "";
  

  dataSource = new MatTableDataSource<shoppingProduct>(this.orderedProductList); //Data Source for HTML TABLE
  myCartDataSource = new MatTableDataSource<shoppingProduct>(this.myCart); //Data source for Mycart

  //List of which columns will be displayed at User Shopping List
  displayedColumns: string[] = [
    "image",
    "name",
    "price",
    "market",
    "quantity",
    "select"
  ];
  // Angular Data List options
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  selection = new SelectionModel<shoppingProduct>(true, []);

  products: shoppingProduct[] = [];
  constructor(private productService: DatabaseService,
              public afAuth: AngularFireAuth) { }

  testMarket:shoppingMarket;

  login(username:string,psswrd:string) {
    console.log("Username: " + username + " Password: " + psswrd);
    this.afAuth.auth.signInWithEmailAndPassword(username,psswrd).catch(function(error){
      console.log(error);
    });
    console.log("Currently Logged in: " + this.afAuth.auth.currentUser.displayName);
  }
  logout() {
    this.afAuth.auth.signOut();
  }


  // Method which will be inited at startup
  ngOnInit() {
    this.dataSource.paginator = this.paginator; //Enable pagination of Product List
    this.myCartDataSource.paginator = this.paginator; // Enable pagination of My Cart
    this.generateTestData(); //Generate Test Data
    this.getProductsFromDatabase(); // Get Product Data from PostgreSql
    
    this.findMe(); //Get User Location
    this.populateOrderedProduct(); //Make OrderedProduct List, Remove duplicate products.
  }

  async getProductsFromDatabase(){
    this.testMarket =  new shoppingMarket();
    await this.productService.getProducts()
    .subscribe( data => {
      this.products = data;
      for(let i = 0 ; i<this.products.length; i++){
        var tempProduct:shoppingProduct = new shoppingProduct();
        tempProduct.name = this.products[i].name;
        tempProduct.price = this.products[i].price;
        tempProduct.stock = this.products[i].stock;
        this.testMarket.addProduct(tempProduct);
      }
      this.dataSource._updateChangeSubscription();
      this.marketList.push(this.testMarket);
      this.populateOrderedProduct(); //Make OrderedProduct List, Remove duplicate products.

    });
      this.dataSource._updateChangeSubscription();


  }

  getProducts() {
    this.productService.getProducts().subscribe(result => {
      this.products = result;
    });
  }

  //Add Product To MyCart
  addToCart(newProduct: shoppingProduct) {
    if (newProduct.selectedMarket == null) {
      console.log("Please choose market.");
      return;
    }
    if (newProduct.selectedQuantity == null) {
      newProduct.selectedQuantity = 1;
    }
    if (this.myCart.includes(newProduct)) {
      for (let i = 0; i < newProduct.selectedQuantity; i++) {
        this.myCart[this.myCart.indexOf(newProduct)].quantity++;
      }
    } else {
      newProduct.quantity = newProduct.selectedQuantity;
      this.myCart.push(newProduct);
    }

    this.calculateMyCartPrice();
    this.calculateCheapestMarket();
    this.myCartDataSource._updateChangeSubscription();
  }

  calculateMyCartPrice() {
    this.myCartPrice = 0;
    for (let i = 0; i < this.myCart.length; i++) {
      let subtotal = 0;
      if (this.myCart[i].quantity == null) {
        this.myCart[i].quantity = 1;
      }
      for (let j = 0; j < this.myCart[i].quantity; j++) {
        subtotal += this.myCart[i].price;
      }
      this.myCartPrice += subtotal;
    }
  }
  //Checkout sequence
  checkout() {
    
    for (var j = 0; j < this.myCart.length; j++) {
      this.removeFromCart(this.myCart[j]);
    }
    for (var j = 0; j < this.myCart.length; j++) {
      this.removeFromCart(this.myCart[j]);
    }
    this.calculateMyCartPrice();
    this.calculateCheapestMarket();
    this.myCartDataSource._updateChangeSubscription();
  }


  calculateCheapestMarket() {
    var matchedItemCount = 0;
    var myCartItemCount = this.myCart.length;
    var subTotalMarketPriceCounts: number[] = [];
    var subTotalPrice = 0;
    var minPriceForCart = 0;
    var indexNumberOfCheapestMarket = 0;
    if(this.myCart.length <1){
      this.improvedCartPossibleCheapestPrice = 0;
      return;
    }
    //Cross referance my cart with all products and assign prices for market total. Assign this value to array's corresponding index
    for (let i = 0; i < this.marketList.length; i++) {
      //All markets

      for (let j = 0; j < this.marketList[i].products.length; j++) {
        // All Products of markets

        for (let k = 0; k < this.myCart.length; k++) {
          // My Products

          if (this.marketList[i].products[j].name == this.myCart[k].name) {
            subTotalPrice +=
              this.marketList[i].products[j].price * this.myCart[k].quantity;
            matchedItemCount++;
          }
        }
      }
      if (matchedItemCount == myCartItemCount) {
        subTotalMarketPriceCounts.push(subTotalPrice);
      } else {
        subTotalMarketPriceCounts.push(-1);
      }
      subTotalPrice = 0;
      matchedItemCount = 0;
    }

    console.log("Array: " + subTotalMarketPriceCounts.toString());

    var tempPriceOfMarkets: number[] = [...subTotalMarketPriceCounts];
    var sortedPriceOfMarkets: number[] = tempPriceOfMarkets.sort(
      (n1, n2) => n1 - n2
    );

    for (var m = 0; m < sortedPriceOfMarkets.length; m++) {
      // Find Minumum possible price for cart
      if (sortedPriceOfMarkets[m] == -1) {
        continue;
      }
      if (sortedPriceOfMarkets[m] > 0) {
        minPriceForCart = sortedPriceOfMarkets[m];
        break;
      }
    }
    if (Math.max.apply(Math, sortedPriceOfMarkets) <= 0) {
      console.error("NO SUCH MARKET EXIST, REMOVING LAST PRODUCT"); // NEED SOLUTION FOR THIS CONDITION
      //this.removeFromCart(this.myCart[this.myCart.length - 1]);
      this.myCartDataSource._updateChangeSubscription();
      return;
    }
    indexNumberOfCheapestMarket = subTotalMarketPriceCounts.indexOf(
      minPriceForCart
    );
    this.cheapestMarket = this.marketList[indexNumberOfCheapestMarket];
    this.improvedCartPossibleCheapestPrice = minPriceForCart;
    console.log("Prices: " + subTotalMarketPriceCounts.toString());
    console.log(
      "Cheapest Market:" +
        this.cheapestMarket.name +
        " Price: " +
        minPriceForCart
    );
  }

  /*setToCheapestMarket() {
    var priceOfMarkets: number[] = [];
    var subTotalPrice = 0;
    var myCartItemCount = this.myCart.length;
    var matchedItemCount = 0;
    var minumumPriceForCart = 0;
    var indexNumberOfCheapestMarket = 0;
    for (var i = 0; i < this.marketList.length; i++) {
      // Kartımdaki ürünler için bütün marketlerden fiyat al. 1 tane bile ürün yoksa -1 olsun.

      for (var j = 0; j < this.myCart.length; j++) {
        // Bu markette, sepetimdeki ürünleri ara, varsa  toplam fiyatını cıkar.

        for (var k = 0; k < this.marketList[i].products.length; k++) {
          if (this.marketList[i].products[k].name == this.myCart[j].name) {
            subTotalPrice += this.marketList[i].products[k].price ;
            matchedItemCount++;
          }
        }
      }
      if (matchedItemCount == myCartItemCount) {
        // Bu markette, kartımdaki bütün ürünler var ise
        priceOfMarkets.push(subTotalPrice);
      } else {
        priceOfMarkets.push(-1); // Bu markette bütün kartımdaki ürünler yoksa, marketin toplam fiyatına -1 ata.
      }

      matchedItemCount = 0; // Reset Matched Item for next market
      subTotalPrice = 0;
    }

    var tempPriceOfMarkets: number[] = [...priceOfMarkets];
    var sortedPriceOfMarkets: number[] = tempPriceOfMarkets.sort(
      (n1, n2) => n1 - n2
    );

    for (var m = 0; m < sortedPriceOfMarkets.length; m++) {
      // Find Minumum possible price for cart
      if (sortedPriceOfMarkets[m] == -1) {
        continue;
      }
      if (sortedPriceOfMarkets[m] > 0) {
        minumumPriceForCart = sortedPriceOfMarkets[m];
        break;
      }
    }
    if (Math.max.apply(Math, sortedPriceOfMarkets) <= 0) {
      console.error("NO SUCH MARKET EXIST, REMOVING LAST PRODUCT"); // Remove Last Added Product
      this.removeFromCart(this.myCart[this.myCart.length - 1]);
      this.myCartDataSource._updateChangeSubscription();
      return;
    }

    indexNumberOfCheapestMarket = priceOfMarkets.indexOf(minumumPriceForCart);
    this.cheapestMarket = this.marketList[indexNumberOfCheapestMarket];
    this.improvedCartPossibleCheapestPrice = minumumPriceForCart;
    console.log("Prices: " + priceOfMarkets.toString());
    console.log(
      "Cheapest Market:" +
        this.cheapestMarket.name +
        " Price: " +
        minumumPriceForCart
    );
  }*/
  removeFromCart(productToRemove: shoppingProduct) {
    var index = this.myCart.indexOf(productToRemove);
    if (this.myCart[index].quantity != null) {
      this.myCart[index].quantity = 1;
    }
    console.log("Index: " + index);
    this.myCart.splice(index, 1);
    /*if (this.myCart.length == 0) {
      this.myCartPrice = 0;
      this.improvedCartPossibleCheapestPrice = 0;
      return;
    }*/
    this.calculateMyCartPrice();
    this.calculateCheapestMarket();
    this.myCartDataSource._updateChangeSubscription();
  }

  //Remote old product from ordered list and add new product to list which is selected.
  changeProductInformationWhichIsSelected(product: shoppingProduct) {
    let newProduct: shoppingProduct = this.marketList[
      this.marketList.indexOf(product.selectedMarket)
    ].findProductInMarket(product.name);
    if (newProduct != null) {
      this.orderedProductList.splice(
        this.orderedProductList.indexOf(product),
        1,
        newProduct
      );
      newProduct.selectedMarket = product.selectedMarket;
      for (let i = 0; i < product.availableMarkets.length; i++) {
        newProduct.addToAvailableMarketList(product.availableMarkets[i]);
      }
    }
    this.dataSource._updateChangeSubscription(); //Update Datasource according to the array changes
  }

  //Remove duplicate Products from list which is showed to end user
  populateOrderedProduct() {
    /*this.marketList.forEach(element => {
      element.products.forEach(product => {
        if (this.orderedProductList.includes(product)) {
          return;
        } else {
          if (this.includeProduct(product)) {
            console.log("Includes");

            let temp: shoppingProduct = this.orderedProductList.splice[
              this.orderedProductList.length - 1
            ];

            if (product.price < temp.price) {
              this.orderedProductList.push(product);
            } else {
              this.orderedProductList.push(temp);
            }
          }
          else {
            this.orderedProductList.push(product);
          }
        }
      });
    });*/

    for (let i = 0; i < this.marketList.length; i++) {
      for (let j = 0; j < this.marketList[i].products.length; j++) {
        if (this.includedInOrderedList(this.marketList[i].products[j])) {
          //IF NAMES ARE SAME
          //console.log("Names are same");
          let index = 0;
          this.orderedProductList.forEach(element => {
            if (this.marketList[i].products[j].name == element.name) {
              index = this.orderedProductList.indexOf(element);
            }
          });

          this.orderedProductList[index].addToAvailableMarketList(
            this.marketList[i]
          );
        } else {
          //console.log("Names are different, ADDING");
          this.orderedProductList.push(this.marketList[i].products[j]);
        }

        this.dataSource._updateChangeSubscription();
      }
    }
  }
  // Check if orderedProductList already includes x.
  includedInOrderedList(search: shoppingProduct): boolean {
    let value = false;
    this.orderedProductList.forEach(product => {
      if (search.name == product.name) {
        value = true;
      }
    });
    return value;
  }

  // Generate Data for Testing purposes.
  generateTestData() {
    //Create Test Market
    let m1 = new shoppingMarket();
    m1.name = "Test Market 1";
    m1.x = 5000;
    m1.y = 7000;
    m1.z = 8000;
    //Create Test Market
    let m2 = new shoppingMarket();
    m2.name = "Test Market 2";
    m2.x = 2000;
    m2.y = 3000;
    m2.z = 1000;
    let m3 = new shoppingMarket();
    m3.name = "Test Market 3";
    m3.x = 2000;
    m3.y = 3000;
    m3.z = 1000;
    //Create Test Product
    let p1 = new shoppingProduct();
    p1.name = "Test Product 1";
    p1.price = 50;
    let p2 = new shoppingProduct();
    p2.name = "Test Product 2";
    p2.price = 100;
    let p3 = new shoppingProduct();
    p3.name = "Test Product 1";
    p3.price = 75;
    //Add Test product to Test Market
    m1.addProduct(p1);
    m2.addProduct(p1);
    m2.addProduct(p2);
    m3.addProduct(p3);
    //Push Market to marketList
    this.marketList.push(m1);
    this.marketList.push(m2);
    this.marketList.push(m3);
  }
  // Use Geo Location to pinpoint user ( ITS NOT ACCURATE for WEB !)
  findMe() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        position => {
          this.userPosition = position;
          this.nearestMarket = this.findNearestMarket(
            position.coords.latitude,
            position.coords.longitude
          );
        },
        error => {},
        { enableHighAccuracy: true }
      );
    } else {
      alert("Geolocation is not supported by this browser.");
    }
  }
  // find closest market to user by calculating distance between user and all available markets.
  findNearestMarket(lat: number, longi: number): shoppingMarket {
    let minMarket: shoppingMarket;
    let minDistance: number = 9999999999; //BIG NUMBER

    for (let market of this.marketList) {
      let distance: number = Math.sqrt(
        (market.x - lat) ** 2 + (market.y - longi) ** 2
      );
      if (distance < minDistance) {
        minDistance = distance;
        minMarket = market;
      }
    }

    return minMarket;
  }
}
