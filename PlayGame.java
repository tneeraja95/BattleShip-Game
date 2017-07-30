import java.util.Scanner;
class Player{
	boolean type;
	char[][] Board = new char[10][10];
	boolean turn;
	Ship[] ships = new Ship[5];
	int shipSinked;
	
	public Player(){
		for(int i=0; i<10; i++){
			for(int j=0;j<10; j++)
				Board[i][j]='E';
		}
		turn = false;
		shipSinked=0;
		for(int i=0; i<5; i++)
			ships[i]=new Ship(i+1);
	}
	
	void DisplayBoardUser(){
		
		for(int i=0; i<10; i++){
			System.out.print(" | ");
			for(int j=0; j<10; j++)
				System.out.print(Board[i][j]+" | ");
			System.out.println();
			System.out.print(" ");	
			for(int j=0; j<10; j++)
				System.out.print(" -- ");
				
			System.out.println();
		}
	}
	
	void DisplayBoardSystem(){
		for(int i=0; i<10; i++){
			System.out.print(" | ");		
			for(int j=0; j<10; j++){
				if(Board[i][j]=='H' || Board[i][j]=='M' )
					System.out.print(Board[i][j]+" | ");
				else
					System.out.print("E | ");
			}	
			System.out.println();
			System.out.print(" ");
			for(int j=0; j<10; j++)
				System.out.print(" -- ");
			System.out.println();
		}
	
	}
	void createRandomBoard(){
		for(int i=1; i<=5;i++){
			ships[i-1]= new Ship(i);
			ships[i-1].createRandomShip(Board);
		}
	}
	
	void createCustomBoard(){
	Scanner sc = new Scanner(System.in);
	int pos_i, pos_j, size=1;
	boolean orientation;
		while(true){
			System.out.println("Enter Details of Ship of size "+size);
			System.out.println("Enter Starting Position (row, column) Indexed from 1");
			pos_i=sc.nextInt()-1;
			pos_j=sc.nextInt()-1;
			if(pos_i <0 || pos_j<0){
				System.out.println("Enter Valid Ship Details");
				continue;
			}	
			System.out.println("Enter orientation (V/H)");
			char o=sc.next().trim().charAt(0);
			if(o=='H' || o=='h')
				orientation=true;
			else if (o=='V' || o=='v')
				orientation=false;
			else{
				System.out.println("Enter Valid Ship Details");
				continue;
			}
					
			
			if(ships[size-1].createCustomShip(pos_i,pos_j,orientation,size,Board)){
				size++;
				DisplayBoardUser();
				if(size>5)
					break;	
				continue;
			}	
			System.out.println("Enter Valid Ship Details");	
			DisplayBoardUser();
		}
	}
	
	void displayShipDetails(){
		for(Ship s : ships){
			if(s.shipAlive==true)
				System.out.println("Ship "+s.size+": Alive");
			else
				System.out.println("Ship "+s.size+": Dead");
		}
	}
	
	boolean onBoardClick(int pos_i, int pos_j){// returns if the player won
		if(Board[pos_i][pos_j]=='E'){
			Board[pos_i][pos_j]='M';
			System.out.println("Its a Miss!!!");
		}
		
		else if(Board[pos_i][pos_j]=='S'){
			System.out.println("Its a Hit!!!");
			Board[pos_i][pos_j]='H';
			for( Ship s : ships){
				if(s.CheckShip(pos_i,pos_j)){
					if(s.CheckifSink()){
						System.out.println("Ship of size "+s.size+" sinked");
						shipSinked++;
						if(shipSinked==5){
							return true;
						}
					}
				}
			}
		}
		return false;
	}	
		
}

class Ship{
	boolean shipAlive;
	int size;
	int startingPosition_i;
	int startingPosition_j;
	boolean orientation;
	int number_of_hits;
	
	public Ship(int size){
		this.size=size;
		number_of_hits=0;
	}
	
	void createRandomShip( char Board[][]){

	    boolean flag=false;
    	int row, col;
    	while(flag==false){
    	    flag=true;
    	  	if(Math.random() > 0.5){
    	     	//horizontal line
    	     	col = (int)( Math.random() * (10));
    	        row = (int) (Math.random()* (10));
  			//	System.out.println("row = "+row+"col = "+col);
  				if(col+size>10){
  					flag=false;
  					continue;
  				}	
    	      	for(int i=0;i<size;i++){
    	            if(Board[row][col+i] == 'S')
    	            {
    	                flag=false;
    	                break;
    	            }
    	        }
    	        if(flag==false)
    	            continue;
    	        for(int i=0;i<size;i++)
    	        {
    	            //update ship sign in display
    	            Board[row][col+i]='S';                  
    	        }
    	        flag=true;
    	        startingPosition_i=row;
    	        startingPosition_j=col;
    	        orientation=false;
    	    }
    	    else
    	    {
    	        //vertical line
    	        col = (int) Math.random() * (Board[0].length-1);
    	        row = (int) Math.random()* (Board.length-1-size);
    	        if(row+size>10){
  					flag=false;
  					continue;
  				}	

    	        for(int i=0;i<size;i++)
    	        {
    	            if(Board[row+i][col]=='S')
    	            {
    	                flag=false;
    	                break;
    	            }
    	        }
    	        if(flag==false)
    	            continue;
    	        for(int i=0;i<size;i++)
    	       	{
    	            //update ship sign in display
    	            Board[row+i][col]='S';
    	        }
    	        flag=true;
    	        startingPosition_i=row;
    	        startingPosition_j=col;
    	        orientation=true;
    	    }

    	}
		
	}
	
	
	boolean createCustomShip(int pos_i, int pos_j, boolean orientation, int size, char Board[][]){
		if(orientation==true)
		{	
			if(pos_j+size>10)
				return false;
			for(int j=pos_j; j<pos_j+size; j++)
				if(Board[pos_i][j]=='S')
					return false;
			for(int j=pos_j; j<pos_j+size; j++)
				Board[pos_i][j]='S';
			startingPosition_i=pos_i;
			startingPosition_j=pos_j;
			this.orientation=orientation;
			this.size=size;
			number_of_hits=0;		
			return true;
		}	
		if(pos_i+size>10)
			return false;
		for(int i=pos_i; i<pos_i+size; i++)
			if(Board[i][pos_j]=='S')
				return false;
		for(int i=pos_i; i<pos_i+size; i++)
			Board[i][pos_j]='S';
		startingPosition_i=pos_i;
		startingPosition_j=pos_j;
		this.orientation=orientation;
		this.size=size;
		number_of_hits=0;		
		return true;			
	}
	
	boolean CheckShip(int pos_i, int pos_j){
		if(orientation == false){
			if(startingPosition_i==pos_i){
				if(pos_j>=startingPosition_j && pos_j<=startingPosition_j+size){
					number_of_hits++;
					return true;
				}
			}
		}
		if(orientation == true){
			if(startingPosition_j==pos_j){
				if(pos_i>=startingPosition_i && pos_i<=startingPosition_i+size){
					number_of_hits++;
					return true;
				}
			}
		}
		return false;
	}
	
	boolean CheckifSink(){
		if(number_of_hits==size)
			return true;
		return false;	
	}	
}	

class PlayGame{


	public static void main(String[] args){
		int pos_i, pos_j;
		boolean result;
		Player User = new Player();
		Player Computer = new Player(); 
		Scanner sc = new Scanner (System.in);
		System.out.println("BATTLE SHIP !!!!");
		Computer.createRandomBoard();
		System.out.println("Choose Board");
		System.out.println("1)Random Board");
		System.out.println("2)Custom Board");
		while(true){
			int choice=sc.nextInt();
			if(choice==1){
				User.createRandomBoard();
				break;
			}
			if(choice==2){
				User.createCustomBoard();
				break;
			}
			System.out.println("Enter Valid Choice");
		}
		System.out.println("Game Begins !!!!");
		while(true){
			System.out.println("Computer's Board");
			Computer.DisplayBoardSystem();
			System.out.println("Your Board");
			User.DisplayBoardUser();
			System.out.println("Enter the coordinates to hit");
			pos_i=sc.nextInt()-1;
			pos_j=sc.nextInt()-1;
			if(pos_i<0 || pos_j<0 || pos_i>9 || pos_j>9){
				System.out.println("Enter Valid Details");
				continue;
			}
			result=Computer.onBoardClick(pos_i, pos_j);
			if(result==true){
				System.out.println("You WIN!!!");
				break;
			}
			System.out.println("Computer's Turn");			
			pos_i=(int)(Math.random()*10);
			pos_j=(int)(Math.random()*10);
			result=User.onBoardClick(pos_i,pos_j);
			if(result==true){
				System.out.println("Computer WIN!!!");
				break;
			}
			
		}
		
				
	}	

}
