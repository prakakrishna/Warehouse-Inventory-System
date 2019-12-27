import { Component, OnInit, ViewChild } from '@angular/core';
import { SchedulerService } from 'src/app/services/scheduler.service';
import { Router } from 'node_modules/@angular/router';
import * as XLSX from 'xlsx';
import { NgxSpinnerService } from 'ngx-spinner';

declare var $ : any;
@Component({
  selector: 'app-parents',
  templateUrl: './parents.component.html',
  styleUrls: ['./parents.component.scss']
})
export class ParentsComponent implements OnInit {
  public parentData: any = [];
  public missingParent: any = [];
  public dataTable: any;
  public parent_item: any;
  public d1: any = 0;
  public d2: any = 0;
  public d3: any = 0;
  @ViewChild('parentTable') Table;

  public arrayBuffer:any;
  public file:File;
  public uploadData: any = [];
  public dtOptions: any = {};

  constructor(private schedulerService: SchedulerService, private router: Router, private spinner: NgxSpinnerService) { }

  ngOnInit() {
    $("#buttonUpload").prop('disabled', true);

    this.dtOptions = {
      dom: '<"top"Bf>rt<"bottom"lip><"clear">',
      buttons: [
          {
            extend: 'excel',
            text: 'Download',
            title: 'Below are the Parents loaded to the Tool. For any change, please contact the Administrator',
            filename: 'Parent_Items',
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
    this.loadParents();
  }

  onKeyD1(event) {
    this.d1 = event.target.value;
  }
  onKeyD2(event) {
    this.d2 = event.target.value;
  }
  onKeyD3(event) {
    this.d3 = event.target.value;
  }

  loadParents() {
    this.spinner.show();
    this.schedulerService.getParents().subscribe(
      data => {
        this.parentData = data;
        this.dataTable = $(this.Table.nativeElement);
        //this.dataTable.dataTable(this.dtOptions);
        setTimeout(()=>{this.dataTable.dataTable(this.dtOptions); this.spinner.hide()}, 1000);
      }
    );
  }

  addtoPlan(id) {
    this.parent_item = id;
    this.d1 = this.d2 = this.d3 = 0;
      var status = $('#popUp').modal('show');
  }
  save() {
    this.schedulerService.addtoPlan(this.parent_item, this.d1, this.d2, this.d3);
    //console.log(this.schedulerService.getPlan());
  }

  incomingfile(event) 
  {
    this.file = event.target.files[0];
    $("#buttonUpload").prop('disabled', false);
  }

  Upload() {
    let fileReader = new FileReader();
    var pData = new Array();
    //var missingParent = new Array();
    for(var i = 0; i != this.parentData.length; ++i) pData[i] = this.parentData[i]['parent_item'];
    var j = 0;
    fileReader.onload = (e) => {
      this.arrayBuffer = fileReader.result;
      var data = new Uint8Array(this.arrayBuffer);
      var arr = new Array();
      for(var i = 0; i != data.length; ++i) arr[i] = String.fromCharCode(data[i]);
      var bstr = arr.join("");
      var workbook = XLSX.read(bstr, {type:"binary"});
      var first_sheet_name = workbook.SheetNames[0];
      var worksheet = workbook.Sheets[first_sheet_name];
      //console.log(XLSX.utils.sheet_to_json(worksheet,{raw:true}));
      this.uploadData = XLSX.utils.sheet_to_json(worksheet,{raw:true});
      this.uploadData.forEach(element => {
        if(pData.indexOf(element['Parent Item']) > 0) {
          this.schedulerService.addtoPlan(element['Parent Item'], element['D1'], element['D2'], element['D3']);
        }
        else {
          if(this.missingParent.indexOf(element['Parent Item']) < 0)  this.missingParent[j++] = element['Parent Item'];
        }
      });
      if(this.missingParent.length > 0) {
        $('#missingPopUp').modal('show');
      }
      else{
        this.spinner.show();
        setTimeout(()=>{this.router.navigate(['email']);this.spinner.hide();}, 500);
      }
    }
    if (this.file) {
      fileReader.readAsArrayBuffer(this.file);
      this.file = null;
      $("#fileUpload").val(null);
    }
    
    $("#buttonUpload").prop('disabled', true);    
  }
}