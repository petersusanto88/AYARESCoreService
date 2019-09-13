package com.webservicemaster.iirs.utility;

import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Service;

@Service
public class SOAP {

	private String xml = "";
	  private String trace_number = ""; private String result_code = ""; private String result_name = ""; private String result_note = "";
	  private String result_timestamp = ""; private String alto_trace_number = ""; private String result_balance = "";
	  private String data = ""; private String desc = "";
	
	  /*public SOAP(String paramString) {
		    this.xml = paramString;
		  }*/
    
    
    public void parseSOAPMessage( String xml ) throws Exception {
    	this.xml = xml;
        StringReader localStringReader = new StringReader(this.xml);
        SAXBuilder localSAXBuilder = new SAXBuilder(false);
        localSAXBuilder.setValidation(false);
        Document localDocument = localSAXBuilder.build(localStringReader);
        Element localElement1 = localDocument.getRootElement();
        
        Element localElement2 = (Element)localElement1.getChildren().get(0);

        Element localElement3 = (Element)localElement2.getChildren().get(0);

        Element localElement4 = (Element)localElement3.getChildren().get(0);

        List localList1 = localElement4.getChildren("item");
        System.out.println("item count = " + localList1.size());
        
        for (int i = 0; i < localList1.size(); i++) {
          Element localElement5 = (Element)localList1.get(i);
          Element localElement6 = localElement5.getChild("key");
          Element localElement7 = localElement5.getChild("value");
          String str1 = localElement6.getText();
          String str2 = localElement7.getText();

          List localList2 = localElement7.getAttributes();

          String str3 = "";
          for (int j = 0; j < localList2.size(); j++) {
            Attribute localAttribute = (Attribute)localList2.get(j);
            if (localAttribute.getName().toLowerCase().compareTo("type") == 0) {
              str3 = localAttribute.getValue();
              break;
            }
          }
          if (str3.compareTo("ns2:Map") == 0)
          {
            List localList3 = localElement7.getChildren("item");
            System.out.println("another item count = " + localList3.size());
            for (int k = 0; k < localList3.size(); k++) {
              Element localElement8 = (Element)localList3.get(k);
              str1 = localElement8.getChildText("key");
              str2 = localElement8.getChildText("value");

              this.data = (this.data + str1 + "=" + str2 + "|");
              this.desc = (this.desc + str1 + "=" + str2 + "|");

              if (str1.toLowerCase().compareTo("result_code") == 0)
                this.result_code = str2;
              else if (str1.toLowerCase().compareTo("alto_trace_number") == 0)
                this.alto_trace_number = str2;
              else if (str1.toLowerCase().compareTo("result_balance") == 0)
                this.result_balance = str2;
              else if (str1.toLowerCase().compareTo("result_note") == 0)
                this.result_note = str2;
              else if (str1.toLowerCase().compareTo("result_timestamp") == 0)
                this.result_timestamp = str2;
            }
          }
          else
          {
            this.data = (this.data + str1 + "=" + str2 + "|");
            if (str1.toLowerCase().compareTo("result_name") == 0)
              this.desc = (this.desc + "Account Owner=" + str2 + "|");
            else
              this.desc = (this.desc + str1 + "=" + str2 + "|");
            if (str1.toLowerCase().compareTo("result_code") == 0)
              this.result_code = str2;
            else if (str1.toLowerCase().compareTo("trace_number") == 0)
              this.trace_number = str2;
            else if (str1.toLowerCase().compareTo("result_name") == 0)
              this.result_name = str2;
            else if (str1.toLowerCase().compareTo("result_note") == 0)
              this.result_note = str2;
            else if (str1.toLowerCase().compareTo("result_timestamp") == 0)
              this.result_timestamp = str2;
          }
        }
        
      }

    public String getResultCode()
    {
        return result_code;
    }

    public String getTraceNumber()
    {
        return trace_number;
    }

    public String getAltoTraceNumber()
    {
        return alto_trace_number;
    }

    public String getResultName()
    {
        return result_name;
    }

    public String getResultNote()
    {
        return result_note;
    }

    public String getResultTimeStamp()
    {
        return result_timestamp;
    }

    public String getResultBalance()
    {
        return result_balance;
    }

    public String getDataOriginal()
    {
        return data;
    }

    public String getDataDesc()
    {
        return desc;
    }

    
}
