import { shoppingMarket } from './shoppingMarket';

export class shoppingProduct {
    name:   string;
    price: number;
    image: string;
    selectedMarket: shoppingMarket;
    availableMarkets: Array <shoppingMarket>;
    stock:    number;
    selectedQuantity:   number;
    quantity: number;
    categories:string[];

    constructor() { 
        this.name ="PlaceHolderProduct";
        this.price = 0;
        this.image = "https://via.placeholder.com/150";
        this.selectedMarket = null; //Possible Null Referance
        this.stock = 0;
        this.quantity = 1;
        this.selectedQuantity = 1;
        this.categories = ["NaN"];

    }

    

    //Change Selected Quantity
    changeSelectedQuantity(num:number){
        this.selectedQuantity = num;
    }
    changeSelectedMarket(market: shoppingMarket){
        this.selectedMarket = market;

    }
    addToAvailableMarketList(market:shoppingMarket){
        if(this.availableMarkets == null){
            this.availableMarkets = [];
        }
        if(!this.availableMarkets.includes(market)){
            this.availableMarkets.push(market);
        }

    }
 


  }