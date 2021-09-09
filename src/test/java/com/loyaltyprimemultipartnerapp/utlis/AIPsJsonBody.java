package com.loyaltyprimemultipartnerapp.utlis;

public class AIPsJsonBody {
	
	String apiBody;
	
	public String  EnrollmentAPIBody(String since,String type,String ccy,String contractStatus,String name,String program,String tier,
		String key,	String FirstName,String Prefix,String Mobile,String City,String Email,
		String userId,String alternateIds,String promoterId,String memberType,String dateofBirth){

	
		 apiBody="{\r\n"
			 		+ "  \r\n"
			 		+ "  \"since\": \""+since+"\",\r\n"
			 		+ "  \"type\": \""+type+"\",\r\n"
			 		+ "  \"ccy\": \""+ccy+"\",\r\n"
			 		+ "  \"contractStatus\": \""+contractStatus+"\",\r\n"
			 		+ "  \"Tier\": \""+tier+"\",\r\n"
			 		+ "  \"key\": \""+key+"\",\r\n"
			 		+ "  \"name\": \""+name+"\",\r\n"
			 		+ " \"program\": \""+program+"\",\r\n"
			 		+ "  \"owner\": {\r\n"
			 		+ "    \"properties\": {\r\n"
			 		+ "        \"FirstName\": \""+FirstName+"\",\r\n"
			 		+ "        \"Prefix\": \""+Prefix+"\",\r\n"
			 		+ "        \"MemberType\": \""+memberType+"\",\r\n"
			 		+ "        \"Mobile\": \""+Mobile+"\",\r\n"
			 		+ "       \"DateofBirth\": \""+dateofBirth+"\",\r\n"
			 		+ "       \"City\": \""+City+"\",\r\n"
			 		+ "       \"Email\": \""+Email+"\" \r\n"
			 		+ "       },     \r\n"
			 		+ "    \"userId\": \""+userId+"\" \r\n"
			 		+ "  },\r\n"
			 		+ "  \"properties\": {\r\n"
			 		+ "    \"PromoterId\":\""+promoterId+"\" \r\n"
			 		+ "  },\r\n"
			 		+ "\"alternateIds\": [ \""+alternateIds+"\"]\r\n"
			 		+ "}";
	
	 
	return apiBody;
	}
	
	public String UpDateEnrollmentBody(String name,String dateofBirth,String address1,String zip)
	{
		apiBody="{\r\n"
				+ "  \"name\": \""+name+"\",\r\n"
				+ "  \"properties\": {\r\n"
				+ "	\"AdvertisingConsent\": false\r\n"
				+ "  },\r\n"
				+ "  \"owner\": {\r\n"
				+ "    \"properties\": {\r\n"
				+ "       \"DateofBirth\": \""+dateofBirth+"\",\r\n"
				+ "       \"Address1\": \""+address1+"\",\r\n"
					+ "       \"Zip\": \""+zip+"\"\r\n"
				+ "    }\r\n"
				+ "  }\r\n"
				+ "}";
		return apiBody;
	}
	
	
	public String TransactionAPIBody(String tranxId,String clientId,String trxDate,String partnerId,
			String quantity,String price,
			String productkey1,String channelKey1, 
			String productkey2,String channelKey2, 
			String productkey3,String channelKey3){
		
		apiBody="{\r\n"
				 + " \r\n"+
				 "\"txId\": \""+tranxId+"\",\r\n"+
					"  \"date\": \""+trxDate+"\",\r\n"+		
					"  \"clientId\": \""+clientId+"\",\r\n"	+	
					"  \"partnerId\": \""+partnerId+"\",\r\n"	+
					"  \"items\": ["+
					 "{"+
					 	"\"productKey\": \""+productkey1+"\",\r\n"+
					 	"\"channelKey\": \""+channelKey1+"\",\r\n"+
					 	"\"price\": \""+price+"\",\r\n"+
					 	"\"quantity\": \""+quantity+"\"\r\n"+
					 "},"+
					 "{"+
					 	"\"productKey\": \""+productkey2+"\",\r\n"+	
					 	"\"channelKey\": \""+channelKey2+"\",\r\n"+	
					 	"\"price\": \""+price+"\",\r\n"+
					 	"\"quantity\": \""+quantity+"\"\r\n"+
					 	"},"+
					 "{"+
					 	"\"productKey\": \""+productkey2+"\",\r\n"+	
					 	"\"channelKey\": \""+channelKey2+"\",\r\n"+	
					 	"\"price\": \""+price+"\",\r\n"+
					 	"\"quantity\": \""+quantity+"\"\r\n"+
				      "}]}";
		
		return apiBody;
		
	}
	
	
	
	public String TransactionAPISingleBody(String tranxId, String clientId, String trxDate, String quantity,
			String price, String productkey1, String channelKey1,String partnerId) {

		if (isNullOrEmpty(tranxId)) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + "" + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
			
		}
		if(tranxId.equalsIgnoreCase("without"))
		{
			apiBody = "{\r\n" + " \r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "  \"clientId\": \"" + clientId + "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \""
					+ productkey1 + "\",\r\n" + "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price
					+ "\",\r\n" + "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
			
		}
		
		else if (isNullOrEmpty(clientId)) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + ""
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		}
		if(clientId.equalsIgnoreCase("without")) {
			
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\""
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		
		}
		else if (isNullOrEmpty(trxDate)) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + "" + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		}
		else if (trxDate.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		}
		
		else if (isNullOrEmpty(quantity)) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + "" + "\"\r\n" + "}]}";
		}
		else if (quantity.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+  "}]}";
		}
		else if (price=="0") {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		} 
		else if(price.equalsIgnoreCase("without"))
		{
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" 
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		}
		
		else if (isNullOrEmpty(productkey1)) {
	
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + "" + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
			
		}
		else if(productkey1.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
			
		}
		else if (isNullOrEmpty(channelKey1)) {
			
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + "" + "\",\r\n"
					+ "\"channelKey\": \"" + "" + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		} 
		else if(channelKey1.equalsIgnoreCase("without")) {
			
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+  "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		}
else if (isNullOrEmpty(partnerId)) {
			
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		} 
		
		else if (partnerId.contains("invalid")) {
			
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		} 
      else if (partnerId.equalsIgnoreCase("without")) {
			
			apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		} 
		
		else {
		apiBody = "{\r\n" + " \r\n" + "\"txId\": \"" + tranxId + "\",\r\n" + "  \"date\": \"" + trxDate + "\","
					+ "\r\n" + "  \"partnerId\": \"" + partnerId + "\","
					+ "\r\n" + "  \"clientId\": \"" + clientId
					+ "\",\r\n" + "  \"items\": [" + "{" + "\"productKey\": \"" + productkey1 + "\",\r\n"
					+ "\"channelKey\": \"" + channelKey1 + "\",\r\n" + "\"price\": \"" + price + "\",\r\n"
					+ "\"quantity\": \"" + quantity + "\"\r\n" + "}]}";
		}

		return apiBody;

	}
	
	public String RedeeemAPIBody(String receiptId,String earnTxdate,String debitorId,String amount,String desc) {
			 if (isNullOrEmpty(receiptId)) {
				 
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +""+"\",\r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else if(receiptId.equalsIgnoreCase("without"))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
				 
			 }
			 else if(isNullOrEmpty(earnTxdate))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"date\": \"" +""+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else if(earnTxdate.equalsIgnoreCase("without"))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else if(isNullOrEmpty(debitorId))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"debitorId\": \"" +""+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else if(debitorId.equalsIgnoreCase("without"))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else if(isNullOrEmpty(amount))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"amount\":\"" +""+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else if(amount.equalsIgnoreCase("without"))
			 {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
			 }
			 else {
				 apiBody="{\r\n"
						 + "  \r\n"+
						 " \"receiptId\":\"" +receiptId+"\",\r\n"+
						  "\"date\": \"" +earnTxdate+"\",\r\n"+
						  "\"debitorId\": \"" +debitorId+"\",\r\n"+
						  "\"amount\":\"" +amount+"\",\r\n"+
						  "\"description\": \"" +desc+"\"\r\n"+
						"}";
				  
			 }
		
		return apiBody;
	}
	
	public String AjustmentAPIBody(String receiptId,String earnTxdate,String amount,String debitorId,String desc) {

		
		 if(isNullOrEmpty(receiptId))
		 {
			 apiBody="{\r\n"
					 + "  \r\n"+
					  "\"date\": \"" +earnTxdate+"\",\r\n"+
					  "\"amount\":\"" +amount+"\",\r\n"+
					  "\"debitorId\":\"" +debitorId+"\",\r\n"+  //commneted because of error
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			 
		 }
		 else if(isNullOrEmpty(earnTxdate))
		 {
			 apiBody="{\r\n"
					 + "  \r\n"+
					 " \"receiptId\":\"" +receiptId+"\",\r\n"+
					  "\"amount\":\"" +amount+"\",\r\n"+
					  "\"debitorId\":\"" +debitorId+"\",\r\n"+  //commneted because of error
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		 }
		 else if(isNullOrEmpty(amount))
		 {
			 apiBody="{\r\n"
					 + "  \r\n"+
					 " \"receiptId\":\"" +receiptId+"\",\r\n"+
					  "\"date\": \"" +earnTxdate+"\",\r\n"+
					  "\"debitorId\":\"" +debitorId+"\",\r\n"+  //commneted because of error
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		 }
		 else if(isNullOrEmpty(debitorId))
		 {
			 apiBody="{\r\n"
					 + "  \r\n"+
					 " \"receiptId\":\"" +receiptId+"\",\r\n"+
					  "\"date\": \"" +earnTxdate+"\",\r\n"+
					  "\"amount\":\"" +amount+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		 }
		 else {
			 apiBody="{\r\n"
					 + "  \r\n"+
					 " \"receiptId\":\"" +receiptId+"\",\r\n"+
					  "\"date\": \"" +earnTxdate+"\",\r\n"+
					  "\"amount\":\"" +amount+"\",\r\n"+
					  "\"debitorId\":\"" +debitorId+"\",\r\n"+  //commneted because of error
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			 
		 }
		return apiBody;
	}
	
	public String TranscationReversalAPIBody(String transId,String date,String key,String desc,String debitorId) {
		
		if(isNullOrEmpty(transId))
		 {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +""+"\",\r\n"+
					 "\"clientId\":\"" +key+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		 }
		else if(isNullOrEmpty(date))
		{
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"clientId\":\"" +key+"\",\r\n"+
					  "\"date\": \"" +""+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		}
		else if(isNullOrEmpty(key)) {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"clientId\":\"" +""+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		}
		else if (transId.equalsIgnoreCase("without")) {
			apiBody="{\r\n"
					 + "  \r\n"+
					  "\"clientId\":\"" +key+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		else if (date.equalsIgnoreCase("without")) {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"clientId\":\"" +key+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		else if (key.equalsIgnoreCase("without")) {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		else 
		{
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"clientId\":\"" +key+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"debitorId\": \"" +debitorId+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		return apiBody;
	}
	
public String TranscationRedemptionReversalAPIBody(String transId,String date,String type,String desc) {
		
		if(isNullOrEmpty(transId))
		 {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +""+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"pointsType\":\"" +type+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		 }
		else if(isNullOrEmpty(date))
		{
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"date\": \"" +""+"\",\r\n"+
					  "\"pointsType\":\"" +type+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		}
		else if(isNullOrEmpty(type)) {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"pointsType\":\"" +""+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
		}
		else if (transId.equalsIgnoreCase("without")) {
			apiBody="{\r\n"
					 + "  \r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"pointsType\":\"" +type+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		else if (date.equalsIgnoreCase("without")) {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"pointsType\":\"" +type+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		else if (type.equalsIgnoreCase("without")) {
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		else 
		{
			apiBody="{\r\n"
					 + "  \r\n"+
					 " \"txId\":\"" +transId+"\",\r\n"+
					  "\"date\": \"" +date+"\",\r\n"+
					  "\"pointsType\":\"" +type+"\",\r\n"+
					  "\"description\": \"" +desc+"\"\r\n"+
					"}";
			
		}
		return apiBody;
	}

	public String PointTransferAPIBody(String date, String receiptId, String debitorId, String amount,String description) {
		if (isNullOrEmpty(date)) {
			
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + "" + "\",\r\n" + "\"receiptId\": \"" + receiptId + "\",\r\n"
					+ "\"debitorId\":\"" + debitorId + "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n"
					+ "\"description\": \"" + description + "\"\r\n" + "}";
		} else if (date.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + "  \r\n" + "\"receiptId\": \"" + receiptId + "\",\r\n" + "\"debitorId\":\"" + debitorId
					+ "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n" + "\"description\": \"" + description + "\"\r\n"
					+ "}";

		} else if (isNullOrEmpty(receiptId)) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"receiptId\": \"" + "" + "\",\r\n"
					+ "\"debitorId\":\"" + debitorId + "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n"
					+ "\"description\": \"" + description + "\"\r\n" + "}";
		} else if (receiptId.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"debitorId\":\"" + debitorId
					+ "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n" + "\"description\": \"" + description + "\"\r\n"
					+ "}";

		} else if (isNullOrEmpty(debitorId)) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"receiptId\": \"" + receiptId
					+ "\",\r\n" + "\"debitorId\":\"" + "" + "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n"
					+ "\"description\": \"" + description + "\"\r\n" + "}";
		} else if (debitorId.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"receiptId\": \"" + receiptId
					+ "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n" + "\"description\": \"" + description + "\"\r\n"
					+ "}";
		} else if (isNullOrEmpty(amount)) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"receiptId\": \"" + receiptId
					+ "\",\r\n" + "\"debitorId\":\"" + debitorId + "\",\r\n" + "\"amount\":\"" + "" + "\",\r\n"
					+ "\"description\": \"" + description + "\"\r\n" + "}";
		} else if (amount.equalsIgnoreCase("without")) {

			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"receiptId\": \"" + receiptId
					+ "\",\r\n" + "\"debitorId\":\"" + debitorId + "\",\r\n" + "\"description\": \"" + description
					+ "\"\r\n" + "}";
		} else {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"receiptId\": \"" + receiptId
					+ "\",\r\n" + "\"debitorId\":\"" + debitorId + "\",\r\n" + "\"amount\":\"" + amount + "\",\r\n"
					+ "\"description\": \"" + description + "\"\r\n" + "}";

		}
		return apiBody;
	}
	
	public String MemberMergeAPIBody(String sourceKey,String targetKey) {

		apiBody="{\r\n"
				 + "  \r\n"+
				 "\"sourceKey\": \""+sourceKey+"\",\r\n"+
				 "\"targetKey\": \""+targetKey+"\"\r\n"+
				 "}";
				 return apiBody;
	}
	public String AddMemberCard(String description,String key,String membershipKey,String name,String status,String validSince,String validUntil)
	{
		apiBody="{\r\n"
				+ "  \"description\": \""+description+"\",\r\n"
				+ "  \"key\": \""+key+"\",\r\n"
				+ "  \"membershipKey\": \""+membershipKey+"\",\r\n"
				+ "  \"name\": \""+name+"\",\r\n"
				+ "  \"status\": \""+status+"\",\r\n"
				+ "  \"validSince\": \""+validSince+"\",\r\n"
				+ "  \"validUntil\": \""+validUntil+"\"\r\n"
				+ "}";
		return apiBody;
	}
	
	public String AddLegalEntity(String userId,String prefix,String firstName,String lastName,String address1)
	{
		apiBody="{\r\n"
				+ "    \"UserId\": \""+userId+"\",\r\n"
				+ "    \"Properties\": {\r\n"
				+ "      \"Prefix\": \""+prefix+"\",\r\n"
				+ "      \"Firstname\": \""+firstName+"\",\r\n"
				+ "      \"Lastname\": \""+lastName+"\",\r\n"
				+ "      \"Address1\": \""+address1+"\"\r\n"
				+ "    }\r\n"
				+ "  }";
		return apiBody;
	}
	
	public String AddLegalEntityRelation(String description,String key,String membershipKey,String legalEntityKey,String name,String status,String validSince,String validUntil)
	{
		apiBody="{\r\n"
				+ "  \"description\": \""+description+"\",\r\n"
				+ "  \"key\": \""+key+"\",\r\n"
				+ "  \"membershipKey\": \""+membershipKey+"\",\r\n"
				+ "  \"legalEntityKey\": \""+legalEntityKey+"\",\r\n"
				+ "  \"name\": \""+name+"\",\r\n"
				+ "  \"status\": \""+status+"\",\r\n"
				+ "  \"validSince\": \""+validSince+"\",\r\n"
				+ "  \"validUntil\": \""+validUntil+"\"\r\n"
				+ "}";
	
		return apiBody;
	}
	
	public String CreateVoucherType(String name,String description,String tyepOf,String value)
	{
		apiBody="{\r\n"
				+ "  \"name\": \""+name+"\",\r\n"
				+ "  \"description\": \""+description+"\",\r\n"
				+ "  \"of\": \""+tyepOf+"\", \r\n"
				+ "  \"value\": \""+value+"\"\r\n"
				+ "}";
		return apiBody; 
	}
	
	public String IssueVoucher(String type,String token,String validFrom,String validUntil,String issuedTo,String issuedAt)
	{
		apiBody="[\r\n"
				+ "  {\r\n"
				+ "    \"type\": \""+type+"\",\r\n"
				+ "    \"token\": \""+token+"\",\r\n"
				+ "    \"validFrom\": \""+validFrom+"\",\r\n"
				+ "    \"validUntil\": \""+validUntil+"\",\r\n"
				+ "    \"issuedTo\": \""+issuedTo+"\",\r\n"
				+ "    \"issuedAt\": \""+issuedAt+"\"\r\n"
				+ "  }]";
		return apiBody; 
	}
	
	
	public String PointTransferReversalAPIBody(String txId,String date, String pointType,String description) {
		if (isNullOrEmpty(txId)) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"txId\": \"" + "" + "\",\r\n"
					+ "\"pointsType\":\"" + pointType + "\",\r\n" + "\"description\": \"" + description + "\"\r\n"
					+ "}";
		} else if (txId.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"pointsType\":\"" + pointType
					+ "\",\r\n" + "\"description\": \"" + description + "\"\r\n" + "}";

		} else if (isNullOrEmpty(date)) {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + "" + "\",\r\n" + "\"txId\": \"" + txId + "\",\r\n"
					+ "\"pointsType\":\"" + pointType + "\",\r\n" + "\"description\": \"" + description + "\"\r\n"
					+ "}";
		} else if (date.equalsIgnoreCase("without")) {
			apiBody = "{\r\n" + "  \r\n" + "\"txId\": \"" + txId + "\",\r\n" + "\"pointsType\":\"" + pointType
					+ "\",\r\n" + "\",\r\n" + "\"description\": \"" + description + "\"\r\n" + "}";

		} else {
			apiBody = "{\r\n" + "  \r\n" + " \"date\":\"" + date + "\",\r\n" + "\"txId\": \"" + txId + "\",\r\n"
					+ "\"pointsType\":\"" + pointType + "\",\r\n" + "\"description\": \"" + description + "\"\r\n"
					+ "}";

		}
		return apiBody;
	}
	
	
	public String BulkImportCard(String key,String Name,String membershipKey,String legalEntityKey,String validSince)
	{
		apiBody="[\r\n"
				+ " {\r\n"
				+ "\"Key\": \""+key+"\",\r\n"
				+ "\"Name\": \""+Name+"\",\r\n"
				+ "\"Description\": \"Card Test JSON\",\r\n"
				+ "\"MembershipKey\": \""+membershipKey+"\",\r\n"
				+ "\"LegalEntityKey\": \""+legalEntityKey+"\",\r\n"
				+ "\"ValidSince\": \""+validSince+"\",\r\n"
				+ "\"ValidUntil\": \"2029-01-01T00:00:00Z\",\r\n"
				+ "\"Status\": \"VALID\"\r\n"
				+ "}\r\n"
				+ "]";
		
        	return apiBody;
	}
	
	public String BulkImportLegalEntity(String strKey)
	{
		apiBody="[{\r\n"
				+ "    \"UserId\": \""+strKey+"\",\r\n"
				+ "   \"MembershipType\": \"DE-Member\",\r\n"
				+ "   \"Properties\": {\r\n"
				+ "     \"Prefix\": \"Mr\",\r\n"
				+ "     \"Firstname\": \"Enrollment\",\r\n"
				+ "     \"Lastname\": \"Test\",\r\n"
				+ "     \"Email\": \"test.automail@lp.com\"\r\n"
				+ "  }\r\n"
				+ " }]";
		return apiBody;
	}
	
	//MembershipKey,PointsType,TargetMshipKey,AccountId,DebitorId,Date,ReceiptId,Amount,Description
	public String BulkImportPointTransfer(String sourceKey,String pointsType,String targetMshipKey,String accountId,String debitorId,String date,String receiptId,String amount,String description)
	{
		
		apiBody="MembershipKey,PointsType,TargetMshipKey,AccountId,DebitorId,Date,ReceiptId,Amount,Description\r\n"
				+ ""+sourceKey+","+pointsType+","+targetMshipKey+","+accountId+","+debitorId+","+date+","+receiptId+","+amount+","+description+"";
        	return apiBody;
	}
	
	public String BulkImportProducts(String currentKey,String channelKey,String sid,String id,String name,String category,String currentKey1,String Id1,String name1 )
	{
		//apiBody="Key,ChannelKey,Sid,Id,Name,Category\r\n"
			//	+ "{{currentKey}}_Prod1,RETAIL,RETAIL-PROD:1,{{currentKey}}_Prod1,\"Product Test\",Accessories\r\n"
				//+ "{{currentKey}}_Prod2,Failing Channel,RETAIL-PROD:1,{{currentKey}}_Prod2,\"Product Test 2\",Accessories";
		//return apiBody;
		
		apiBody="Key,ChannelKey,Sid,Id,Name,Category\r\n"
				+ ""+currentKey+","+channelKey+","+sid+","+id+","+name+","+category+"\r\n"
				+ ""+currentKey1+","+channelKey+","+sid+","+Id1+","+name1+","+category+"";
		return apiBody;
		
		
	}

	public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
