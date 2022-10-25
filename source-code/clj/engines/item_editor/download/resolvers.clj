
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-editor.download.resolvers
    (:require [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [engines.item-editor.download.helpers  :as download.helpers]
              [pathom.api                            :as pathom]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-suggestions-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (let [editor-id (pathom/env->param env :editor-id)]
       (download.helpers/env->item-suggestions env editor-id)))

(defresolver get-item-suggestions
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ;  {:params (map)
             ;    {:editor-id (keyword)
             ;     :suggestion-keys (keywords in vector)}}
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:item-editor/get-item-suggestions (map)
             [env resolver-props]
             {:item-editor/get-item-suggestions (get-item-suggestions-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item-suggestions])

(pathom/reg-handlers! ::handlers HANDLERS)
