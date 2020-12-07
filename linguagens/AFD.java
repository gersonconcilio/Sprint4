import java.util.*;

public class AFD {
    ArrayList<String> estados;
    char[] alfabeto;
    String estadoInicial;
    ArrayList<String> estadosFinais;
    String[][] transicoes;

    public static class Configuracoes {
        List<String[]> configuracoes;

        public Configuracoes (){
            this.configuracoes = new LinkedList<String[]>();
        }

        public void add(String estado, String fita) {
            this.configuracoes.add(new String[]{estado, fita});
        }

        public void imprimir(){
            for(int i = 0; i < this.configuracoes.size(); i++) {
                System.out.println("(" + this.configuracoes.get(i)[0] + ", " + this.configuracoes.get(i)[1] + ")");
            }
        }
    }

    public AFD(String estados, String alfabeto, String estadoInicial, String estadosFinais, String[][] transicoes) {
        this.estados = new ArrayList<>(Arrays.asList(estados.trim().split("\\s*,\\s*")));
        this.alfabeto = alfabeto.toCharArray();
        this.estadoInicial = estadoInicial;
        this.estadosFinais = new ArrayList<>(Arrays.asList(estadosFinais.trim().split("\\s*,\\s*")));;
        this.transicoes = transicoes;
    }

    public AFD(ArrayList<String> estados, char[] alfabeto, String estadoInicial, ArrayList<String> estadosFinais, String[][] transicoes){
        this.estados = estados;
        this.alfabeto = alfabeto;
        this.estadoInicial = estadoInicial;
        this.estadosFinais = estadosFinais;
        this.transicoes = transicoes;
    }



    boolean Reconhecer(String palavra, Configuracoes configuracoes) {
        String palavraAux = palavra;
        String estadoAtual = this.estadoInicial;
        configuracoes.add(estadoAtual, palavraAux);

            for (int i = 0; i < this.transicoes.length; ++i) {
                if (this.transicoes[i][1].equals(palavraAux.substring(0, 1))) {
                    if (this.transicoes[i][0].equals(estadoAtual)) {

                        palavraAux = palavraAux.substring(1);
                        estadoAtual = this.transicoes[i][2];

                        if (palavraAux.length() == 0) {
                            configuracoes.add(estadoAtual, "ε");
                            break;
                        }
                        configuracoes.add(estadoAtual, palavraAux);
                        i = -1;
                    }
                }
            }
            if (this.estadosFinais.contains(estadoAtual)) {
                return true;
            } else {
               return false;
            }
    }

    public AFD minimizar() {
        ArrayList<String> novoEstados = new ArrayList<String>();

        // Adiciona todos os estados atingíveis
        novoEstados.add(this.estadoInicial);

        estadosAtingiveis(this.estadoInicial, novoEstados);

        // print para debug
        System.out.println("Estados atingiveis:");
        for(int i = 0; i < novoEstados.size(); i++){
            System.out.print(novoEstados.get(i) + ", ");
        }

        //tabela relacionando os estados
        Map<List<String>, Integer> tabelaRelacoes = new HashMap<List<String>, Integer>();
        for(int i = 0; i < novoEstados.size(); i++) {
            for(int j = i+1; j < novoEstados.size(); j++) {
                List<String> parEstados = Arrays.asList(novoEstados.get(i), novoEstados.get(j));

                // Marca 1 os estados trivialmente não equivalentes (um final e outro não)
                if( estadosFinais.contains(novoEstados.get(i)) != estadosFinais.contains(novoEstados.get(j))) {
                    tabelaRelacoes.put(parEstados, 1);
                } else {
                    tabelaRelacoes.put(parEstados, 0);
                }

            }
        }

        // print para debug
        System.out.println("\nTabela de relação:");
        for (Map.Entry<List<String>, Integer> entry : tabelaRelacoes.entrySet()) {
            List<String> parEstados = entry.getKey();
            int valor = entry.getValue();
            System.out.println("["+ parEstados.get(0) +"]"+"["+ parEstados.get(1) +"]"+"["+valor+"]");

        }

        //Marcação dos estados não equivalentes
        Map<List<String>, ArrayList<List<String>>> listaAnalise = new HashMap<List<String>, ArrayList<List<String>>>();
        for (Map.Entry<List<String>, Integer> entry : tabelaRelacoes.entrySet()) {
            List<String> parEstados = entry.getKey();
            int valor = entry.getValue();

            if (valor == 1) {
                continue;
            }

            for (char simb : this.alfabeto) {
                String proxEst1 = proximoEstado(parEstados.get(0), simb);
                String proxEst2 = proximoEstado(parEstados.get(1), simb);

                if (proxEst1 != proxEst2) {
                    List<String> proxEstados = Arrays.asList(proxEst1, proxEst2);

                    if(tabelaRelacoes.get(proxEstados) == null) {
                        proxEstados = Arrays.asList(proxEst2, proxEst1);
                    }

                    int proxValor = tabelaRelacoes.get(proxEstados);

                    if (proxValor == 1) {
                        tabelaRelacoes.put(parEstados, 1);

                        marcarListaPares(tabelaRelacoes, listaAnalise, parEstados);
                    } else {
                        ArrayList<List<String>> lista;
                        if (!listaAnalise.containsKey(proxEstados)) {
                            listaAnalise.put(proxEstados, new ArrayList<List<String>>());
                        }
                        lista = listaAnalise.get(proxEstados);
                        lista.add(parEstados);
                    }
                }

            }
        }

        // print para debug
        System.out.println("\nTabela de relação:");
        for (Map.Entry<List<String>, Integer> entry : tabelaRelacoes.entrySet()) {
            List<String> parEstados = entry.getKey();
            int valor = entry.getValue();
            System.out.println("["+ parEstados.get(0) +"]"+"["+ parEstados.get(1) +"]"+"["+valor+"]");
        }

        // print para debug
        System.out.println("\nlistaAnalise:");
        for (Map.Entry<List<String>, ArrayList<List<String>>> entry : listaAnalise.entrySet()) {
            List<String> parEstados = entry.getKey();
            ArrayList<List<String>> listaEstados = entry.getValue();
            System.out.print("["+ parEstados.get(0) +"]"+"["+ parEstados.get(1) +"] ->");
            for(List<String> proxEstados : listaEstados) {
                System.out.print("["+ proxEstados.get(0) +"]"+"["+ proxEstados.get(1) +"] ->");
            }
            System.out.println();
        }

        String novoEstadoInicial = this.estadoInicial;

        ArrayList<String> novoEstadosFinais = new ArrayList<String>();

        for(String estado : novoEstados){
            if(this.estadosFinais.contains(estado)) {
                novoEstadosFinais.add(estado);
            }
        }

        ArrayList<String[]> auxNovoTransicoes = new ArrayList<String[]>();

        for(String estado : novoEstados) {
            for(String[] transicao : this.transicoes) {
                if(transicao[0].equals(estado)){
                    auxNovoTransicoes.add(transicao);
                }
            }
        }

        // Unificação dos estados equivalentes
        for (Map.Entry<List<String>, Integer> entry : tabelaRelacoes.entrySet()) {
            List<String> parEstados = entry.getKey();
            int valor = entry.getValue();

            if (valor == 0) {
                String nomeNovoEstado = parEstados.get(0) + parEstados.get(1);
                novoEstados.remove(parEstados.get(0));
                novoEstados.remove(parEstados.get(1));
                novoEstados.add(nomeNovoEstado);
                Collections.sort(novoEstados);

                if (parEstados.contains(this.estadoInicial)) {
                    novoEstadoInicial = nomeNovoEstado;
                }

                if (novoEstadosFinais.contains(parEstados.get(0)) && novoEstadosFinais.contains(parEstados.get(1))) {
                    novoEstadosFinais.add(nomeNovoEstado);
                }
                novoEstadosFinais.remove(parEstados.get(0));
                novoEstadosFinais.remove(parEstados.get(1));

                for (int i = 0; i < auxNovoTransicoes.size(); i++) {

                    String[] transicao = auxNovoTransicoes.get(i);

                    if (parEstados.get(0).equals(transicao[0])) {
                        transicao[0] = nomeNovoEstado;
                    }
                    if (parEstados.get(1).equals(transicao[0])) {
                        auxNovoTransicoes.remove(i);
                        i--;
                    }
                    if (parEstados.get(0).equals(transicao[2])) {
                        transicao[2] = nomeNovoEstado;
                    }
                    if (parEstados.get(1).equals(transicao[2])) {
                        transicao[2] = nomeNovoEstado;
                    }
                }
            }
        }

            // print para debug
            System.out.println("\nNovos Estados:");
            for (String estado : novoEstados) {
                System.out.print(estado + ", ");
            }

            // print para debug
            System.out.println("\n\nNovas transições:");
            for (String[] transicao : auxNovoTransicoes) {
                for (String x : transicao) {
                    System.out.print(x + ", ");
                }
                System.out.println();
            }

            String[][] novoTransicoes = new String[auxNovoTransicoes.size()][3];
            for (int i = 0; i < auxNovoTransicoes.size(); i++) {
                novoTransicoes[i] = auxNovoTransicoes.get(i);
            }

            return new AFD(novoEstados, this.alfabeto, novoEstadoInicial, novoEstadosFinais, novoTransicoes);
        }


    private void marcarListaPares(Map<List<String>, Integer> tabelaRelacoes, Map<List<String>, ArrayList<List<String>>> listaAnalise, List<String> parEstados) {
        ArrayList<List<String>> lista = listaAnalise.get(parEstados);
        if(lista == null) {
            return;
        }
        for(List<String> proxEstados : lista) {
            tabelaRelacoes.put(proxEstados, 1);
            marcarListaPares(tabelaRelacoes, listaAnalise, proxEstados);
        }
    }

    private void estadosAtingiveis(String estado, ArrayList<String> estados) {
        for (int i = 0; i < this.transicoes.length; i++) {
            if (estado.equals(this.transicoes[i][0]) && !estados.contains(this.transicoes[i][2])) {
                estados.add(this.transicoes[i][2]);
                Collections.sort(estados);
                estadosAtingiveis(this.transicoes[i][2], estados);
            }
        }
    }

    private String proximoEstado(String estadoAtual, char entrada){
        String proxEstado = "";
        for(String[] transicao : this.transicoes) {
            if(transicao[0] == estadoAtual && transicao[1].charAt(0) == entrada) {
                proxEstado = transicao[2];
            }
        }

        return proxEstado;
    }
}
