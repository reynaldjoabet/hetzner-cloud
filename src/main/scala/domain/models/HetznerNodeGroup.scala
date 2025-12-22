package domain.models

import hcloud.models.PlacementGroup
// hetznerNodeGroup implements cloudprovider.NodeGroup interface. hetznerNodeGroup contains
// configuration info and functions to control a set of nodes that have the
// same capacity and set of labels.
case class HetznerNodeGroup(
    id: String,
    // manager: HetznerManager,
    minSize: Int,
    maxSize: Int,
    instanceType: String,
    region: String,
    var targetSize: Int, // volatile-like state
    placementGroup: PlacementGroup
)
