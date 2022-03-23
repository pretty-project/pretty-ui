
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.helpers
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.string  :as string]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [x.server-core.api  :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-suggestions
  ; @param (map) env
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (item-editor/get-item-suggestions {:params {:suggestion-keys [:my-key :your-key]}} :my-extension :my-type)
  ;  =>
  ;  {:item-editor/get-item-suggestions {:my-type/my-key   ["..."]
  ;                                      :my-type/your-key ["..." "..."]}}
  ;
  ; @return (map)
  [env extension-id item-namespace]
  (let [suggestion-keys  (pathom/env->param env :suggestion-keys)
        suggestion-keys  (keyword/add-items-namespace item-namespace suggestion-keys)
        collection-name @(a/subscribe [:item-editor/get-editor-prop extension-id item-namespace :collection-name])]
       (mongo-db/get-specified-values collection-name suggestion-keys string/nonempty?)))
