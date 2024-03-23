package hu.webler.weblerbddassertionunitandintegrationtest.entity.extra;

import hu.webler.weblerbddassertionunitandintegrationtest.entity.base.Identifier;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bdd_extra")
public class Extra extends Identifier {

    // We do not need Auditable for this class, then we only extends the Identifier to have UUID
}
