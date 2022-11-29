package ejercicio1;

public class Ejercicio1 {
	int cont = 1;
	int elemento;
	boolean[][] matrizLogica;

	public void setMatrizLogica(int m, int n) {
		matrizLogica = new boolean[m][n];
			}

	public void porDerecha(int i, int j, int[][] matriz) {
		if(matriz[0].length -2 < j) return ;
		if(matriz[i][j] == matriz[i][j+1] ) {
			if(!matrizLogica[i][j+1]) {
				cont++;
				matrizLogica[i][j+1] = true;
				elemento=matriz[i][j];
				this.porDerecha(i, j+1, matriz);
				if(matriz.length-2 > i)
					this.porAbajo(i, j+1, matriz);
				if(1 < i)
					this.porArriba(i, j+1, matriz);
			}	
		}	
	}

	public void porAbajo(int i, int j, int[][] matriz) {
		if(matriz.length-2 < i ) return;
		if(matriz[i][j] == matriz[i+1][j]) {
			if(!matrizLogica[i+1][j]) {
				cont++;
				matrizLogica[i+1][j] = true;
				elemento=matriz[i][j];
				this.porDerecha(i+1, j, matriz);
				this.porIzquierda(i+1, j, matriz);
				this.porAbajo(i+1, j, matriz);
			}
		}
	}
	public void porArriba(int i, int j, int[][] matriz) {
		if(1 > i ) return;
		if(matriz[i][j] == matriz[i-1][j]) {
			if(!matrizLogica[i-1][j]) {
				cont++;
				matrizLogica[i-1][j] = true;
				elemento=matriz[i][j];
				this.porDerecha(i-1, j, matriz);
				this.porIzquierda(i-1, j, matriz);
				this.porArriba(i-1, j, matriz);
			}
		}
	}

	public void porIzquierda(int i, int j, int[][] matriz) {
		if(1 > j) return ;
		if(matriz[i][j] == matriz[i][j-1] ) {
			if(!matrizLogica[i][j-1]) {
				cont++;
				matrizLogica[i][j-1] = true;
				elemento=matriz[i][j];
				this.porIzquierda(i, j-1, matriz);
				if(matriz.length-2 > i)
					this.porAbajo(i, j-1, matriz);
				if(1 < i)
					this.porArriba(i, j-1, matriz);
			}
		}
	}

	public static void main(String[]args) {
		
		Ejercicio1 a1 = new Ejercicio1();
		a1.setMatrizLogica(9, 9);
		int[][] matriz = new int[9][9]; 
		
		for(int i=0; i<matriz.length; i++) {
			for(int j=0; j<matriz[i].length; j++) {
				matriz[i][j]= (int) (Math.random()*2);
				System.out.print(matriz[i][j]+" ");
			}
			System.out.println("");
		}

		a1.matrizLogica[0][0] = true;
		a1.porDerecha(0, 0, matriz);
		a1.porIzquierda(0, 0, matriz);
		a1.porAbajo(0, 0, matriz);
		a1.porArriba(0, 0, matriz);	
		System.out.println(a1.cont +"  "+ a1.elemento);	
	}
	
}
