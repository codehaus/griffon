@artifact.package@import @type.package@.@type.name@

interface @artifact.name@ {
    @type.name@ find@type.name@ById(int id)

    int insert(@type.name@ @type.property.name@)

    int update(@type.name@ @type.property.name@)

    int delete(@type.name@ @type.property.name@)

    List<@type.name@> list()
}
