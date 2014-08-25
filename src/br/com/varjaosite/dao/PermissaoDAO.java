package br.com.varjaosite.dao;

import java.util.List;

import br.com.topsys.database.TSDataBaseBrokerIf;
import br.com.topsys.database.factory.TSDataBaseBrokerFactory;
import br.com.topsys.exception.TSApplicationException;
import br.com.varjaosite.model.Grupo;
import br.com.varjaosite.model.Permissao;

public class PermissaoDAO {

	@SuppressWarnings("unchecked")
	public List<Permissao> pesquisar(Grupo model) {

		TSDataBaseBrokerIf broker = TSDataBaseBrokerFactory.getDataBaseBrokerIf();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT M.ID, M.DESCRICAO, M.FLAG_ATIVO, M.MANAGED_BEAN_RESET, M.ORDEM, M.URL, M.MENU_ID, (SELECT MP.DESCRICAO FROM MENUS MP WHERE MP.ID = M.ID) PAI, P.ID, P.FLAG_INSERIR, P.FLAG_ALTERAR, P.FLAG_EXCLUIR, P.GRUPO_ID FROM PERMISSOES P, MENUS M, GRUPOS G WHERE P.MENU_ID = M.ID AND P.GRUPO_ID = G.ID AND M.FLAG_ATIVO AND P.GRUPO_ID = ? ORDER BY M.ORDEM, M.DESCRICAO");

		broker.setSQL(sql.toString());

		broker.set(model.getId());

		return broker.getCollectionBean(Permissao.class, "menu.id", "menu.descricao", "menu.flagAtivo", "menu.managedBeanReset", "menu.ordem", "menu.url", "menu.menuPai.id", "menu.menuPai.descricao", "id", "flagInserir", "flagAlterar", "flagExcluir", "grupo.id");
	}

	public void inserir(Permissao model, TSDataBaseBrokerIf broker) throws TSApplicationException {

		model.setId(broker.getSequenceNextValue("permissoes_id_seq"));

		broker.setPropertySQL("permissaodao.inserir",

		model.getId(), model.getFlagAlterar(), model.getFlagExcluir(), model.getFlagInserir(), model.getGrupo().getId(), model.getMenu().getId());

		broker.execute();

	}

	public void excluirPorGrupo(Grupo model, TSDataBaseBrokerIf broker) throws TSApplicationException {

		broker.setPropertySQL("permissaodao.excluirPorGrupo", model.getId());

		broker.execute();

	}

}
