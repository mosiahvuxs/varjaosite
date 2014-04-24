package br.com.varjaosite.filter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.topsys.util.TSCryptoUtil;
import br.com.varjaosite.util.Utilitarios;

@WebFilter("*.jsp")
public class JspFilter implements Filter {

	public JspFilter() {

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		HttpServletResponse response = (HttpServletResponse) resp;

		String noticia = req.getParameter("noticia");

		if (noticia != null && noticia.length() > 0) {

			String noticiaDecodificada = Utilitarios.decodificar(noticia);

			if (noticiaDecodificada != null && noticiaDecodificada.length() > 0) {

				String cliente = noticiaDecodificada.substring(11, 13);

				String midia = noticiaDecodificada.substring(23, noticiaDecodificada.length() - 1);

				if (cliente != null && midia != null) {

					try {

						response.sendRedirect(request.getContextPath() + "/visualizacao.xhtml?cliente=" + TSCryptoUtil.criptografar(cliente) + "&midia=" + TSCryptoUtil.criptografar(midia) + "&codigoIntegracao=true");

					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalBlockSizeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		if (!response.isCommitted()) {

			chain.doFilter(request, response);

		}
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
