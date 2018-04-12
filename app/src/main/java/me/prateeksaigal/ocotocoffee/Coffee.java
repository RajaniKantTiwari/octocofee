package me.prateeksaigal.ocotocoffee;

/**
 * Created by Prateek Saigal on 01-11-2017.
 */

public class Coffee {

    int id;
    String name;
    String _foam, _coffee, _milk, _liquid1, _liquid2;
    int _type;

    public Coffee() {

    }

    public Coffee(String name, String foam, String coffee, String milk, String liquid1, String liquid2, int _type) {
        this.name = name;
        this._foam = foam;
        this._milk = milk;
        this._liquid1 = liquid1;
        this._liquid2 = liquid2;
        this._coffee = coffee;
        this._type = _type;
    }

    public int get_type() {
        return _type;
    }

    public void set_type(int _type) {
        this._type = _type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_coffee() {
        return _coffee;
    }

    public String get_foam() {
        return _foam;
    }

    public String get_liquid1() {
        return _liquid1;
    }

    public String get_liquid2() {
        return _liquid2;
    }

    public String get_milk() {
        return _milk;
    }

    public String getName() {
        return name;
    }

    public void set_coffee(String _coffee) {
        this._coffee = _coffee;
    }

    public void set_foam(String _foam) {
        this._foam = _foam;
    }

    public void set_liquid1(String _liquid1) {
        this._liquid1 = _liquid1;
    }

    public void set_liquid2(String _liquid2) {
        this._liquid2 = _liquid2;
    }

    public void set_milk(String _milk) {
        this._milk = _milk;
    }

    public void setName(String name) {
        this.name = name;
    }
}
