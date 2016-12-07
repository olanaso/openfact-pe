package org.openfact.pe.services.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.openfact.common.converts.DocumentUtils;
import org.openfact.models.ModelException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SunatResponseUtils {

	public static Map<String, String> byteResponseToMap(byte[] data) throws Exception {
		Map<String, String> response = new HashMap<>();
		try {
			data = unzip(data);
			Document e = DocumentUtils.byteToDocument(data);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String responseCode = (String) xPath.evaluate(
					"/*[local-name()=\'ApplicationResponse\']/*[local-name()=\'DocumentResponse\']/*[local-name()=\'Response\']/*[local-name()=\'ResponseCode\']/text()",
					e.getDocumentElement(), XPathConstants.STRING);
			String description = (String) xPath.evaluate(
					"/*[local-name()=\'ApplicationResponse\']/*[local-name()=\'DocumentResponse\']/*[local-name()=\'Response\']/*[local-name()=\'Description\']/text()",
					e.getDocumentElement(), XPathConstants.STRING);
			NodeList warningsNode = (NodeList) xPath.evaluate(
					"/*[local-name()=\'ApplicationResponse\']/*[local-name()=\'Note\']", e.getDocumentElement(),
					XPathConstants.NODESET);
			// ArrayList lstWarnings = new ArrayList();
			// for (int i = 0; i < warningsNode.getLength(); ++i) {
			// Node show = warningsNode.item(i);
			// lstWarnings.add(show.getTextContent());
			// }
			int ErrorCode = (new Integer(responseCode)).intValue();
			if (ErrorCode != 0 && (ErrorCode < 100 || ErrorCode > 399) && ErrorCode <= 4000) {
				response.put("ACCEPTED BY SUNAT", "NO");
			} else {
				response.put("ACCEPTED BY SUNAT", "YES");
			}
			response.put("RESPONSE CODE", responseCode);
			response.put("MESSAGE", description);
		} catch (XPathExpressionException e) {
			response.put("ACCEPTED BY SUNAT", "NO");
			response.put("MESSAGE", e.getMessage());
		}
		return response;
	}

	public static Map<String, String> faultToMap(String... soapFault) {
		Map<String, String> response = new HashMap<>();
		String faultCode = soapFault[0];
		String message = "";
		String retCode = getErrorCode(faultCode);
		String faultString = soapFault[1];
		int intCode = -1;
		try {
			if ("".equals(retCode)) {
				intCode = (new Integer(faultString)).intValue();
			} else {
				intCode = (new Integer(retCode)).intValue();
			}
			Integer errorCode = Integer.valueOf(soapFault[1].indexOf("Detalle"));
			if (errorCode.intValue() > 0) {
				message = soapFault[1].substring(0, errorCode.intValue() - 1);
			} else {
				message = soapFault[1];
			}
		} catch (Throwable e) {
			message = "Failed to invoke Service: " + e.getMessage();
		}
		response.put("ACCEPTED BY SUNAT", "NO");
		response.put("MESSAGE", message);
		return response;
	}

	private static String getErrorCode(String faultCode) {
		Integer length = Integer.valueOf(faultCode.length());
		String errorCode = "";

		for (int i = 0; i < length.intValue(); ++i) {
			if (Character.isDigit(faultCode.charAt(i))) {
				errorCode = errorCode + faultCode.charAt(i);
			}
		}
		return errorCode;
	}

	private static byte[] unzip(byte[] data) {
		try {
			ByteArrayInputStream e = new ByteArrayInputStream(data);
			ZipInputStream srcIs = new ZipInputStream(e);
			ByteArrayOutputStream destOs = new ByteArrayOutputStream();
			ZipEntry entry = null;

			while (true) {
				do {
					if ((entry = srcIs.getNextEntry()) == null) {
						destOs.flush();
						byte[] b2 = destOs.toByteArray();
						destOs.close();
						destOs.close();
						srcIs.close();
						e.close();
						return b2;
					}
				} while (!entry.getName().endsWith(".xml"));

				boolean b = false;
				byte[] buffer = new byte[2048];

				int b1;
				while ((b1 = srcIs.read(buffer)) > 0) {
					destOs.write(buffer, 0, b1);
				}
			}
		} catch (Exception e) {
			throw new ModelException("Error al descomprimir la constancia", e.getCause());
		}
	}

	private static void writeByteToFile(String fileName, byte[] document, boolean response) throws IOException {
		if (!response) {
			FileUtils.writeByteArrayToFile(new File(System.getProperty("user.home") + "/ubl/" + fileName + ".zip"),
					document);
		} else {
			FileUtils.writeByteArrayToFile(new File(System.getProperty("user.home") + "/ubl/R" + fileName + ".zip"),
					document);
		}
	}

}
