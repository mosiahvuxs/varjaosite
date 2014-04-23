package br.com.varjaosite.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.com.topsys.util.TSUtil;
import br.com.topsys.web.util.TSFacesUtil;

public class JasperUtil {

	public void gerarRelatorio(String jasper, Map<String, Object> parametros, String nomePdf) throws JRException {

		InputStream stream = null;

		stream = this.findReport(jasper);

		//jasper = TSFacesUtil.getServletContext().getRealPath("WEB-INF" + File.separator + "relatorios" + File.separator + jasper);

		JasperPrint impressao = JasperFillManager.fillReport(stream, parametros);

		if (!TSUtil.isEmpty(impressao) && !TSUtil.isEmpty(impressao.getPages()) && impressao.getPages().size() > 0) {

			ExternalContext econtext = TSFacesUtil.getFacesContext().getExternalContext();

			HttpServletResponse response = (HttpServletResponse) econtext.getResponse();

			response.setContentType("APPLICATION/PDF");

			response.setHeader("Content-Disposition", "attachment; filename=\"" + nomePdf + ".pdf\"");

			try {

				JasperExportManager.exportReportToPdfStream(impressao, response.getOutputStream());

			} catch (JRException e) {

				TSFacesUtil.addErrorMessage(e.getMessage());

				e.printStackTrace();

			} catch (IOException e) {

				TSFacesUtil.addErrorMessage(e.getMessage());

				e.printStackTrace();

			}

			TSFacesUtil.getFacesContext().responseComplete();

		} else {

			TSFacesUtil.addInfoMessage("Não foi possível gerar o PDF.");
		}

	}

	private InputStream findReport(String nomeArquivo) {

		final ServletContext servletContext = (ServletContext) TSFacesUtil.getFacesContext().getExternalContext().getContext();

		InputStream is = servletContext.getResourceAsStream("WEB-INF" + File.separator + "relatorios" + File.separator + nomeArquivo);

		return is;
	}

}
