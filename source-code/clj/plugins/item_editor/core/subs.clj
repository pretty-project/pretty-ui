
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.subs
    (:require [plugins.plugin-handler.core.subs :as core.subs]
              [x.server-core.api                :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.core.subs
(def get-plugin-prop core.subs/get-plugin-prop)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-editor-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (keyword) item-key
  ;
  ; @return (map)
  [db [_ lister-id item-key]]
  (r get-plugin-prop db lister-id item-key))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :item-editor/get-editor-prop get-editor-prop)
