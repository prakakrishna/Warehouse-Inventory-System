import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { SchedulerService } from 'src/app/services/scheduler.service';
import { NgxSpinnerService } from 'ngx-spinner';

declare var $;

@Component({
  selector: 'app-componentsuppliers',
  templateUrl: './componentsuppliers.component.html',
  styleUrls: ['./componentsuppliers.component.scss']
})
export class ComponentsuppliersComponent implements OnInit {
  public componentSuppliersData: any = [];
  public dataTable: any;
  public dtOptions: any = {};
  @ViewChild('componentSuppliersTable') Table;

  constructor(private schedulerService: SchedulerService, private router: Router, private spinner: NgxSpinnerService) { 
  }

  ngOnInit() {
    this.dtOptions = {
      dom: '<"top"Bf>rt<"bottom"lip><"clear">',
      buttons: [
          {
            extend: 'excel',
            text: 'Download',
            title: 'Below are the Component Suppliers loaded to the Tool. For any change, please contact the Administrator',
            filename: 'Component_Suppliers',
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
    this.loadComponentSuppliers();
  }

  loadComponentSuppliers() {
    this.spinner.show();
    this.schedulerService.getComponentSuppliers().subscribe(
      data => {
        this.componentSuppliersData = data;
        this.dataTable = $(this.Table.nativeElement);
        setTimeout(()=>{this.dataTable.dataTable(this.dtOptions); this.spinner.hide()}, 1000);
      }
    );
  }
}
