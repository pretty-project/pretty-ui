
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.download.helpers
    (:require [mid-fruits.keyword                 :as keyword]
              [mongo-db.api                       :as mongo-db]
              [pathom.api                         :as pathom]
              [plugins.item-editor.engine.helpers :as engine.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->item-suggestions
  ; @param (map) env
  ;
  ; @return (map)
  [env]
  (let [extension-id    (pathom/env->param env :extension-id)
        item-namespace  (pathom/env->param env :item-namespace)
        suggestion-keys (pathom/env->param env :suggestion-keys)
        collection-name (engine.helpers/collection-name extension-id item-namespace)
        suggestion-keys (keyword/add-items-namespace item-namespace suggestion-keys)]
       (mongo-db/get-specified-values collection-name suggestion-keys string/nonempty?)))
