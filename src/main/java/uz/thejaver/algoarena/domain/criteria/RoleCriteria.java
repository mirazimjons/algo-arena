package uz.thejaver.algoarena.domain.criteria;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.springbootstarterjpasupporter.specification.CriteriaMarker;
import uz.thejaver.springbootstarterjpasupporter.specification.filter.Filter;
import uz.thejaver.springbootstarterjpasupporter.specification.filter.StringFilter;
import uz.thejaver.springbootstarterjpasupporter.specification.filter.UUIDFilter;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCriteria extends CriteriaMarker {

    UUIDFilter id;
    StringFilter name;
    StringFilter description;
    PermissionFiler permission;

    public static class PermissionFiler extends Filter<Permission> {
    }

}
