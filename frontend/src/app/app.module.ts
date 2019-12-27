import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Globals } from './global';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ParentsComponent } from './scheduler/parents/parents.component';
import { ComponentsComponent } from './scheduler/components/components.component';
import { SuppliersComponent } from './scheduler/suppliers/suppliers.component';
import { PlansComponent } from './scheduler/plans/plans.component';
import { HttpClientModule } from 'node_modules/@angular/common/http';
import { EmailComponent } from './scheduler/email/email.component';
import { ComponentsuppliersComponent } from './scheduler/componentsuppliers/componentsuppliers.component';
import { NgxSpinnerModule } from 'ngx-spinner';

@NgModule({
  declarations: [
    AppComponent,
    ParentsComponent,
    ComponentsComponent,
    SuppliersComponent,
    PlansComponent,
    EmailComponent,
    ComponentsuppliersComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgxSpinnerModule
  ],
  providers: [Globals],
  bootstrap: [AppComponent]
})
export class AppModule { 
}
