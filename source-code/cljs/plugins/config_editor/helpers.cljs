
(ns plugins.config-editor.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-backup-path
  ; @param (keyword) configer-id
  ;
  ; @return (vector)
  [configer-id]
  [configer-id :config-handler/backup-item])

(defn default-config-path
  ; @param (keyword) configer-id
  ;
  ; @return (vector)
  [configer-id]
  [configer-id :config-handler/edited-item])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-config-query-key
  ; @param (keyword) configer-id
  ;
  ; @example
  ;  (entities/request-config-query-key :my-configer)
  ;  =>
  ;  :my-configer/get-config
  ;
  ; @return (namespaced keyword)
  [configer-id]
  (keyword (name configer-id) "get-config"))

(defn save-config-query-key
  ; @param (keyword) configer-id
  ;
  ; @example
  ;  (entities/save-config-query-key :my-configer)
  ;  =>
  ;  "my-configer/save-config!"
  ;
  ; @return (string)
  [configer-id]
  (str (name configer-id) "/save-config!"))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn server-response->config-item
  ; @param (keyword) configer-id
  ; @param (namespaced map) server-response
  ;
  ; @example
  ;  (entities/server-response->config-item :my-configer {:my-configer/get-config {...}})
  ;  =>
  ;  {...}
  ;
  ; @return (map)
  [configer-id server-response]
  (let [query-key (request-config-query-key configer-id)]
       (query-key server-response)))
