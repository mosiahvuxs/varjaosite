package br.com.varjaosite.faces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.topsys.constant.TSConstant;
import br.com.topsys.util.TSCryptoUtil;
import br.com.topsys.util.TSUtil;
import br.com.topsys.web.faces.TSMainFaces;
import br.com.topsys.web.util.TSFacesUtil;
import br.com.varjaosite.dao.ClienteDAO;
import br.com.varjaosite.model.Autenticacao;
import br.com.varjaosite.model.Cliente;
import br.com.varjaosite.util.Constantes;

@ViewScoped
@ManagedBean(name = "loginFaces")
public class LoginFaces extends TSMainFaces {

	private Cliente cliente;

	private String clienteId;

	private String midiaId;

	public LoginFaces() {

		String clienteId = TSFacesUtil.getRequestParameter("cliente");

		String midiaId = TSFacesUtil.getRequestParameter("midia");

		if (!TSUtil.isEmpty(clienteId) && !TSUtil.isEmpty(midiaId)) {

			String clienteIdDescriptografado;

			String midiaIdDescriptografado;

			try {

				clienteIdDescriptografado = TSCryptoUtil.desCriptografar(clienteId);

				midiaIdDescriptografado = TSCryptoUtil.desCriptografar(midiaId);

				if (TSUtil.isNumeric(clienteIdDescriptografado) && TSUtil.isNumeric(midiaIdDescriptografado)) {

					this.clienteId = clienteId;

					this.midiaId = midiaId;

				} else {

					this.sair();
				}

			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
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

		} else {

			this.initObjetos();
		}

	}

	private void initObjetos() {

		this.setCliente(new Cliente(new Autenticacao()));
		this.setMidiaId(null);
		this.setClienteId(null);
	}

	private boolean validaCampos() {

		boolean validado = true;

		if (TSUtil.isEmpty(this.cliente.getAutenticacao().getLogin())) {

			super.addErrorMessage("Login: obrigatório.");

			validado = false;
		}

		if (TSUtil.isEmpty(this.cliente.getAutenticacao().getSenha())) {

			super.addErrorMessage("Senha: obrigatório.");

			validado = false;
		}

		return validado;
	}

	public String autenticar() {

		if (this.validaCampos()) {

			Cliente cliente = new Cliente(this.cliente.getAutenticacao());

			cliente.getAutenticacao().setSenha(TSCryptoUtil.gerarHash(cliente.getAutenticacao().getSenha(), TSConstant.CRIPTOGRAFIA_MD5));

			this.cliente = new ClienteDAO().obterPorLoginSenha(cliente);

			if (TSUtil.isEmpty(this.cliente)) {

				this.cliente = cliente;

				super.addErrorMessage("Dados inválidos.");

			} else {

				if (TSUtil.isEmpty(clienteId) && TSUtil.isEmpty(this.midiaId)) {

					super.addObjectInSession(Constantes.USUARIO_CONECTADO, this.cliente);

					return Constantes.CLIPPING;

				} else {

					this.redirecionarVisualizacaoClipping(this.clienteId, this.midiaId);
				}

			}
		}

		return null;
	}

	private void redirecionarVisualizacaoClipping(String clienteId, String midiaId) {

		try {

			TSFacesUtil.getResponse().sendRedirect("/visualizacao.xhtml?cliente=" + clienteId + "&midia=" + midiaId);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String sair() {

		this.clearFields();

		TSFacesUtil.removeObjectInSession(Constantes.USUARIO_CONECTADO);

		TSFacesUtil.getRequest().getSession().invalidate();

		return Constantes.INDEX;

	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getClienteId() {
		return clienteId;
	}

	public void setClienteId(String clienteId) {
		this.clienteId = clienteId;
	}

	public String getMidiaId() {
		return midiaId;
	}

	public void setMidiaId(String midiaId) {
		this.midiaId = midiaId;
	}

}
