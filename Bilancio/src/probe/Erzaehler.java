package probe;

import java.util.Observable;

class Erzaehler extends Observable {
    
    public Erzaehler(){
        this.addObserver(new Zuhoerer_1());
        this.addObserver(new Zuhoerer_2());
        tell("hoihoi!");
    }

    
    public void tell(String info){
        if(countObservers()>0){
        	
            setChanged();
            notifyObservers(info);
        }
    }
}
