
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.item-editor.events)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ extension-id _ editor-props]]

  ; Biztos, hogy az item-editor plugin is így töltődik be?
  ; XXX#8705
  ;(update-in db [extension-id :item-editor/meta-items] map/reverse-merge editor-props))

  (assoc-in db [extension-id :item-editor/meta-items] editor-props))
