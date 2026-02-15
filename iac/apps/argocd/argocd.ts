import * as k8s from "@pulumi/kubernetes"
import { deepMerge } from "../../deepmerge";
import { Input, Resource } from "@pulumi/pulumi";

export const install = ({
    namespace,
    provider,
    values,
    version,
    dependsOn,
}: {
    namespace: k8s.core.v1.Namespace,
    version: string
    values: any,
    provider: k8s.Provider
    dependsOn: Input<Input<Resource>[]>
}) => {

    const baseValues = {
        server: {
            httproute: {
                enabled: true,
                hostnames: [
                    "argocd.tijsgroenendaal.nl"
                ],
                annotations: {
                    "cert-manager.io/issuer": "letsencrypt-production",
                },
                parentRefs: [
                    {
                        name: "traefik",
                        namespace: namespace.id,
                        sectionName: "websecure",
                    }
                ]
            }
        }
    }

    const merged = deepMerge(baseValues, values)

    new k8s.helm.v3.Release("argocd", {
        namespace: namespace.id,
        version: version,
        name: "argocd",
        chart: "argo-cd",
        repositoryOpts: {
            repo: "https://argoproj.github.io/argo-helm",
        },
        values: merged
    }, { provider, dependsOn })
}