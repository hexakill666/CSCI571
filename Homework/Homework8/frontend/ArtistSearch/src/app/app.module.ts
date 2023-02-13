import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { TopbarComponent } from './views/topbar/topbar.component';
import { BottombarComponent } from './views/bottombar/bottombar.component';
import { SearchallComponent } from './views/searchall/searchall.component';

@NgModule({
  declarations: [
    AppComponent,
    TopbarComponent,
    BottombarComponent,
    SearchallComponent
  ],
  imports: [
    BrowserModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
