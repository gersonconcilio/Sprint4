public class testAFD {
    public static void main(String[] args) {
        // ALFABETO
        String alfabeto = "ab";

        // ESTADOS
        String estados = "q1,q2,q3,q4,q5,q6,q7,q8";

        // ESTADO INICIAL EX: A
        String estadoInicial = "q1";

        // ESTADO FINAL PODE SER MAIS DE UM EX: BC
        String estadosFinais = "q1,q3,q7";

        // FUNÇÕES DE TRANSIÇÃO DE ESTADOS
        String [][] transicoes = new String[][]{
                {"q1", "a", "q2"},
                {"q1", "b", "q4"},
                {"q2", "a", "q5"},
                {"q2", "b", "q3"},
                {"q3", "a", "q2"},
                {"q3", "b", "q6"},
                {"q4", "a", "q1"},
                {"q4", "b", "q5"},
                {"q5", "a", "q5"},
                {"q5", "b", "q5"},
                {"q6", "a", "q3"},
                {"q6", "b", "q5"},
                {"q7", "a", "q6"},
                {"q7", "b", "q8"},
                {"q8", "a", "q7"},
                {"q8", "b", "q3"},
        };

        // CRIA UM AFD COM OS DADOS INSERIDOS
        AFD meuAFD = new AFD(estados, alfabeto, estadoInicial, estadosFinais, transicoes);

        // PALAVRA PARA TESTAR O AUTOMATO
        String palavra = "baababbaab";

        AFD.Configuracoes configuracoes = new AFD.Configuracoes();
        boolean reconhecido = meuAFD.Reconhecer(palavra,configuracoes);

        configuracoes.imprimir();

        if(reconhecido){
            System.out.println("A palavra " + palavra + " foi reconhecida pelo autômato.");
        } else {
            System.out.println("A palavra " + palavra + " não foi reconhecida pelo autômato.");
        }

        System.out.println("--------------------");
        System.out.println("--------------------");
        System.out.println("minimizar():");
        meuAFD.minimizar();
    }
}
