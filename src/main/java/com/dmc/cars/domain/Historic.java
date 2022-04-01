package com.dmc.cars.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Historic.
 */
@Entity
@Table(name = "historic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Historic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "kms")
    private Integer kms;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "gearbox", "fuel", "model", "dealer" }, allowSetters = true)
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Historic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKms() {
        return this.kms;
    }

    public Historic kms(Integer kms) {
        this.setKms(kms);
        return this;
    }

    public void setKms(Integer kms) {
        this.kms = kms;
    }

    public Double getPrice() {
        return this.price;
    }

    public Historic price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Historic car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Historic)) {
            return false;
        }
        return id != null && id.equals(((Historic) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Historic{" +
            "id=" + getId() +
            ", kms=" + getKms() +
            ", price=" + getPrice() +
            "}";
    }
}
