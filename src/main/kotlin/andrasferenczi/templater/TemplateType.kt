package andrasferenczi.templater

enum class TemplateType(
    val templateKey: String
) {
    NamedParameterConstructor("named_parameter_constructor"),
    CopyWithMethod("copy_with__method"),
    MapTemplate("to_map__from_map"),
    JsonTemplate("to_json__from_json"),
    Combined("combined"),

    ToString("to_string"),
    Equals("equals"),
    HashCode("hashcode"),

    Comment("comment")
}
