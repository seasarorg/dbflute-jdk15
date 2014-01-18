package org.seasar.dbflute.bhv;

import java.util.List;

import org.seasar.dbflute.Entity;

/**
 * The interface of DTO mapper. 
 * @author jflute
 * @param <ENTITY> The type of entity.
 * @param <DTO> The type of DTO.
 */
public interface DtoMapper<ENTITY extends Entity, DTO> {

    /**
     * Do mapping from an entity to a DTO with relation data.
     * @param entity The entity as mapping resource. (NullAllowed: if null, returns null)
     * @return The mapped DTO. (NotNull)
     */
    DTO mappingToDto(ENTITY entity);

    /**
     * Do mapping from an entity list to a DTO list with relation data. <br />
     * This calls this.mappingToDto() in a loop of the list.
     * @param entityList The list of entity as mapping resource. (NotNull: null elements are inherited)
     * @return The list of mapped DTO. (NotNull)
     */
    List<DTO> mappingToDtoList(List<ENTITY> entityList);

    /**
     * Do mapping from a DTO to an entity with relation data. <br />
     * A setter of an entity is called under the rule of this.needsMapping().
     * @param dto The DTO as mapping resource. (NullAllowed: if null, returns null)
     * @return The mapped entity. (NotNull)
     */
    ENTITY mappingToEntity(DTO dto);

    /**
     * Do mapping from a DTO list to an entity list with relation data. <br />
     * This calls this.mappingToEntity() in loop of the list.
     * @param dtoList The list of DTO as mapping resource. (NotNull: null elements are inherited)
     * @return The list of mapped entity. (NotNull)
     */
    List<ENTITY> mappingToEntityList(List<DTO> dtoList);

    /**
     * Set the option whether base-only mapping or not.
     * @param baseOnlyMapping Does the mapping ignore all references? (true: base-only mapping, false: all relations are valid)
     */
    void setBaseOnlyMapping(boolean baseOnlyMapping);

    /**
     * Set the option whether reverse reference or not.
     * @param reverseReference Does the mapping contain reverse references? (true: reverse reference, false: one-way reference)
     */
    void setReverseReference(boolean reverseReference);
}
