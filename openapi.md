
A model contains multiple inline enums → name collisions

```yml
Subnet:
  type: object
  properties:
    type:
      type: string
      enum: [cloud, server, vswitch]

    status:
      type: string
      enum: [active, pending, disabled]

    mode:
      type: string
      enum: [auto, manual]
```

[scala-sttp4-jsoniter](https://github.com/OpenAPITools/openapi-generator/tree/fb2878cb23a545ceb520ab1bdb039aa6eaac1f15/modules/openapi-generator/src/main/resources/scala-sttp4-jsoniter)