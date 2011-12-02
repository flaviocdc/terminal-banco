package br.ufrj.dcc.so.modelo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufrj.dcc.so.modelo.OperacaoFinaceira.Tipo;
import br.ufrj.dcc.so.util.GsonUtil;

public class GeradorUsuariosJSON {

	public static void main(String[] args) {
		OperacaoFinaceira[] opsArray = {
				new OperacaoFinaceira(Tipo.DOC, 10.0),
				new OperacaoFinaceira(Tipo.TRANSFERENCIA, 10.0),
				new OperacaoFinaceira(Tipo.SAQUE, 10.0),
				new OperacaoFinaceira(Tipo.DEPOSITO, 150.0)
		};
		
		List<OperacaoFinaceira> ops = Arrays.asList(opsArray);
		
		Usuario usuario1 = new Usuario("Flavio", "1234-0", "0000-1", "123456");
		usuario1.setOperacoes(ops);
		
		Usuario usuario2 = new Usuario("Magno", "1234-0", "0000-2", "123456");
		usuario2.setOperacoes(ops);
		
		Usuario usuario3 = new Usuario("Augusto", "1234-0", "0000-3", "123456");
		usuario3.setOperacoes(ops);
		
		Map<String, Usuario> map = new HashMap<String, Usuario>();
		map.put(usuario1.getId(), usuario1);
		map.put(usuario2.getId(), usuario2);
		map.put(usuario3.getId(), usuario3);

		
		System.out.println(GsonUtil.gson().toJson(map));
/*		
		Map<String, Usuario> map2 = GsonUtil.gson().fromJson(GsonUtil.gson().toJson(map), new TypeToken<HashMap<String, Usuario>>() { }.getType());
		System.out.println(map2);
*/	}
}
