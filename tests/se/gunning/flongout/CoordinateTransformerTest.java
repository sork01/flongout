//package se.gunning.flongout;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class CoordinateTransformerTest {
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//	@Test
//	public void testToScreen() {
//		CoordinateTransformer ct = new CoordinateTransformer(1024.0, 768.0, -1.0, 1.0, -2.0, 7.0);
//		
//		assertEquals(0.0, ct.toScreenX(-1.0), 1e-14);
//		assertEquals(1024, ct.toScreenX(1.0), 1e-14);
//		
//		assertEquals(768.0, ct.toScreenY(-2.0), 1e-14);
//		assertEquals(0.0, ct.toScreenY(7.0), 1e-14);
//		
//		assertEquals(512.0, ct.toScreenX(0.0), 1e-14);
//		assertEquals(384.0, ct.toScreenY(2.5), 1e-14);
//	}
//}
