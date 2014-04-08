package br.com.varjaosite.faces;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.StreamedContent;

import br.com.topsys.exception.TSApplicationException;
import br.com.topsys.util.TSCryptoUtil;
import br.com.topsys.util.TSUtil;
import br.com.topsys.web.faces.TSMainFaces;
import br.com.topsys.web.util.TSFacesUtil;
import br.com.varjaosite.dao.ClienteDAO;
import br.com.varjaosite.dao.MidiaDAO;
import br.com.varjaosite.dao.MidiaEnvioDAO;
import br.com.varjaosite.model.Cliente;
import br.com.varjaosite.model.Midia;
import br.com.varjaosite.model.MidiaEnvio;
import br.com.varjaosite.model.Usuario;
import br.com.varjaosite.util.Constantes;
import br.com.varjaosite.util.Utilitarios;

@ViewScoped
@ManagedBean(name = "visualizacaoFaces")
public class VisualizacaoFaces extends TSMainFaces {

	private Midia midia;
	private StreamedContent file;

	public VisualizacaoFaces() {

		try {

			this.carregar();

		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		}
	}

	private String carregar() throws InvalidKeyException, UnsupportedEncodingException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException {

		String clienteId = TSFacesUtil.getRequestParameter("cliente");

		String midiaId = TSFacesUtil.getRequestParameter("midia");

		if (!TSUtil.isEmpty(clienteId) && !TSUtil.isEmpty(midiaId) && TSUtil.isNumeric(TSCryptoUtil.desCriptografar(clienteId)) && TSUtil.isNumeric(TSCryptoUtil.desCriptografar(midiaId))) {

			String clienteIdDescriptografado = TSCryptoUtil.desCriptografar(clienteId);

			String midiaIdDescriptografado = TSCryptoUtil.desCriptografar(midiaId);

			Cliente cliente = new ClienteDAO().obter(new Cliente(Long.valueOf(clienteIdDescriptografado)));

			if (!TSUtil.isEmpty(cliente) && !TSUtil.isEmpty(cliente.getId())) {

				super.addObjectInSession(Constantes.USUARIO_CONECTADO, cliente);

				if (!cliente.getFlagExigeSenha()) {

					if (!this.carregaMidia(Long.valueOf(midiaIdDescriptografado), cliente)) {

						this.redirecionarIndex();

					} else {

						MidiaEnvio midiaEnvio = new MidiaEnvio();

						midiaEnvio.setUsuario(new Usuario());

						midiaEnvio.setCliente(cliente);

						midiaEnvio.setMidia(this.midia);

						try {

							new MidiaEnvioDAO().setarVisualizacao(midiaEnvio);

						} catch (TSApplicationException e) {

							e.printStackTrace();
						}
					}

				} else {

					this.redirecionarLogin(clienteId, midiaId);
				}

			}

		} else {

			this.redirecionarIndex();

		}

		return null;
	}

	private boolean carregaMidia(Long midiaId, Cliente cliente) {

		this.midia = new MidiaDAO().obter(new Midia(midiaId));

		if (!TSUtil.isEmpty(this.midia) && !TSUtil.isEmpty(this.midia.getId())) {
			
			this.getMidia().setCliente(cliente);
			
			this.tratarArquivo();

			return true;
		}

		return false;
	}

	private void tratarArquivo() {

		if (Constantes.AUDIO.equals(this.midia.getTipoMidia().getId())) {

			this.midia.setArquivo(this.midia.getAudio().getArquivo());

		} else if (Constantes.VIDEO.equals(this.midia.getTipoMidia().getId())) {

			this.midia.setArquivo(this.midia.getVideo().getArquivo());

		} else if (Constantes.IMPRESSO.equals(this.midia.getTipoMidia().getId())) {

			this.midia.setArquivo(this.midia.getImpresso().getArquivo());

		} else if (Constantes.WEB.equals(this.midia.getTipoMidia().getId())) {

			this.midia.setArquivo(this.midia.getWeb().getArquivo());
		}
	}

	private void redirecionarIndex() {

		try {

			TSFacesUtil.getResponse().sendRedirect(TSFacesUtil.getRequest().getContextPath() + "/index.xhtml");

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private void redirecionarLogin(String clienteId, String midiaId) {

		try {

			TSFacesUtil.getResponse().sendRedirect(TSFacesUtil.getRequest().getContextPath() + "/login.xhtml?cliente=" + clienteId + "&midia=" + midiaId);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String download() {

		try {

			File arquivo = new File(this.getMidia().getArquivoFormatado());

			byte[] b = Utilitarios.getBytes(arquivo);

			if (!TSUtil.isEmpty(b)) {

				HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

				res.setHeader("Content-disposition", "attachment;filename=" + this.getMidia().getArquivo());

				res.getOutputStream().write(b);

				res.getCharacterEncoding();

				FacesContext.getCurrentInstance().responseComplete();

			} else {

				super.addErrorMessage("Arquivo não encontrado.");
			}

		} catch (IOException e) {

			TSFacesUtil.addErrorMessage("Erro ao gerar arquivo.");
		}

		return null;

	}

	public Midia getMidia() {
		return midia;
	}

	public void setMidia(Midia midia) {
		this.midia = midia;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
