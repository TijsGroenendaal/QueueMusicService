import * as k8s from "@pulumi/kubernetes";
import { CustomResource, Input, Resource } from "@pulumi/pulumi";

export const install = ({
    name,
    dependsOn,
    provider,
    namespace,
    listeners
}: {
    name: string
    dependsOn?:  Input<Resource> | Input<Input<Resource>[]>,
    provider: k8s.Provider,
    namespace: k8s.core.v1.Namespace,
    listeners: any[]
}): CustomResource => {
    return new k8s.apiextensions.CustomResource(`gateway-${name}`, {
        apiVersion: "gateway.networking.k8s.io/v1",
        kind: "Gateway",
        metadata: {
            "name": name,
            "namespace": namespace.id,
        },
        spec: {
            gatewayClassName: name,
            listeners: listeners
        }
    }, {
        provider,
        dependsOn
    })
}