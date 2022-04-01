package com.dmc.cars.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "engine")
    private String engine;

    @Column(name = "power")
    private Integer power;

    @Column(name = "kms")
    private Integer kms;

    @Column(name = "color")
    private String color;

    @Column(name = "doors")
    private Integer doors;

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "building_date")
    private Instant buildingDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "offer")
    private Boolean offer;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Gearbox gearbox;

    @ManyToOne
    private Fuel fuel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "brand" }, allowSetters = true)
    private Model model;

    @ManyToOne
    @JsonIgnoreProperties(value = { "city" }, allowSetters = true)
    private Dealer dealer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Car id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Car name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Car description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEngine() {
        return this.engine;
    }

    public Car engine(String engine) {
        this.setEngine(engine);
        return this;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Integer getPower() {
        return this.power;
    }

    public Car power(Integer power) {
        this.setPower(power);
        return this;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getKms() {
        return this.kms;
    }

    public Car kms(Integer kms) {
        this.setKms(kms);
        return this;
    }

    public void setKms(Integer kms) {
        this.kms = kms;
    }

    public String getColor() {
        return this.color;
    }

    public Car color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDoors() {
        return this.doors;
    }

    public Car doors(Integer doors) {
        this.setDoors(doors);
        return this;
    }

    public void setDoors(Integer doors) {
        this.doors = doors;
    }

    public Integer getSeats() {
        return this.seats;
    }

    public Car seats(Integer seats) {
        this.setSeats(seats);
        return this;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Instant getBuildingDate() {
        return this.buildingDate;
    }

    public Car buildingDate(Instant buildingDate) {
        this.setBuildingDate(buildingDate);
        return this;
    }

    public void setBuildingDate(Instant buildingDate) {
        this.buildingDate = buildingDate;
    }

    public Double getPrice() {
        return this.price;
    }

    public Car price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getOffer() {
        return this.offer;
    }

    public Car offer(Boolean offer) {
        this.setOffer(offer);
        return this;
    }

    public void setOffer(Boolean offer) {
        this.offer = offer;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Car category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Gearbox getGearbox() {
        return this.gearbox;
    }

    public void setGearbox(Gearbox gearbox) {
        this.gearbox = gearbox;
    }

    public Car gearbox(Gearbox gearbox) {
        this.setGearbox(gearbox);
        return this;
    }

    public Fuel getFuel() {
        return this.fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Car fuel(Fuel fuel) {
        this.setFuel(fuel);
        return this;
    }

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Car model(Model model) {
        this.setModel(model);
        return this;
    }

    public Dealer getDealer() {
        return this.dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Car dealer(Dealer dealer) {
        this.setDealer(dealer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", engine='" + getEngine() + "'" +
            ", power=" + getPower() +
            ", kms=" + getKms() +
            ", color='" + getColor() + "'" +
            ", doors=" + getDoors() +
            ", seats=" + getSeats() +
            ", buildingDate='" + getBuildingDate() + "'" +
            ", price=" + getPrice() +
            ", offer='" + getOffer() + "'" +
            "}";
    }
}
