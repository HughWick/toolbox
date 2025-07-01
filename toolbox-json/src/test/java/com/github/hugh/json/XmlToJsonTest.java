package com.github.hugh.json;

import com.github.hugh.json.gson.GsonUtils;
import com.github.hugh.json.gson.Jsons;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML 转 json
 */
class XmlToJsonTest {

    @Test
    void test01() {
        String str1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Error>\n" +
                "  <Code>NoSuchKey</Code>\n" +
                "  <Message>The specified key does not exist.</Message>\n" +
                "  <Key>su_pai/su_pai-1.0.1.apk2</Key>\n" +
                "  <BucketName>apk</BucketName>\n" +
                "  <Resource>/apk/su_pai/su_pai-1.0.1.apk2</Resource>\n" +
                "  <RequestId>180629777916A56F</RequestId>\n" +
                "  <HostId>dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8</HostId>\n" +
                "</Error>";
        Jsons jsons1 = GsonUtils.xmlToJson(str1);
        Jsons error1 = jsons1.getThis("Error");
        assertTrue(error1.isEquals("Code", "NoSuchKey"));

        String str2 = "<Error>\n" +
                "  <Code>NoSuchKey</Code>\n" +
                "  <Message>The specified key does not exist.</Message>\n" +
                "  <Key>su_pai/su_pai-1.0.1.apk2</Key>\n" +
                "  <BucketName>apk</BucketName>\n" +
                "  <Resource>/apk/su_pai/su_pai-1.0.1.apk2</Resource>\n" +
                "  <RequestId>180629777916A56F</RequestId>\n" +
                "  <HostId>dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8</HostId>\n" +
                "</Error>";
        Jsons jsons2 = GsonUtils.xmlToJson(str2);
//        System.out.println(jsons2);
        Jsons error2 = jsons2.getThis("Error");
        assertTrue(error2.isEquals("BucketName", "apk"));
    }

    @Test
    void testException() {
        String str3 = "<Code>NoSuchKey</Code>\n" +
                "  <Message>The specified key does not exist.</Message>\n" +
                "  <Key>su_pai/su_pai-1.0.1.apk2</Key>\n" +
                "  <BucketName>apk</BucketName>\n" +
                "  <Resource>/apk/su_pai/su_pai-1.0.1.apk2</Resource>\n" +
                "  <RequestId>180629777916A56F</RequestId>\n" +
                "  <HostId>dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8</HostId>\n";
        // 验证是否抛出 RuntimeException 异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            GsonUtils.xmlToJson(str3);
        });
        // 验证异常消息中是否包含特定的错误信息
        assertEquals("org.dom4j.DocumentException: Error on line 2 of document  : 文档中根元素后面的标记必须格式正确。", exception.getMessage());
    }

    @Test
    void testXmlToObject() {
        String str1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Error>\n" +
                "  <Code>NoSuchKey</Code>\n" +
                "  <Message>The specified key does not exist.</Message>\n" +
                "  <Key>su_pai/su_pai-1.0.1.apk2</Key>\n" +
                "  <BucketName>apk</BucketName>\n" +
                "  <Resource>/apk/su_pai/su_pai-1.0.1.apk2</Resource>\n" +
                "  <RequestId>180629777916A56F</RequestId>\n" +
                "  <HostId>dd9025bab4ad464b049177c95eb6ebf374d3b3fd1af9251148b658df7ac2e3e8</HostId>\n" +
                "</Error>";
        Error errorResponse = GsonUtils.xmlToObject(str1, Error.class);
        assertEquals("NoSuchKey", errorResponse.getError().getCode());
        Error.ErrorResponse errorResponse2 = GsonUtils.xmlToObject(str1, Error.ErrorResponse.class, "Error");
        assertEquals("apk", errorResponse2.getBucketName());
    }


    @Test
    void test02() {
        String str1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<order>\n" +
                "    <orderId>12345</orderId>\n" +
                "    <customer>\n" +
                "        <name>John Doe</name>\n" +
                "        <email>johndoe@example.com</email>\n" +
                "        <address>\n" +
                "            <street>Main Street 123</street>\n" +
                "            <city>Sample City</city>\n" +
                "            <postalCode>12345</postalCode>\n" +
                "            <country>US</country>\n" +
                "        </address>\n" +
                "        <phoneNumbers>\n" +
                "            <phone>+1-800-555-1234</phone>\n" +
                "            <phone>+1-800-555-5678</phone>\n" +
                "        </phoneNumbers>\n" +
                "    </customer>\n" +
                "    <items>\n" +
                "        <item>\n" +
                "            <productId>1001</productId>\n" +
                "            <name>Wireless Mouse</name>\n" +
                "            <quantity>2</quantity>\n" +
                "            <price currency=\"USD\">19.99</price>\n" +
                "            <description><![CDATA[Ergonomic wireless mouse with adjustable DPI.]]></description>\n" +
                "        </item>\n" +
                "        <item>\n" +
                "            <productId>1002</productId>\n" +
                "            <name>Keyboard</name>\n" +
                "            <quantity>1</quantity>\n" +
                "            <price currency=\"USD\">49.99</price>\n" +
                "            <description><![CDATA[Mechanical keyboard with RGB backlighting.]]></description>\n" +
                "        </item>\n" +
                "    </items>\n" +
                "    <totalAmount currency=\"USD\">89.97</totalAmount>\n" +
                "    <payment>\n" +
                "        <method>Credit Card</method>\n" +
                "        <transactionId>ABC123XYZ</transactionId>\n" +
                "    </payment>\n" +
                "    <shipping>\n" +
                "        <method>FedEx</method>\n" +
                "        <trackingNumber>1Z9999999999999999</trackingNumber>\n" +
                "    </shipping>\n" +
                "    <orderDate>2024-11-01</orderDate>\n" +
                "    <status>Shipped</status>\n" +
                "</order>";
        Jsons jsons = GsonUtils.xmlToJson(str1);
        Jsons order = jsons.getThis("order");
        assertNotNull(order);
        assertEquals(order.getString("orderId"), "12345");
        assertEquals(order.getThis("customer").getString("name"), "John Doe");
//        System.out.println(jsons.toJson());
    }
}

@Data
class Error {
    private ErrorResponse Error;

    @Data
    public class ErrorResponse {
        private String Code;
        private String Message;
        private String key;
        private String BucketName;
        private String Resource;
        private String RequestId;
        private String HostId;
    }
}