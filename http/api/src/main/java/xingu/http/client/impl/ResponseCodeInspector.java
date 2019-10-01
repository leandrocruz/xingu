package xingu.http.client.impl;

import java.util.HashMap;
import java.util.Map;

import xingu.http.client.HttpException;
import xingu.http.client.HttpResponse;
import xingu.http.client.ResponseInspector;

public class ResponseCodeInspector
	implements ResponseInspector
{
	private int		expected;

	private String	msg;
	
	private static final Map<Integer, ResponseCodeInspector> byCode = new HashMap<Integer, ResponseCodeInspector>();

	private ResponseCodeInspector(int code, String msg)
	{
		this.expected = code;
		this.msg      = msg;
	}

	public static final ResponseCodeInspector forCode(int code, String msg)
	{
		if(msg == null)
		{
			ResponseCodeInspector result = byCode.get(code);
			if(result == null)
			{
				result = new ResponseCodeInspector(code, null);
				byCode.put(code, result);
			}
			return result;
		}
		return new ResponseCodeInspector(code, msg);
	}
	
	
	private String getErrorMessage(int code)
	{
		if(msg != null)
		{
			return msg;
		}		
		
		String error = "Código de resposta inesperado. Esperava por: " + expected + ", mas foi obtido: " + code;
		
		if(codeString.containsKey(code))
		{
			error = "Código de resposta inesperado - " + codeString.get(code) + ".";
		}
		
		return error; 
	}

	@Override
	public void throwErrorIf(HttpResponse res)
		throws Exception
	{
		int code = res.getCode();
		if(expected != code)
		{
			String msg = getErrorMessage(code);
			throw new HttpException(msg);
		}
	}
	
	private static Map<Integer, String> codeString = new HashMap<Integer, String>();
	
	static
	{
		codeString.put(100, "100 Continue");
		codeString.put(101, "101 Switching Protocols");
		codeString.put(102, "102 Processing (WebDAV)");
		
		codeString.put(200, "200 OK");
		codeString.put(201, "201 Created");
		codeString.put(202, "202 Accepted");
		codeString.put(203, "203 Não autorizado");
		codeString.put(204, "204 Sem conteúdo");
		codeString.put(205, "205 Reset Content");
		codeString.put(206, "206 Partial Content");
		codeString.put(207, "207 Multi-Status (WebDAV)");
		codeString.put(208, "208 Already Reported (WebDAV)");
		codeString.put(226, "226 IM Used");
		
		codeString.put(300, "300 Multiple choices");
		codeString.put(301, "301 Movido permanentemente");
		codeString.put(302, "302 Encontrado (porém redirecionamento)");
		codeString.put(303, "303 See Other");
		codeString.put(304, "304 Not Modified");
		codeString.put(305, "305 Use Proxy");
		codeString.put(306, "306 (Unused)");
		codeString.put(307, "307 Temporary Redirect");
		codeString.put(308, "308 Permanent Redirect (experimental)");
		
		codeString.put(400, "400 Solicitação inválida");
		codeString.put(401, "401 Não autorizado");
		codeString.put(402, "402 Pagamento necessário");
		codeString.put(403, "403 Proibido");
		codeString.put(404, "404 Não encontrado");
		codeString.put(405, "405 Método não permitido");
		codeString.put(406, "406 Not Acceptable");
		codeString.put(407, "407 Proxy Authentication Required");
		codeString.put(408, "408 Request Timeout");
		codeString.put(409, "409 Conflict");
		codeString.put(410, "410 Gone");
		codeString.put(411, "411 Length Required");
		codeString.put(412, "412 Precondition Failed");
		codeString.put(413, "413 Request Entity Too Large");
		codeString.put(414, "414 Request-URI Too Long");
		codeString.put(415, "415 Unsupported Media Type");
		codeString.put(416, "416 Requested Range Not Satisfiable");
		codeString.put(417, "417 Expectation Failed");
		codeString.put(418, "418 I'm a teapot (RFC 2324)");
		codeString.put(420, "420 Enhance Your Calm (Twitter)");
		codeString.put(422, "422 Unprocessable Entity (WebDAV)");
		codeString.put(423, "423 Locked (WebDAV)");
		codeString.put(424, "424 Failed Dependency (WebDAV)");
		codeString.put(425, "425 Reserved for WebDAV");
		codeString.put(426, "426 Upgrade Required");
		codeString.put(428, "428 Precondition Required");
		codeString.put(429, "429 Too Many Requests");
		codeString.put(431, "431 Request Header Fields Too Large");
		codeString.put(444, "444 No Response (Nginx)");
		codeString.put(449, "449 Retry With (Microsoft)");
		codeString.put(450, "450 Blocked by Windows Parental Controls (Microsoft)");
		codeString.put(451, "451 Unavailable For Legal Reasons");
		codeString.put(499, "499 Client Closed Request (Nginx)");
		
		codeString.put(500, "500 Erro interno no servidor");
		codeString.put(501, "501 Não implementado");
		codeString.put(502, "502 Gateway inválido");
		codeString.put(503, "503 Serviço indisponível");
		codeString.put(504, "504 Tempo excedido do gateway");
		codeString.put(505, "505 HTTP Version Not Supported");
		codeString.put(506, "506 Variant Also Negotiates (Experimental)");
		codeString.put(507, "507 Insufficient Storage (WebDAV)");
		codeString.put(508, "508 Loop Detected (WebDAV)");
		codeString.put(509, "509 Bandwidth Limit Exceeded (Apache)");
		codeString.put(510, "510 Not Extended");
		codeString.put(511, "511 Network Authentication Required");
		codeString.put(598, "598 Network read timeout error");
		codeString.put(599, "599 Network connect timeout erro");
	}
}