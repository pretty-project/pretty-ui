
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn init-editor!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace editor-props]]
  (assoc-in db [:plugins :item-editor/editor-props extension-id] editor-props))
