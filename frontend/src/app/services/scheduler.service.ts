import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Globals} from '../global'
import * as XLSX from 'xlsx';

@Injectable({
  providedIn: 'root'
})
export class SchedulerService {
  //public row = 0;
  constructor(private http: HttpClient, private globals: Globals) { }
  public url = 'http://localhost:8080/scheduler/';

  getComponents() {
    return this.http.get(this.url + 'getComponents');
  }
  getParents() {
    return this.http.get(this.url + 'getParents');
  }
  getSuppliers() {
    return this.http.get(this.url + 'getSuppliers');
  }
  getPlans(planData) {
    return this.http.post(this.url + 'getPlans', planData);
  }
  getAllPlans() {
    return this.http.get(this.url + 'getAllPlans');
  }
  getComponentSuppliers() {
    return this.http.get(this.url + 'getChildSuppliers');
  }
  getPlanBySuppliers(email_id) {
    return this.http.get(this.url + 'getPlanBySuppliers?email_id=' + email_id);
  }
  addtoPlan(id, d1, d2, d3) {
    this.globals.plan[this.globals.row] = {"id": id, "d1" : d1, "d2" : d2, "d3" : d3};
    this.globals.row = this.globals.row + 1;
    //console.log(this.globals.plan);
  }
  resetPlan() {
    this.globals.row = 0;
    this.globals.plan = [];
  }
  getPlan() {
    //console.log(this.globals.plan);
    return this.globals.plan;
  }
  stagePlan(data) {
    return this.http.post(this.url + 'stagePlan', data, {reportProgress: true});
  }
  sendPlan(id) {
    var data : any = {"id": id};
    return this.http.post(this.url + 'sendPlan', data);
  }
  updatePlan(id, d1, d2, d3) {
    var data : any = {"id" : id, "d1": d1, "d2" : d2, "d3" : d3};
    //var data : any = {email_id, child_id, supplier_id, d1, d2, d3};
    return this.http.post(this.url + 'updatePlan', data);
  }
  deletePlan(id) {
    var data : any = {"id": id};
    //var data : any = {email_id, child_id, supplier_id, d1, d2, d3};
    return this.http.post(this.url + 'deletePlan', data);
  }

  updateSupplier(id, email) {
    var data : any = {"id" : id, "email": email};
    return this.http.post(this.url + 'updateSupplier', data);
  }

  exportExcel(data: any[], filename: any){
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(data);
    const workbook: XLSX.WorkBook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
    XLSX.writeFile(workbook, filename, { bookType: 'xlsx', type: 'buffer' });
  }
}
