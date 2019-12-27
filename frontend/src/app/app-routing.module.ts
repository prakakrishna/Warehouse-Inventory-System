import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ParentsComponent } from './scheduler/parents/parents.component';
import { ComponentsComponent } from './scheduler/components/components.component';
import { SuppliersComponent } from './scheduler/suppliers/suppliers.component';
import { PlansComponent } from './scheduler/plans/plans.component';
import { EmailComponent } from './scheduler/email/email.component';
import { ComponentsuppliersComponent } from './scheduler/componentsuppliers/componentsuppliers.component';

const routes: Routes = [
  {path: '', component: ParentsComponent},
  {path: 'parents', component: ParentsComponent},
  {path: 'components', component: ComponentsComponent},
  {path: 'suppliers', component: SuppliersComponent},
  {path: 'plans', component: PlansComponent},
  {path: 'componentsuppliers', component: ComponentsuppliersComponent},
  {path: 'email', component: EmailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
