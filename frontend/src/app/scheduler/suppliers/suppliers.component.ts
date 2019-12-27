import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SchedulerService } from 'src/app/services/scheduler.service';
import { NgxSpinnerService } from 'ngx-spinner';

declare var $;
@Component({
  selector: 'app-suppliers',
  templateUrl: './suppliers.component.html',
  styleUrls: ['./suppliers.component.scss']
})
export class SuppliersComponent implements OnInit {
  public supplierData: any = [];
  public dataTable: any;
  public email: any = "";
  public index: any = 0;
  public dtOptions: any = {};

  @ViewChild('supplierTable') Table;

  constructor(private schedulerService: SchedulerService, private router: Router, private spinner: NgxSpinnerService) { 
  }

  ngOnInit() {
    this.dtOptions = {
      dom: '<"top"Bf>rt<"bottom"lip><"clear">',
      buttons: [
          {
            extend: 'excel',
            text: 'Download',
            filename: 'Suppliers',
            title: 'Below are the Suppliers loaded to the Tool. For any change, please contact the Administrator',
            exportOptions: {
              columns: ":visible"
            }
          },
          {
            extend: 'colvis',
            text: 'Columns'
          }
      ]
    };
    this.loadSuppliers();
  }

  loadSuppliers() {
    this.spinner.show();

    this.schedulerService.getSuppliers().subscribe(
      data => {
        this.supplierData = data;
        this.dataTable = $(this.Table.nativeElement);
        setTimeout(()=>{this.dataTable.dataTable(this.dtOptions); this.spinner.hide(); }, 10);
      }
    );
  }

  editEmail(i, id) {
    this.index = i;
    this.email = this.supplierData[i].email;
    $('#editEmail').modal('show');
  }
  onKeyEmail(event) {
    this.email = event.target.value;
  }
  saveEmail() {
    this.spinner.show();
    this.schedulerService.updateSupplier(this.supplierData[this.index].supplier, this.email).subscribe(result => {
      //console.log(result);
      this.supplierData = result;
      this.dataTable = $(this.supplierData.nativeElement);
      setTimeout(()=>{this.dataTable.dataTable(); this.spinner.hide();}, 10);
    });
    this.loadSuppliers();
  }
}
