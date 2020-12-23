package ule.edi.event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.*;

import ule.edi.model.*;
import ule.edi.model.Configuration.Type;

public class EventArrayImplTests {

	private DateFormat dformat = null;
	private EventArrayImpl e;
	
	private Date parseLocalDate(String spec) throws ParseException {
        return dformat.parse(spec);
	}

	public EventArrayImplTests() {
		
		dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	}
	
	@Before
	public void testBefore() throws Exception{
	    e = new EventArrayImpl("The Fabulous Five", parseLocalDate("24/02/2018 17:00:00"), 110);

	}
	
	@Test
	public void testEventoVacio() throws Exception {
		
	    Assert.assertTrue(e.getNumberOfAvailableSeats()==110);
	    Assert.assertEquals(e.getNumberOfAvailableSeats(), 110);
	    Assert.assertEquals(e.getNumberOfAttendingAdults(), 0);
	}
	
	@Test
	public void testSellSeat1Adult() throws Exception{
		
			
	    Assert.assertEquals(e.getNumberOfAttendingAdults(), 0);
		Assert.assertTrue(e.sellSeat(1, new Person("10203040A","Alice", 34),false));	//venta normal
	    Assert.assertEquals(e.getNumberOfAttendingAdults(), 1);  
	    Assert.assertEquals(e.getNumberOfNormalSaleSeats(), 1);
	  
	}
	

	
	@Test
	public void testgetCollection() throws Exception{
		Event  ep = new EventArrayImpl("The Fabulous Five", parseLocalDate("24/02/2018 17:00:00"), 4);
		Assert.assertEquals(ep.sellSeat(1, new Person("1010", "AA", 10), true),true);
		Assert.assertTrue(ep.getCollectionEvent()==75);					
	}
	
	// TODO EL RESTO DE MÉTODOS DE TESTS NECESARIOS PARA LA COMPLETA COMPROBACIÓN DEL BUEN FUNCIONAMIENTO DE TODO EL CÓDIGO IMPLEMENTADO

	@Test
	public void testEventArrayImpl() throws Exception{
		 e = new EventArrayImpl("Prueba Constructor", parseLocalDate("24/02/2018 17:00:00"), 100, 100.0, (byte)25);
	}
	
	@Test
	public void testGetName() throws Exception{
		Assert.assertEquals("The Fabulous Five", e.getName());
	}
	
	@Test
	public void testGetDateEvent() throws Exception{
		Assert.assertEquals(parseLocalDate("24/02/2018 17:00:00"), e.getDateEvent());
	}
	
	@Test
	public void testGetPrice() throws Exception{
		Assert.assertEquals(Configuration.DEFAULT_PRICE,e.getPrice());
	}
	
	@Test
	public void testGetDiscountAdvanceSale() throws Exception{
		Assert.assertEquals(Configuration.DEFAULT_DISCOUNT,e.getDiscountAdvanceSale());
	}
	
	@Test
	public void testGetNumberOfSoldSeats() throws Exception{
		Assert.assertEquals(0,e.getNumberOfSoldSeats());
		Person p= new Person("Pablo", "X62482388V", 16);
		e.sellSeat(0, p, false); //no existe este numero de la butaca
		e.sellSeat(111, p, false); ////no existe este numero de la butaca
		e.sellSeat(1, p, false); //+1
		e.sellSeat(10, p, false); //+1
		e.sellSeat(10, p, false); //Ya esta ocupado
		e.sellSeat(110, p, false); //+1
		Assert.assertEquals(3,e.getNumberOfSoldSeats());	
	}
	
	@Test
	public void testGetNumberOfNormalSaleSeats() {
		Person p= new Person("Pablo", "X62482388V", 16);
		e.sellSeat(1, p, true);  //Es anticipada
		e.sellSeat(10, p, false); //+1
		e.sellSeat(10, p, false); //Ya esta ocupado
		e.sellSeat(110, p, false); //+1
		Assert.assertEquals(2,e.getNumberOfNormalSaleSeats());	
	}
	
	@Test
	public void testGetNumberOfAdvanceSaleSeats() {
		Person p= new Person("Pablo", "X62482388V", 16);
		e.sellSeat(1, p, true);  //+1
		e.sellSeat(10, p, false); //no es anticipada
		e.sellSeat(10, p, true); //Ya esta ocupado
		e.sellSeat(110, p, false); //no es anticipada
		Assert.assertEquals(1,e.getNumberOfAdvanceSaleSeats());	
	}
	
	@Test
	public void testGetNumberOfSeats() {
		Assert.assertEquals(110, e.getNumberOfSeats());
	}
	
	@Test
	public void testGetSeat() {
		Assert.assertEquals(null,e.getSeat(0));
		Assert.assertEquals(null,e.getSeat(111));
		
		Person p= new Person("Jaja", "X62482388V", 16);
		e.sellSeat(10, p, false);
		Seat asiento = new Seat(e, 10,Configuration.Type.NORMAL ,p);
		asiento.setType(Type.NORMAL);
		Assert.assertEquals(asiento.toString(),e.getSeat(10).toString());
		
	}
	
	@Test
	public void testRefundSeat() {
		
		Assert.assertNull(e.refundSeat(0));
		Assert.assertNull(e.refundSeat(111));
		
		Person p= new Person("Jaja", "X62482388V", 16);
		e.sellSeat(10, p, false);
		
		Assert.assertEquals(p, e.refundSeat(10));
		
	}
	
	@Test
	public void testGetNumberOfAttendingChildren() {
		Person a= new Person("Adulto","X3413431H",14);
		e.sellSeat(10, a, false); //Es un adulto
		Assert.assertEquals(0, e.getNumberOfAttendingChildren());
		

		Person n= new Person("ninio","X3413431H",13);
		e.sellSeat(20, n, false); //+1
		e.sellSeat(30, n, false); //+1
		e.sellSeat(40, n, false); //+1
		Assert.assertEquals(3, e.getNumberOfAttendingChildren());
		
	}
	
	@Test
	public void testGetNumberOfAttendingAdults() {
		
		Person n= new Person("ninio","X3413431H",13);
		e.sellSeat(9, n, false); //Es un ninio
		
		Person v= new Person("viejo","X3413431H",90);
		e.sellSeat(90, v, false); //Es un ancuano
		
		Person a= new Person("Adulto","X3413431H",14);
		e.sellSeat(10, a, false); //+1
		Assert.assertEquals(1, e.getNumberOfAttendingAdults());
		e.sellSeat(20, a, false); //+1
		e.sellSeat(30, a, false); //+1
		e.sellSeat(40, a, false); //+1
		Assert.assertEquals(4, e.getNumberOfAttendingAdults());
		
	}
	
	@Test
	public void testGetNumberOfAttendingElderlyPeople() {
		
		Person a= new Person("Adulto","X3413431H",14);
		e.sellSeat(1, a, false); //No es un anciano
		Assert.assertEquals(0,e.getNumberOfAttendingElderlyPeople());
		
		Person v= new Person("viejo","X3413431H",90);
		e.sellSeat(10, v, false); //+1
		e.sellSeat(20, v, false); //+1
		e.sellSeat(30, v, false); //+1
		e.sellSeat(40, v, false); //+1
		Assert.assertEquals(4,e.getNumberOfAttendingElderlyPeople());
		
	}
	
	@Test
	public void testGetAvailableSeatsList() throws ParseException {

		Event ev= new EventArrayImpl("The Fabulous Five", parseLocalDate("24/02/2018 17:00:00"), 10);
		Person p= new Person("Jaja","X3413431H",14);
		ev.sellSeat(1,p, false); 
		ev.sellSeat(3,p, false); 
		ev.sellSeat(5,p, true); 
		ev.sellSeat(7,p, true); 
		ev.sellSeat(9,p, true); 
		// todos los impares se han vendido,
		// quedan los pares.
		Assert.assertEquals(ev.getAvailableSeatsList().toString(),"[2, 4, 6, 8, 10]");

	}
	
	@Test
	public void testGetAdvanceSaleSeatsList() {

		Person p= new Person("Jaja","X3413431H",14);
		
		e.sellSeat(10,p, false); //no anticipada
		e.sellSeat(20,p, true); //20
		e.sellSeat(30,p, true); //30
		e.sellSeat(40,p, true); //40
		e.sellSeat(50,p, true); //50
		e.sellSeat(60,p, true); //60
		Assert.assertEquals(e.getAdvanceSaleSeatsList().toString(), "[20, 30, 40, 50, 60]");
		
	}
	
	@Test
	public void testGetMaxNumberConsecutiveSeats() throws ParseException {
		
		Event ep= new EventArrayImpl("The Fabulous Seven", parseLocalDate("24/02/2019  20:00:00"), 4); 
		Person p= new Person("Jaja","X3413431H",14);
		ep.sellSeat(2, p, false); 
		Assert.assertEquals(ep.getMaxNumberConsecutiveSeats(), 2);
	}
	
	@Test
	public void testGetPrice2() throws ParseException {
		
		Person p= new Person("Jaja", "X62482388V", 16);

		e.sellSeat(10, p, false); 
		e.sellSeat(20, p, true); 

		Assert.assertEquals(e.getPrice(e.getSeat(10)), 100, 0.001);
		Assert.assertEquals(e.getPrice(e.getSeat(20)), 75, 0.001);
		
		Event  ev = new EventArrayImpl("The Fabulous Five", parseLocalDate("24/02/2018 17:00:00"), 10);
		Assert.assertEquals(ev.getPrice(ev.getSeat(10)),0.00, 0.001);
		ev.sellSeat(30, p, false); 
		
		Assert.assertEquals(e.getPrice(ev.getSeat(30)),0.00, 0.001);

	}
	

	
	@Test
	public void testgetPosPerson() {
		

		Person p= new Person("Jaja", "X62482388V", 16);
		e.sellSeat(10, p, false);
		Person p1 = null;
		Assert.assertEquals(e.getPosPerson(p), 10);
		Assert.assertEquals(e.getPosPerson(p1),-1);
		
	}
	
	@Test
	public void testisAdvanceSale() throws ParseException {
		Person p= new Person("Jaja", "X62482388V", 16);
		Person p1 = null;
		Person p2= new Person ("Emmm", "H73642387B", 66); 
		e.sellSeat(10, p2, false); 
		e.sellSeat(20, p, true); 
		Assert.assertTrue(e.isAdvanceSale(p));
		Assert.assertFalse(e.isAdvanceSale(p1));
		Assert.assertFalse(e.isAdvanceSale(p2));

	}
	
	@Test
	public void testEquals() {

		Person p= new Person("Jaja", "X62482388V", 16);
		Person p2= new Person ("Jaja", "X62482388V", 16); 
		Assert.assertEquals(p, p2);
		
		
	}
	


}
