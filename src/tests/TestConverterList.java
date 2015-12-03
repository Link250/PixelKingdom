package tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.conversion.ConverterQueue;

public class TestConverterList {

	ConverterQueue list;
	Random ran;
	boolean bool;
	byte b;
	short s;
	int i;
	long l;
	float f;
	double d;
	char c;
	
	@Before
	public void setUp() throws Exception {
		list = new ConverterQueue();
		ran = new Random();
		
		bool = ran.nextBoolean();
		list.addBoolean(bool);
		
		b = (byte) ran.nextInt();
		list.addByte(b);
		
		s = (short) ran.nextInt();
		list.addShort(s);
		
		i = ran.nextInt();
		list.addInt(i);
		
		l = ran.nextLong();
		list.addLong(l);
		
		f = ran.nextFloat();
		list.addFloat(f);
		
		d = ran.nextDouble();
		list.addDouble(d);
		
		c = (char) ran.nextInt();
		list.addChar(c);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(list.pollBoolean(), bool);
		assertEquals(list.pollByte(), b);
		assertEquals(list.pollShort(), s);
		assertEquals(list.pollInt(), i);
		assertEquals(list.pollLong(), l);
		assertEquals(list.pollFloat(), f, 0.000000001);
		assertEquals(list.pollDouble(), d, 0.00000000000000001);
		assertEquals(list.pollChar(), c);
	}

}
