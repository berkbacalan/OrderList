import { Component, OnInit } from "@angular/core";
import { AngularFireAuth } from "@angular/fire/auth";
import { AngularFireDatabase } from "@angular/fire/database";

import { Observable } from "rxjs";
import { stringify } from "@angular/core/src/render3/util";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit {
  email: string = "";
  password: string = "";
  adress: string = "";
  adress2: string = "";
  city: string = "";
  state: string = "";
  zip: string = "";
  item: Observable<any>;
  loggedIn: boolean = false;

  currentEmail: string = "";
  currentAdress: string = "";
  currentAdress2: string = "";
  currentCity: string = "";
  currentState: string = "";
  currentZip: string = "";

  asd:number;
  constructor(public afAuth: AngularFireAuth, public db: AngularFireDatabase) {

  }

ngOnInit(){

}



  async createUser(
    email: string,
    password: string,
    adress: string,
    adress2: string,
    city: string,
    state: string,
    zip: string
  ) {
    var shouldOpenRecord = true;
    await this.afAuth.auth
      .createUserWithEmailAndPassword(email, password)
      .catch(function(error) {
        console.log(error);
        shouldOpenRecord = false;
      });
    if (shouldOpenRecord) {
      const itemRef = this.db.object(
        this.afAuth.auth.currentUser.uid + "/email"
      );
      itemRef.set(email);
      const itemRef2 = this.db.object(
        this.afAuth.auth.currentUser.uid + "/adress"
      );
      itemRef2.set(adress);
      const itemRef3 = this.db.object(
        this.afAuth.auth.currentUser.uid + "/adress2"
      );
      itemRef3.set(adress2);
      const itemRef4 = this.db.object(
        this.afAuth.auth.currentUser.uid + "/city"
      );
      itemRef4.set(city);
      const itemRef5 = this.db.object(
        this.afAuth.auth.currentUser.uid + "/state"
      );
      itemRef5.set(state);
      const itemRef6 = this.db.object(
        this.afAuth.auth.currentUser.uid + "/zip"
      );
      itemRef6.set(zip);
    }
  }

  async getCurrentUserData() {
    if(!this.afAuth.auth.currentUser || this.afAuth == null){
      console.log("No Logged in user");
      return;
    }
    var currentEmail;
   await  this.db.database
      .ref(this.afAuth.auth.currentUser.uid + "/email")
      .once("value")
      .then(function(snapshot) {
        currentEmail = snapshot.val();
        // ...
      });
      var currentAdress;
    await this.db.database
      .ref(this.afAuth.auth.currentUser.uid + "/adress")
      .once("value")
      .then(function(snapshot) {
        currentAdress = snapshot.val();
        // ...
      });
      var currentAdress2;

    await this.db.database
      .ref(this.afAuth.auth.currentUser.uid + "/adress2")
      .once("value")
      .then(function(snapshot) {
        currentAdress2 = snapshot.val();
        // ...
      });
      var currentCity;

    await this.db.database
      .ref(this.afAuth.auth.currentUser.uid + "/city")
      .once("value")
      .then(function(snapshot) {
        currentCity = snapshot.val();
        // ...
      });
      var currentState;

    await this.db.database
      .ref(this.afAuth.auth.currentUser.uid + "/state")
      .once("value")
      .then(function(snapshot) {
        currentState = snapshot.val();
        // ...
      });
      var currentZip;

    await this.db.database
      .ref(this.afAuth.auth.currentUser.uid + "/zip")
      .once("value")
      .then(function(snapshot) {
        currentZip = snapshot.val();
        // ...
      });
      this.currentAdress = currentAdress;
      
      this.currentAdress2 = currentAdress2;
      this.currentCity = currentCity;
      this.currentEmail = currentEmail;
      this.currentState = currentState;
      this.currentZip = currentZip;
  }

  login(username: string, psswrd: string) {
    this.afAuth.auth
      .signInWithEmailAndPassword(username, psswrd)
      .catch(function(error) {
        console.log(error);
      });
    /*console.log(
      "Currently Logged in: " + this.afAuth.auth.currentUser.email.toString()
    );*/
  }
  logout() {
    this.afAuth.auth.signOut();
    console.log("Logged Out Successfully");
  }
}
