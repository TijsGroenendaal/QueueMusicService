import * as k8s from "@pulumi/kubernetes";
import { deepMerge } from "../../deepmerge";
import { Input, Resource } from "@pulumi/pulumi";

export const install = ({
    provider,
    version,
    values,
    dependsOn,
}: {
    provider: k8s.Provider,
    version: string,
    values: any,
    dependsOn: Input<Input<Resource>[]>
}): k8s.helm.v4.Chart  => {
    const config = {
        providers: {
            kubernetesGateway: {
                enabled: true
            }
        },
        gateway: {
            enabled: true,
            annotations: {
                "cert-manager.io/issuer": "letsencrypt-production"
            },
            listeners: {
                websecure: {
                    hostname: "argocd.tijsgroenendaal.nl",
                    port: 8443,
                    protocol: "HTTPS",
                    certificateRefs: [
                        {
                            name: "argocd-tls"
                        }
                    ]
                }
            }
        },
        nodeSelector: {
            // TODO hardcoded
            "kubernetes.io/hostname": "server-queuemusic-worker1-1ad71ec"
        },
        deployment: {
            enabled: true,
        },
        service: {
            enabled: false,
        },
        ingressClass: {
            enabled: false
        },
        hostNetwork: true
    }

    const merged = deepMerge(config, values)

    return new k8s.helm.v4.Chart("traefik", {
        version: version,
        name: "traefik",
        chart: "traefik",
        repositoryOpts: {
            repo: "https://traefik.github.io/charts",
        },
        skipCrds: true,
        values: merged
    }, { provider, dependsOn })
}