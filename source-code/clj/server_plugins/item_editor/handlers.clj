
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.handlers
    (:require [mid-fruits.string    :as string]
              [mid-fruits.keyword   :as keyword]
              [mid-fruits.validator :as validator]
              [mongo-db.api         :as mongo-db]
              [pathom.api           :as pathom]
              [x.server-db.api      :as db]
              [server-plugins.item-editor.engine     :as engine]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-suggestions
  ; @param (map) env
  ;
  ; @return (map)
  [env]
  (let [extension-id      (pathom/env->param env :extension-id)
        item-namespace    (pathom/env->param env :item-namespace)
        suggestion-keys   (pathom/env->param env :suggestion-keys)
        all-documents     (mongo-db/get-all-documents  extension-id)
        suggestion-keys   (keyword/add-items-namespace item-namespace suggestion-keys)
        suggestion-values (db/get-specified-values all-documents suggestion-keys string/nonempty?)]
       (validator/validate-data suggestion-values)))



;; -- Resolvers ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
             [env _]
             {:item-editor/get-item-suggestions (env->item-suggestions env)})



;; -- Handlers ----------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-item-suggestions])

(pathom/reg-handlers! ::handlers HANDLERS)
