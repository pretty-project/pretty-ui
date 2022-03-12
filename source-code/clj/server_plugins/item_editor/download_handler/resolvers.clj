
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.download-handler.resolvers
    (:require [com.wsscode.pathom3.connect.operation              :refer [defresolver]]
              [pathom.api                                         :as pathom]
              [server-plugins.item-editor.download-handler.engine :as download-handler.engine]))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-suggestions-f
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (download-handler.engine/env->item-suggestions env))

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
             ; @example
             ;  (item-editor/get-item-suggestions {:params {:extension-id    :my-extension
             ;                                              :item-namespace  :my-type
             ;                                              :suggestion-keys [:my-key :your-key]}})
             ;  =>
             ;  {:item-editor/get-item-suggestions {:my-type/my-key   ["..."]
             ;                                      :my-type/your-key ["..." "..."]}}
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
