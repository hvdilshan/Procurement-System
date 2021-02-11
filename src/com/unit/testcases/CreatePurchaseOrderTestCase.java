package com.unit.testcases;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.procument.model.Item;
import com.procument.model.PurchaseOrder;
import com.procument.model.Site;
import com.procument.model.Supplier;
import com.procument.model.User;
import com.procument.service.ItemService;
import com.procument.service.PurchaseOrderService;
import com.procument.service.SiteManagerService;
import com.procument.service.SiteService;
import com.procument.service.SupplierService;

public class CreatePurchaseOrderTestCase {
	
	PurchaseOrderService purchaseOrderService;
	SiteManagerService siteManagerService;
	ItemService itemService;
	ArrayList<Item> items;
	ArrayList<Item> specialItems;
	User user;
	PurchaseOrder purchaseOrder;
	PurchaseOrder specialPurchaseOrder;
	Supplier supplier;
	SupplierService supplierService;
	SiteService siteService;
	Site site;
	int po_id;

	//Checking Values
	final String PO_STATUS = "Pending";
	final int ITEM_ID_ONE = 1;
	final int ITEM_ID_TWO = 6;
	final int ITEM_ID_SPECIAL = 4;
	final int ITEM_ID_ONE_QUANTITY = 100;
	final int ITEM_ID_TWO_QUANTITY = 13;
	final int ITEM_ID_SPECIAL_QUANTITY = 5;
	final String SUPPLIER_ID = "SophLyfter97421310";
	final int SITE_ID = 1;
	final int USER_ID = 1;
	final double PO_PRICE = 103500;
	final String PO_DESCRIPTION_FALSE = "PO is under budget";
	final String PO_SPECIAL_DECRIPTION_TRUE = "Some items of the PO need some special Approval!";
	final String PO_APPROVED = "Approved";
	
	public CreatePurchaseOrderTestCase() {
		//get purchase order
		purchaseOrderService = purchaseOrderService.getInstance();
		siteManagerService = new SiteManagerService();
		itemService = itemService.getInstance();
		purchaseOrder = new PurchaseOrder();
		specialPurchaseOrder = new PurchaseOrder();
		supplierService = SupplierService.getInstance();
		siteService = SiteService.getInstance();	
	}
	
	//initialize new Item
	//get item from db
	public void assignItems() {
		//init items array
		items = new ArrayList<Item>();
		//adding exisiting items in the database
		Item item_one = new Item();
		item_one = itemService.getItem(ITEM_ID_ONE);
		item_one.setQuantity(ITEM_ID_ONE_QUANTITY);
		
		Item item_two = new Item();
		item_two = itemService.getItem(ITEM_ID_TWO);
		item_two.setQuantity(ITEM_ID_TWO_QUANTITY);
		
		//adding to the item list
		items.add(item_one);
		items.add(item_two);
		
	}
	
	//initialize new Item
	//get item from db
	public void assignSpecial() {
		
		//init items array
		specialItems = new ArrayList<Item>();
		
		//adding exisiting items in the database
		Item item_one = new Item();
		item_one = itemService.getItem(ITEM_ID_SPECIAL);
		item_one.setQuantity(ITEM_ID_SPECIAL_QUANTITY);
		
		//adding to the item list
		specialItems.add(item_one);
		
	}
	
	//getUser 
	public void getUser() {
		user = new User();
		user = siteManagerService.getUserById(USER_ID);
	}

	//getSupplier id = SophLyfter97421310
	public void getSupplier() {
		supplier = new Supplier();
		supplier = supplierService.getSupplierDetails(SUPPLIER_ID);
	}
	
	public void getSite() {
		site = new Site();
		site = siteService.getSiteById(SITE_ID);
	}
	
	//set purchaseOrder 
	@Test(groups = "com.po.testcases")
	public void createNewPo() {
		getUser();
		purchaseOrder.setUser(user);
		
		assignItems();
		purchaseOrder.setItems(items);
		
		getSupplier();
		purchaseOrder.setSupplier(supplier);
		
		getSite();
		purchaseOrder.setSite(site);
		
		purchaseOrder.setDescription("");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		String dateStr = sdf.format(date);
		try {
			Date today = sdf.parse(dateStr);
			purchaseOrder.setDilivery_date(today);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//set purchaseOrder with special items
	@Test(groups = "com.po.testcases", dependsOnMethods = "deleteAddedPo")
	public void createNewSpecialPo() {
		getUser();
		specialPurchaseOrder.setUser(user);
		
		assignSpecial();
		specialPurchaseOrder.setItems(specialItems);
		
		getSupplier();
		specialPurchaseOrder.setSupplier(supplier);
		
		getSite();
		specialPurchaseOrder.setSite(site);
		
		specialPurchaseOrder.setDescription("");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		String dateStr = sdf.format(date);
		try {
			Date today = sdf.parse(dateStr);
			specialPurchaseOrder.setDilivery_date(today);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//add purchase order with over 100000 po_price
	//status should be pending
	@Test(groups = "com.po.testcases", dependsOnMethods = "createNewPo")
	public void addPurchaseOrder() {
		Assert.assertTrue(siteManagerService.makeNewPurchaseOrder(purchaseOrder));
	}
	
	
	//add purchase order with special item
	//status should be pending
	//and description should be "Some items of the PO need some special Approval!"
	@Test(groups = "com.po.testcases", dependsOnMethods = {"createNewSpecialPo", "deleteAddedPo"})
	public void addSpecialPurchaseOrder() {
		Assert.assertTrue(siteManagerService.makeNewPurchaseOrder(specialPurchaseOrder));
	}
	
	//getLatest po_id
	@Test(groups = "com.po.testcases", dependsOnMethods = {"addPurchaseOrder"})
	public void getMaxPoId() {
		po_id = purchaseOrderService.getMaxPoId();
	}
	
	//getLatest po_id
	@Test(groups = "com.po.testcases", dependsOnMethods = {"addSpecialPurchaseOrder"})
	public void getMaxPoIdSpecial() {
		po_id = purchaseOrderService.getMaxPoId();
	}
	
	//Status Testing
	@Test(groups = "com.po.testcases", dependsOnMethods = {"addPurchaseOrder", "getMaxPoId"})
	public void checkDescription() {
		
		//get po by the last added po_id
		PurchaseOrder pStatus = purchaseOrderService.getPoById(po_id);
		
		//check the status
		Assert.assertEquals(pStatus.getStatus(), PO_STATUS);
		Assert.assertNotEquals(pStatus.getDescription(), PO_DESCRIPTION_FALSE);
		
		//get items of the po
		ArrayList<Item> po_items = pStatus.getItems();
		
		//check items on related po is matched and in po_items table has entity from these items
		Assert.assertEquals(ITEM_ID_ONE, po_items.get(0).getItem_id());
		Assert.assertEquals(ITEM_ID_TWO, po_items.get(1).getItem_id());

		//check supplier
		Assert.assertEquals(SUPPLIER_ID, pStatus.getSupplier_id());
		
		//check PO price should be equal to 103500
		Assert.assertEquals(PO_PRICE, pStatus.getPo_price());

		//created by id
		Assert.assertEquals(USER_ID, pStatus.getCreated_by());
		
		//check site id
		Assert.assertEquals(SITE_ID, pStatus.getSite_id());
		
	}
	
	//update the po, delete item 6 and check it is approved by checking the po_price
	@Test(groups = "com.po.testcases", dependsOnMethods = {"checkDescription"})
	public void updatePo() {
		purchaseOrder.setPo_id(po_id);
		Assert.assertEquals(PO_APPROVED, purchaseOrderService.deleteItemFromPo(purchaseOrder, ITEM_ID_TWO).getStatus());
	}
	
	//delete added po
	@Test(groups = "com.po.testcases", dependsOnMethods = {"updatePo"})
	public void deleteAddedPo() {
		purchaseOrder.setPo_id(po_id);
		Assert.assertTrue(purchaseOrderService.deletePo(purchaseOrder));
	}
	
	//add po with special item requirment
	//Status Testing for Special Items
	@Test(groups = "com.po.testcases", dependsOnMethods = {"addSpecialPurchaseOrder", "getMaxPoIdSpecial"})
	public void checkSpecialDescription() {
		//get po by the last added po_id
		PurchaseOrder pStatus = purchaseOrderService.getPoById(po_id);
		Assert.assertEquals(PO_SPECIAL_DECRIPTION_TRUE, pStatus.getDescription());
	}
	
	//delete added po
	@Test(groups = "com.po.testcases", dependsOnMethods = {"checkSpecialDescription"})
	public void deleteSpecialAddedPo() {
		specialPurchaseOrder.setPo_id(po_id);
		Assert.assertTrue(purchaseOrderService.deletePo(specialPurchaseOrder));
	}
	
}
