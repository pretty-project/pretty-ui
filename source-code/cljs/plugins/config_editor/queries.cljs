
(ns plugins.config-editor.queries
    (:require [plugins.config-editor.helpers :as configer.helpers]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-config-query
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;
  ; @example
  ;  (entities/get-request-config-query :my-configer)
  ;  =>
  ;  [:my-configer/get-config]
  ;
  ; @return (vector)
  [_ [_ configer-id _]]
  [(configer.helpers/request-config-query-key configer-id)])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-config-query
  ; @param (keyword) configer-id
  ; @param (map) configer-props
  ;  {:config-path (vector)}
  ;
  ; @example
  ;  (entities/get-save-config-query :my-configer)
  ;  =>
  ;  [`(my-configer/save-config! ~{...})])
  ;
  ; @return (vector)
  [db [_ configer-id {:keys [config-path]}]]
  (let [config-item (get-in db config-path)
        query-key   (configer.helpers/save-config-query-key configer-id)]
       [`((symbol query-key) ~config-item)]))
