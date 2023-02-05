{{/*
Generate labels
*/}}
{{- define "labels" }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
component: {{ .Chart.Name | quote }}
heritage: {{ .Release.Service | quote }}
{{ if .Values.releaseLabel -}}
release: {{ .Values.releaseLabel | quote }}
{{ else -}}
release: {{ .Release.Name | quote }}
{{ end -}}
{{- end -}}

{{/*
Generate selector labels
*/}}
{{- define "selector-labels" }}
{{ if .Values.customSelectors }}
{{- toYaml .Values.customSelectors -}}
{{ else }}
component: {{ .Chart.Name | quote }}
{{ if .Values.releaseLabel -}}
release: {{ .Values.releaseLabel | quote }}
{{ else -}}
release: {{ .Release.Name | quote }}
{{ end -}}
{{- end -}}
{{- end -}}
