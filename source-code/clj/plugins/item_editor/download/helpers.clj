
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.helpers
    (:require [mid-fruits.keyword               :as keyword]
              [mid-fruits.string                :as string]
              [mongo-db.api                     :as mongo-db]
              [pathom.api                       :as pathom]
              [plugins.item-editor.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-suggestions
  ; @param (map) env
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
  [env]
  (let [extension-id    (pathom/env->param env :extension-id)
        item-namespace  (pathom/env->param env :item-namespace)
        suggestion-keys (pathom/env->param env :suggestion-keys)
        collection-name (core.helpers/collection-name extension-id item-namespace)
        suggestion-keys (keyword/add-items-namespace item-namespace suggestion-keys)]
       (mongo-db/get-specified-values collection-name suggestion-keys string/nonempty?)))
