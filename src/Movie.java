import java.io.Serializable;

public class Movie implements Comparable, Serializable {

    private int id; //let's say different remote user
    private String name;
    private int size;

    public Movie(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public Movie(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }


    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override //overrides object's equals and we use our custom one
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o != null && o instanceof Movie){
            isEqual = this.name.equals(((Movie) o).getName());
        }
        return isEqual;
    }

    @Override
    public int compareTo(Object o){
        return (((Movie) o).getSize() - this.getSize());
    }



    @Override //we considered ID as a key part for an object (see prospective uploads/uploads)
    public String toString() {
        return name + "(" + size + " MB)";
    }
}
