
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.resolvers
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [pathom.api                            :as pathom]
              [plugins.item-editor.download.helpers  :as download.helpers]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-suggestions-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (download.helpers/env->item-suggestions env))

(defresolver get-item-suggestions
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ;  {:params (map)
             ;    {:extension-id (keyword)
             ;     :item-namespace (keyword)
             ;     :suggestion-keys (keywords in vector)}}
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:item-editor/get-item-suggestions (map)
             [env resolver-props]
             {:item-editor/get-item-suggestions (get-item-suggestions-f env resolver-props)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item-suggestions])

(pathom/reg-handlers! ::handlers HANDLERS)
