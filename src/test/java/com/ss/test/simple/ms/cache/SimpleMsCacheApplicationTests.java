package com.ss.test.simple.ms.cache;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import com.ss.test.simple.ms.cache.service.KeyValueService;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleMsCacheApplicationTests {

    @NotNull
    @Autowired
    private KeyValueService keyValueService;

	@Test
	public void contextLoads() {
	}

    @Test
	public void test1Inserting() {
        keyValueService.clear();
	    keyValueService.set(new KeyValueObject("4", "1"));
        keyValueService.set(new KeyValueObject("14", "3"));
        keyValueService.set(new KeyValueObject("21", "23"));
        keyValueService.set(new KeyValueObject("33", "70"));
        keyValueService.set(new KeyValueObject("55", "55"));
    }

    @Test
    public void test2Getting() {

        final KeyValueEntity by4 = keyValueService.get("4");
        final KeyValueEntity by14 = keyValueService.get("14");
        final KeyValueEntity by21 = keyValueService.get("21");
        final KeyValueEntity by33 = keyValueService.get("33");
        final KeyValueEntity by55 = keyValueService.get("55");

        Assert.assertNotNull("not found value for the key 4", by4);
        Assert.assertNotNull("not found value for the key 13", by14);
        Assert.assertNotNull("not found value for the key 21", by21);
        Assert.assertNotNull("not found value for the key 33", by33);
        Assert.assertNotNull("not found value for the key 55", by55);

        Assert.assertEquals(by4.getValue(), "1");
        Assert.assertEquals(by14.getValue(), "3");
        Assert.assertEquals(by21.getValue(), "23");
        Assert.assertEquals(by33.getValue(), "70");
        Assert.assertEquals(by55.getValue(), "55");
    }

    @Test
    public void test3Updating() {

        keyValueService.set(new KeyValueObject("4", "55"));
        keyValueService.set(new KeyValueObject("14", "22"));
        keyValueService.set(new KeyValueObject("21", "66"));
        keyValueService.set(new KeyValueObject("33", "11"));
        keyValueService.set(new KeyValueObject("55", "77"));

        final KeyValueEntity by4 = keyValueService.get("4");
        final KeyValueEntity by14 = keyValueService.get("14");
        final KeyValueEntity by21 = keyValueService.get("21");
        final KeyValueEntity by33 = keyValueService.get("33");
        final KeyValueEntity by55 = keyValueService.get("55");

        Assert.assertNotNull("not found value for the key 4", by4);
        Assert.assertNotNull("not found value for the key 13", by14);
        Assert.assertNotNull("not found value for the key 21", by21);
        Assert.assertNotNull("not found value for the key 33", by33);
        Assert.assertNotNull("not found value for the key 55", by55);

        Assert.assertEquals(by4.getValue(), "55");
        Assert.assertEquals(by14.getValue(), "22");
        Assert.assertEquals(by21.getValue(), "66");
        Assert.assertEquals(by33.getValue(), "11");
        Assert.assertEquals(by55.getValue(), "77");

        keyValueService.clear();
    }
}
