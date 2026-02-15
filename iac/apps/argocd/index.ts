import * as argocd from './argocd';
import * as k8s from "@pulumi/kubernetes";
import * as components from '../components';
import { Input, Resource } from "@pulumi/pulumi";

export const install = ({
    values,
    version,
    provider,
    dependsOn,
}: {
    version: string
    values: any
    provider: k8s.Provider
    dependsOn: Input<Input<Resource>[]>
}) => {
    const ns = components.namespace.install({
        name: "argocd",
        provider,
        dependsOn,
    })

    argocd.install({
        values: values,
        version: version,
        provider,
        namespace: ns,
        dependsOn: [ns],
    })
}