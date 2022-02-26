
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.engine)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-edit-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (item-path vector)
  [extension-id editor-id]
  [extension-id :value-editor/data-items editor-id])

(defn view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; (engine/view-id :my-extension :my-editor)
  ; =>
  ; :my-extension.my-editor/view
  ;
  ; @return (keyword)
  [extension-id editor-id]
  (keyword (str (name extension-id) "."
                (name editor-id))
           "view"))
