package domain.models
final case class HetznerNodeGroupSpec(
    name: String,
    minSize: Int,
    maxSize: Int,
    region: String,
    instanceType: String
)
