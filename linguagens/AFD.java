import java.util.*;

public class AFD {
    private ArrayList<String> estados;
    private char[] alfabeto;
    private String estadoInicial;
    private ArrayList<String> estadosFinais;
    private String[][] transicoes;

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

    public void minimizar() {
        ArrayList<String> estados = new ArrayList<String>();

        // Adiciona todos os estados atingíveis
        estados.add(this.estadoInicial);

        estadosAtingiveis(estadoInicial, estados);

        for(int i = 0; i < estados.size(); i++){
            System.out.println(estados.get(i));
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
}
