package griffon.domain

import griffon.transform.Domain

@Domain
class Sample {
    String name
    String lastName
    int num

    String toString() {"<$id> $name $lastName [$num]"}
}
