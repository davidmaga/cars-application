package com.dmc.cars.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Photo.
 */
@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "main")
    private Boolean main;

    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "gearbox", "fuel", "model", "dealer" }, allowSetters = true)
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Photo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public Photo path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getMain() {
        return this.main;
    }

    public Photo main(Boolean main) {
        this.setMain(main);
        return this;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Photo car(Car car) {
        this.setCar(car);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Photo)) {
            return false;
        }
        return id != null && id.equals(((Photo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Photo{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", main='" + getMain() + "'" +
            "}";
    }
}
