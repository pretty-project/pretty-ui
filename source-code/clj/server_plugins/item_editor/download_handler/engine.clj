
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-editor.download-handler.engine
    (:require [mid-fruits.keyword                            :as keyword]
              [mid-plugins.item-editor.editor-handler.engine :as editor-handler.engine]
              [mongo-db.api                                  :as mongo-db]
              [pathom.api                                    :as pathom]))



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
        collection-name (editor-handler.engine/collection-name extension-id item-namespace)
        suggestion-keys (keyword/add-items-namespace item-namespace suggestion-keys)]
       (mongo-db/get-specified-values collection-name suggestion-keys string/nonempty?)))
