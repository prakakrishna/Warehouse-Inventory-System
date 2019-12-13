package com.hanon.scheduler;

import com.hanon.scheduler.services.*;
import com.hanon.scheduler.table.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/scheduler")
@CrossOrigin(origins = "*")
public class Controller {
    @Autowired
    ParentService parentService;
    @Autowired
    ChildService childService;
    @Autowired
    SupplierService supplierService;
    @Autowired
    PlanService planService;
    @Autowired
    UploadService uploadService;
    @Autowired
    EmailService emailService;
    @Autowired
    EmailPlanService emailPlanService;
    @Autowired
    ChildSupplierService childSupplierService;
    @Autowired
    private JavaMailSender sender;

    private ArrayList<String> missingList = new ArrayList<String>();

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam(defaultValue="File") String description) throws Exception, IOException, InvalidFormatException {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Path tempDir = Files.createTempDirectory("");
        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);
        Workbook workbook = WorkbookFactory.create(new File(String.valueOf(tempFile)));

        UploadTable uploadTable = new UploadTable();
        uploadTable.setDescription(description);
        uploadTable.setCreated_date(new Date());
        uploadService.create(uploadTable);
        Long upload_id = uploadTable.getId();

        AtomicReference<ParentTable> parent = new AtomicReference<>(new ParentTable());
        parent.get().setIs_active('Y');
        parent.get().setCreated_date(new Date());

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter formatter = new DataFormatter();
        AtomicReference<String> parent_item = new AtomicReference<>(String.valueOf("default"));
        AtomicReference<Long> num = new AtomicReference<>(Long.valueOf(0));

        for (Row row : sheet) {
            AtomicReference<SupplierTable> supplier = new AtomicReference<>(new SupplierTable());
            supplier.get().setIs_active('Y');
            supplier.get().setCreated_date(new Date());
            supplier.get().setEmail("");
            supplier.get().setName("");
            supplier.get().setSupplier("");

            AtomicReference<ChildTable> child = new AtomicReference<>(new ChildTable());
            child.get().setIs_active('Y');
            child.get().setCreated_date(new Date());

            PlanTable plan = new PlanTable();
            plan.setUpload_id(upload_id.intValue());
            plan.setIs_active('Y');
            plan.setCreated_date(new Date());
            if (row.getRowNum() == 0) {
                continue;
            }
            row.forEach(cell -> {
                switch (cell.getColumnIndex()) {
                    case 0:
                        //plan.setSeq(Math.round(cell.getNumericCellValue()));
                        num.getAndSet(num.get() + 1);
                        plan.setSeq(num.get());
                        break;
                    case 1:
                        String value = cell.getStringCellValue();
                        if(value.length() > 0) {
                            parent_item.set(value);
                        }
                        parent.get().setParent_item(parent_item.get());
                        break;
                    case 2:
                        parent.get().setEnd_item_desc(cell.getStringCellValue());
                        break;
                    case 5:
                        plan.setLevel(cell.getStringCellValue());
                        break;
                    case 6:
                        child.get().setComponent(cell.getStringCellValue());
                        break;
                    case 7:
                        child.get().setComponent_desc(cell.getStringCellValue());
                        break;
                    case 8:
                        if ( cell.getCellTypeEnum() == CellType.STRING ) {
                            child.get().setComponent2_desc(cell.getStringCellValue());
                        }
                        else {
                            plan.setUsage(cell.getNumericCellValue());
                        }
                        break;
                    case 9:
                        plan.setUsage(cell.getNumericCellValue());
                        break;
                    case 11:
                        child.get().setUom(cell.getStringCellValue());
                        break;
                    case 17:
                        child.get().setItem_type(cell.getStringCellValue());
                        break;
                    case 20:
                        child.get().setCost_total(cell.getNumericCellValue());
                        break;
                    case 23:
                        supplier.get().setSupplier(cell.getStringCellValue());
                        break;
                    case 24:
                        supplier.get().setName(cell.getStringCellValue());
                        break;
                    case 25:
                        //System.out.println(cell.getStringCellValue());
                        //supplier.get().setEmail(cell.getStringCellValue());
                        supplier.get().setEmail("prakakrishna@gmail.com");
                        break;
                }
            });
            if(parentService.isParentItemExists(parent_item.get()) == 0) {
                parentService.create(parent.get());
            }
            parent.set(parentService.getByParentItem(parent_item.get()));

            if(childService.isComponentExists(child.get().getComponent()) == 0) {
                childService.create(child.get());
            }
            ChildTable tChild = childService.getByComponent(child.get().getComponent());
            child.set(tChild);

            plan.setParent_id(parent.get().getId());
            plan.setChild_id(child.get().getId());
            //plan.setSupplier_id(supplier.get().getId());

            //if(planService.isPlanExists(parent.get().getId(), child.get().getId(), plan.getLevel()) == 0) {
                planService.create(plan);
            //}

            if(supplier.get().getSupplier() != null && supplier.get().getSupplier().length() > 0) {
                if (supplierService.isSupplierExists(supplier.get().getSupplier()) == 0) {
                    supplierService.create(supplier.get());
                }
                SupplierTable tSupplier = supplierService.getBySupplier(supplier.get().getSupplier());
                //supplier.set(tSupplier);
                if(childSupplierService.isExists(child.get().getId(), tSupplier.getId()) == 0) {
                    ChildSupplierTable childSupplierTable = new ChildSupplierTable();
                    childSupplierTable.setIs_active('Y');
                    childSupplierTable.setCreated_date(new Date());
                    childSupplierTable.setChild_id(child.get().getId());
                    childSupplierTable.setSupplier_id(tSupplier.getId());
                    childSupplierService.create(childSupplierTable);
                }
                //System.out.println(tSupplier.getEmail());
            }
        }

        // Closing the workbook
        workbook.close();
        return "File Uploaded Successfully";
    }

    @GetMapping("/getUploadIds")
    public List<UploadTable> getUploadIds() throws IOException, InvalidFormatException {
        List<UploadTable> result = uploadService.getAll();
        return result;
    }

    @GetMapping("/getEmailIds")
    public List<EmailTable> getEmailIds() throws IOException, InvalidFormatException {
        List<EmailTable> result = emailService.getAll();
        return result;
    }

    @GetMapping("/getAllPlans")
    public List<PlanTable> getAllPlan() throws IOException, InvalidFormatException {
        List<PlanTable> resultPlan = planService.getAll();
        return resultPlan;
    }

    @PostMapping("/getPlans")
    public List<PlanTable> getPlans(@RequestBody String json) throws IOException, InvalidFormatException {
        ArrayList<Long> parentIds = new ArrayList<Long>();
        JSONArray j = new JSONArray(json);
        for (int i = 0 ; i < j.length(); ++i) {
            JSONObject input = (JSONObject) j.get(i);
            String parent_item = (String) input.get("id");
            System.out.println(parent_item);
            if(parentService.getByParentItem(parent_item)!= null) {
                parentIds.add(parentService.getByParentItem(parent_item).getId());
            }
        }
        List<PlanTable> resultPlan = null;
        if(parentIds.size()>0) {
            resultPlan = planService.getPlanByParentids(parentIds);
        }
        return resultPlan;
    }

    @GetMapping("/getPlan/UploadId")
    public List<PlanTable> getByUploadId(@RequestParam(defaultValue="1") Integer upload_id) throws IOException, InvalidFormatException {
        List<PlanTable> resultPlan = planService.getPlanByUploadId(upload_id);
        return resultPlan;
    }

    @GetMapping("/getPlan/ParentId")
    public List<PlanTable> getByParentId(@RequestParam(defaultValue="1") Long parent_id) throws IOException, InvalidFormatException {
        List<PlanTable> resultPlan = planService.getPlanByParentid(parent_id);
        return resultPlan;
    }

    @GetMapping("/getPlan/ParentId/SupplierId")
    public List<PlanTable> getByParentIdSupplierId(@RequestParam(defaultValue="1") Long parent_id, @RequestParam(defaultValue="1") Long supplier_id) throws IOException, InvalidFormatException {
        List<PlanTable> resultPlan = planService.getPlanByParentIdSupplierId(parent_id, supplier_id);
        return resultPlan;
    }

    @GetMapping("/getPlan/ParentIds/SupplierIds")
    public List<PlanTable> getByParentIdsSupplierIds(@RequestParam() List <Long> parent_ids, @RequestParam() List <Long> supplier_ids) throws IOException, InvalidFormatException {
        List<PlanTable> resultPlan = planService.getPlanByParentIdsSupplierIds(parent_ids, supplier_ids);
        return resultPlan;
    }

    @GetMapping("/getParents")
    public List<ParentTable> getParents() throws IOException, InvalidFormatException {
        List<ParentTable> resultPlan = parentService.getAll();
        return resultPlan;
    }
    @GetMapping("/getParent/Id")
    public ParentTable getParentById(@RequestParam(defaultValue="1") Long parent_id) throws IOException, InvalidFormatException {
        ParentTable result = parentService.getById(parent_id).get();
        return result;
    }

    @GetMapping("/getComponents")
    public List<ChildTable> getComponents() throws IOException, InvalidFormatException {
        List<ChildTable> result = childService.getAll();
        return result;
    }

    @GetMapping("/getSuppliers")
    public List<SupplierTable> getSuppliers() throws IOException, InvalidFormatException {
        List<SupplierTable> result = supplierService.getAll();
        return result;
    }
    @GetMapping("/getSupplier/Id")
    public SupplierTable getSupplierById(@RequestParam(defaultValue="1") Long supplier_id) throws IOException, InvalidFormatException {
        SupplierTable result = supplierService.getById(supplier_id).get();
        return result;
    }
    @GetMapping("/getChildSuppliers")
    public List<ChildSupplierTable> getChildSuppliers() throws IOException, InvalidFormatException {
        List<ChildSupplierTable> result = childSupplierService.getAll();
        return result;
    }
    @PostMapping("/updateSupplier")
    public List<SupplierTable> updateSupplier(@RequestBody String json) throws IOException, InvalidFormatException {
        JSONObject input = new JSONObject(json);
        String id = input.getString("id");
        String email = input.getString("email");
        SupplierTable result = supplierService.getBySupplier(id);
        result.setEmail(email);
        result.setCreated_date(new Date());
        supplierService.update(result);
        return supplierService.getAll();
    }

    @GetMapping("/getPlanBySuppliers")
    public List<EmailPlanTable> getPlanBySuppliers(@RequestParam(defaultValue="1") Long email_id) throws IOException, InvalidFormatException {
        List<EmailPlanTable> result = emailPlanService.getByEmailId(email_id);
        return result;
    }

    /*
    @PostMapping("/sendMail")
    public String sendMail(@RequestParam() List<Long> parent_ids, @RequestParam() List<Long> supplier_ids, @RequestParam() String description) throws IOException, InvalidFormatException {
        EmailTable emailTable = new EmailTable();
        emailTable.setDescription(description);
        emailTable.setCreated_date(new Date());
        emailService.create(emailTable);
        Long email_id = emailTable.getId();

        String[] columns = {"Seq", "Parent Item", "End Item Desc", "D1", "D2", "D3", "Component", "Component Description", "Comp Desc2", "Usage", "UOM", "Item Type", "Cost Total", "Supplier", "Name", "email" };


        //ParentTable parent = parentService.getById(parent_id).get();
        //String parent_item = parent.getParent_item();
        //List<PlanTable> resultPlan = planService.getPlanByParentid (parent_id);

        HashSet<Long> hashsupplier_ids = new HashSet<>();
        for (Long sup: supplier_ids) {
            hashsupplier_ids.add(sup);
        }

        for (Long supplier_id: hashsupplier_ids) {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            List<PlanTable> result = planService.getPlanByParentIdsSupplierId (parent_ids, supplier_id);
            Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet();
            sheet.setDisplayGridlines(false);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.YELLOW.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);

            CellStyle otherCellStyle = workbook.createCellStyle();
            otherCellStyle.setBorderBottom(BorderStyle.THIN);
            otherCellStyle.setBorderLeft(BorderStyle.THIN);
            otherCellStyle.setBorderTop(BorderStyle.THIN);
            otherCellStyle.setBorderRight(BorderStyle.THIN);


            // Create a Row
            Row headerRow = sheet.createRow(0);
            // Create cells
            for(int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Create Cell Style for formatting Date
            //CellStyle dateCellStyle = workbook.createCellStyle();
            //dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int rowNum = 1;
            for (PlanTable res: result) {
                ParentTable parent = parentService.getById(res.getParent_id()).get();
                ChildTable child = childService.findById(res.getChild_id()).get();
                //SupplierTable supplier = supplierService.getById(res.getSupplier_id()).get();

                EmailPlanTable emailPlanTable = new EmailPlanTable();
                //emailPlanTable.setParent_id(res.getParent_id());
                emailPlanTable.setChild_id(res.getChild_id());
                //emailPlanTable.setSupplier_id(res.getSupplier_id());
                emailPlanTable.setCreated_date(new Date());
                //emailPlanTable.setEmail_id(email_id);
                emailPlanTable.setIs_active('Y');
                //emailPlanTable.setSeq(res.getSeq());
                //emailPlanTable.setUpload_id(res.getUpload_id());

                emailPlanService.create(emailPlanTable);

                // Create Other rows and cells
                Row row = sheet.createRow(rowNum);
                Cell cell;
                Integer i = 0;

                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(rowNum++);

                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(parent.getParent_item());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(parent.getEnd_item_desc());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent_desc());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent2_desc());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(res.getUsage());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getUom());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getItem_type());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getCost_total());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                //cell.setCellValue(supplier.getSupplier());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                //cell.setCellValue(supplier.getName());
            }
            if (rowNum > 2) {
                sheet.addMergedRegion(new CellRangeAddress(1, rowNum - 1, 1, 1));
            }
            for(int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String supplier_file = supplierService.getById(supplier_id).get().getSupplier() + ".xlsx";

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(supplier_file);
            workbook.write(fileOut);
            fileOut.close();

            String TextMail = "Hi Team,\n\nWe need the attached components.\n\n";

            TextMail += "\nThank you,\nHanon Systems.";

            // Closing the workbook
            workbook.close();

            try {
                helper.setTo(supplierService.getById(supplier_id).get().getEmail());
                helper.setText(TextMail);
                helper.setSubject("Plan for the Supplier - " + supplierService.getById(supplier_id).get().getName());
                helper.addAttachment(supplier_file, new File(supplier_file));
                sender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error while sending mail ..";
            }
        }

        return "Mail Sent Success!";
    }
    */

    @PostMapping("/sendMailUI")
    public String sendMailUI(@RequestBody String json, @RequestParam() String description) throws IOException {
        EmailTable emailTable = new EmailTable();
        emailTable.setDescription(description);
        emailTable.setCreated_date(new Date());
        emailService.create(emailTable);
        Long email_id = emailTable.getId();

        JSONObject body = new JSONObject(json.toString());
        HashSet<Long> hashsupplier_ids = new HashSet<>();
        HashSet<Long> hashparent_ids = new HashSet<>();
        HashSet<Long> hashchild_ids = new HashSet<>();
        HashMap<Long, List<Long>>  planUsage = new HashMap<Long, List<Long>>();

        for (Integer i = 0; i < body.length(); ++i) {
            System.out.println(body.get(i.toString()));
            JSONObject t = (JSONObject) body.get(i.toString());
            System.out.println(t.get("supplier"));
            System.out.println(t.get("parent_item"));
            System.out.println(t.get("component"));
            System.out.println(t.get("d1"));
            System.out.println(t.get("d2"));
            System.out.println(t.get("d3"));

            System.out.println(supplierService.getBySupplier(t.get("supplier").toString()).getId());
            System.out.println(parentService.getByParentItem(t.get("parent_item").toString()).getId());
            System.out.println(childService.getByComponent(t.get("component").toString()).getId());
            hashsupplier_ids.add(supplierService.getBySupplier(t.get("supplier").toString()).getId());
            hashparent_ids.add(parentService.getByParentItem(t.get("parent_item").toString()).getId());
            hashchild_ids.add(childService.getByComponent(t.get("component").toString()).getId());
            Long v1 = Long.valueOf(t.get("d1").toString());
            Long v2 = Long.valueOf(t.get("d2").toString());

            Long v3 = Long.valueOf(t.get("d3").toString());
            List<Long> list = new ArrayList<Long>();
            list.add(v1);
            list.add(v2);
            list.add(v3);
            planUsage.put(childService.getByComponent(t.get("component").toString()).getId(), list);
        }

        List<Long> parent_ids = new ArrayList<Long>(hashparent_ids);
        List<Long> child_ids = new ArrayList<Long>(hashchild_ids);

        String[] columns = {"Seq", "Parent Item", "End Item Desc", "D1", "D2", "D3", "Component", "Component Description", "Comp Desc2", "Usage", "UOM", "Item Type", "Cost Total", "Supplier", "Name", "email", "D1", "D2", "D3" };


        for (Long supplier_id: hashsupplier_ids) {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            List<PlanTable> result = planService.getPlanByParentIdsChildIdsSupplierId (parent_ids, child_ids, supplier_id);
            Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
            //CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet();
            sheet.setDisplayGridlines(false);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setColor(IndexedColors.YELLOW.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);

            CellStyle otherCellStyle = workbook.createCellStyle();
            otherCellStyle.setBorderBottom(BorderStyle.THIN);
            otherCellStyle.setBorderLeft(BorderStyle.THIN);
            otherCellStyle.setBorderTop(BorderStyle.THIN);
            otherCellStyle.setBorderRight(BorderStyle.THIN);

            // Create a Row
            Row headerRow = sheet.createRow(0);
            // Create cells
            for(int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
            }

            // Create Cell Style for formatting Date
            //CellStyle dateCellStyle = workbook.createCellStyle();
            //dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            int rowNum = 1;
            for (PlanTable res: result) {
                ParentTable parent = parentService.getById(res.getParent_id()).get();
                ChildTable child = childService.findById(res.getChild_id()).get();
                //SupplierTable supplier = supplierService.getById(res.getSupplier_id()).get();

                EmailPlanTable emailPlanTable = new EmailPlanTable();
                //emailPlanTable.setParent_id(res.getParent_id());
                emailPlanTable.setChild_id(res.getChild_id());
                //emailPlanTable.setSupplier_id(res.getSupplier_id());
                emailPlanTable.setCreated_date(new Date());
                //emailPlanTable.setEmail_id(email_id);
                emailPlanTable.setIs_active('Y');
                //emailPlanTable.setSeq(res.getSeq());
                //emailPlanTable.setUpload_id(res.getUpload_id());

                emailPlanService.create(emailPlanTable);

                // Create Other rows and cells
                Row row = sheet.createRow(rowNum);
                Cell cell;
                Integer i = 0;

                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(rowNum++);

                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(parent.getParent_item());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(parent.getEnd_item_desc());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(planUsage.get(child.getId()).get(0));
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(planUsage.get(child.getId()).get(1));
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(planUsage.get(child.getId()).get(2));
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent_desc());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getComponent2_desc());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(res.getUsage());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getUom());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getItem_type());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(child.getCost_total());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                //cell.setCellValue(supplier.getSupplier());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                //cell.setCellValue(supplier.getName());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                //cell.setCellValue(supplier.getEmail());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(planUsage.get(child.getId()).get(0) * res.getUsage());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(planUsage.get(child.getId()).get(1) * res.getUsage());
                cell = row.createCell(i++);
                cell.setCellStyle(otherCellStyle);
                cell.setCellValue(planUsage.get(child.getId()).get(2) * res.getUsage());
            }
            /*if (rowNum > 2) {
                sheet.addMergedRegion(new CellRangeAddress(1, rowNum - 1, 1, 1));
            }*/
            for(int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String supplier_file = supplierService.getById(supplier_id).get().getSupplier() + ".xlsx";

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(supplier_file);
            workbook.write(fileOut);
            fileOut.close();

            String TextMail = "Hi Team,\n\nWe need the attached components.\n\n";

            TextMail += "\nThank you,\nHanon Systems.";

            // Closing the workbook
            workbook.close();

            try {
                helper.setTo(supplierService.getById(supplier_id).get().getEmail());
                helper.setText(TextMail);
                helper.setSubject("Plan for the Supplier - " + supplierService.getById(supplier_id).get().getName());
                helper.addAttachment(supplier_file, new File(supplier_file));
                sender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error while sending mail ..";
            }
        }
        return "Mail Sent Success!";
    }

    @PostMapping("/stagePlan")
    public List<EmailPlanTable> stagePlan(@RequestBody String json) throws IOException {
        EmailTable emailTable = new EmailTable();
        emailTable.setDescription(new Date().toString());
        emailTable.setCreated_date(new Date());
        emailService.create(emailTable);
        Long email_id = emailTable.getId();
        missingList.clear();

        JSONArray j = new JSONArray(json);
        for (int i = 0 ; i < j.length(); ++i) {
            System.out.println(j.get(i));
            JSONObject input = (JSONObject) j.get(i);
            String parent_item = (String) input.get("id");
            if(parentService.isParentItemExists(parent_item) == 0) {
                //addToexception(j);
                missingList.add(parent_item);
            }
            else {
                Long parent_id = parentService.getByParentItem(parent_item).getId();
                Long d1 = Long.valueOf(input.get("d1").toString());
                Long d2 = Long.valueOf(input.get("d2").toString());
                Long d3 = Long.valueOf(input.get("d3").toString());
                List<EmailPlanTable> emailPlanTables = emailPlanService.stagePlan(parent_id, d1, d2, d3, email_id);
                for (EmailPlanTable em : emailPlanTables) {
                    emailPlanService.create(em);
                }
            }
        }
        return emailPlanService.getPlanByEmailId(email_id);
    }

    @PostMapping("/updatePlan")
    private List<EmailPlanTable> updatePlan(@RequestBody String strJson) {
        JSONObject json = new JSONObject(strJson);
        //System.out.println(json.toString());
        Long id = json.getLong("id");

        Double d1 = Double.valueOf(json.get("d1").toString());  //json.getDouble("d1");
        Double d2 = Double.valueOf(json.get("d2").toString());  //json.getDouble("d1");
        Double d3 = Double.valueOf(json.get("d3").toString());  //json.getDouble("d1");
        EmailPlanTable emailPlanTable = emailPlanService.getById(id);

        //EmailPlanTable emailPlanTable = emailPlanService.searchPlan(id, parent_id, email_id, child_id, supplier_id);
        //System.out.println(d1 + " " + d2 + " " + d3);
        emailPlanTable.setD1(d1);
        emailPlanTable.setD2(d2);
        emailPlanTable.setD3(d3);

        emailPlanService.update(emailPlanTable);
        return emailPlanService.getPlanByEmailId(emailPlanTable.getEmail_id());
    }

    @PostMapping("/deletePlan")
    private List<EmailPlanTable> deletePlan(@RequestBody String strJson) {
        JSONObject json = new JSONObject(strJson);
        //System.out.println(json.toString());
        Long id = json.getLong("id");

        EmailPlanTable emailPlanTable = emailPlanService.getById(id);
        emailPlanTable.setIs_active('N');
        emailPlanService.update(emailPlanTable);
        return emailPlanService.getPlanByEmailId(emailPlanTable.getEmail_id());
    }

    @PostMapping("/sendPlan")
    private List<EmailPlanTable> sendPlan(@RequestBody String strJson) {
        JSONObject json = new JSONObject(strJson);
        //System.out.println(json.toString());
        Long email_id = json.getLong("id");
        List<java.math.BigInteger> supplierIds = emailPlanService.getSupplierIds(email_id);
        for (java.math.BigInteger supplier_id: supplierIds) {
            List<EmailPlanTable> ept = emailPlanService.getPlanBySupplierId(supplier_id, email_id);
            sendPlanToSupplier(supplier_id.longValue(), ept);
        }
        return emailPlanService.getPlanByEmailId((long) 0);
    }

    private void sendPlanToSupplier(Long supplier_id, List<EmailPlanTable> ept) {
        //System.out.println("We are here");
        String[] columns = {"Seq", "Component", "Component Description", "Comp Desc2", "Supplier", "Name", "D1", "D2", "D3"};

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` files
        //CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Plan");
        sheet.setDisplayGridlines(false);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 11);
        headerFont.setColor(IndexedColors.YELLOW.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);

        CellStyle otherCellStyle = workbook.createCellStyle();
        otherCellStyle.setBorderBottom(BorderStyle.THIN);
        otherCellStyle.setBorderLeft(BorderStyle.THIN);
        otherCellStyle.setBorderTop(BorderStyle.THIN);
        otherCellStyle.setBorderRight(BorderStyle.THIN);

        // Create a Row
        Row headerRow = sheet.createRow(0);
        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        //CellStyle dateCellStyle = workbook.createCellStyle();
        //dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        int rowNum = 1;
        for (EmailPlanTable res : ept) {
            ChildTable child = childService.findById(res.getChild_id()).get();
            SupplierTable supplier = supplierService.getById(res.getSupplier_id()).get();

            // Create Other rows and cells
            Row row = sheet.createRow(rowNum);
            Cell cell;
            Integer i = 0;

            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(rowNum++);

            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(child.getComponent());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(child.getComponent_desc());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(child.getComponent2_desc());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(supplier.getSupplier());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(supplier.getName());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(res.getD1());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(res.getD2());
            cell = row.createCell(i++);
            cell.setCellStyle(otherCellStyle);
            cell.setCellValue(res.getD3());
        }
        /*
        if (rowNum > 2) {
            sheet.addMergedRegion(new CellRangeAddress(1, rowNum - 1, 1, 1));
        }
        */
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        sheet.protectSheet("Secret");
        String supplier_file = supplierService.getById(supplier_id).get().getSupplier() + ".xlsx";

        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(supplier_file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            workbook.write(fileOut);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            fileOut.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String TextMail = "Hi Team,\n\nWe need the attached components.\n\n";
        if(missingList.size() > 0) {
            TextMail += "The below parents are not in the database. Please check.\n\n";
            for (String par: missingList) {
                TextMail += par + "\n";
            }
            TextMail += "\n";
        }
        TextMail += "\nThank you,\nHanon Systems.";

        //System.out.println(TextMail);
        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            helper.setTo(supplierService.getById(supplier_id).get().getEmail().split(","));
            helper.setText(TextMail);
            Map<String, String> env = System.getenv();
            helper.setFrom(env.get("MAIL_USER"));
            helper.setSubject("Plan for the Supplier - " + supplierService.getById(supplier_id).get().getName());
            helper.addAttachment(supplier_file, new File(supplier_file));
            //System.out.println(supplier_file);
            sender.send(message);
        } catch (MessagingException e1) {
            e1.printStackTrace();
        }
    }

    /*
    private static void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                } else {
                    System.out.print(cell.getNumericCellValue());
                }
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula());
                break;
            case BLANK:
                System.out.print("");
                break;
            default:
                System.out.print("");
        }
        System.out.print("\t");
    }
    */
}