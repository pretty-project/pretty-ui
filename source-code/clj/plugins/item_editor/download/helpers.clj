
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
  ; @param (keyword) editor-id
  ;
  ; @example
  ;  (item-editor/get-item-suggestions {:params {:suggestion-keys [:my-key :your-key]}} :my-editor)
  ;  =>
  ;  {:item-editor/get-item-suggestions {:my-type/my-key   ["..."]
  ;                                      :my-type/your-key ["..." "..."]}}
  ;
  ; @return (map)
  [env editor-id]
  (let [collection-name @(a/subscribe [:item-editor/get-editor-prop editor-id :collection-name])
        item-namespace  @(a/subscribe [:item-editor/get-editor-prop editor-id :item-namespace])
        suggestion-keys  (pathom/env->param env :suggestion-keys)
        suggestion-keys  (keyword/add-items-namespace item-namespace suggestion-keys)]
       (mongo-db/get-specified-values collection-name suggestion-keys string/nonempty?)))
