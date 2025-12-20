import * as k8s from "@pulumi/kubernetes";
import { Namespace } from "@pulumi/kubernetes/core/v1";
import { Input, Resource } from "@pulumi/pulumi";

export const install = ({
    name,
    provider,
    dependsOn
}: {
    name: string
    provider: k8s.Provider,
    dependsOn?: Input<Resource> | Input<Input<Resource>[]>
}): Namespace => {
    return new k8s.core.v1.Namespace(name, {
        metadata: {
            name: name,
            labels: {
                "app.kubernetes.io/managed-by": "pulumi",
            }
        },
    }, { provider, dependsOn })
}