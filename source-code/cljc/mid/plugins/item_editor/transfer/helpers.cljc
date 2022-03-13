
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.item-editor.transfer.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @example
  ;  (transfer.helpers/transfer-id :my-extension :my-type)
  ;  =>
  ;  :my-extension.my-type-editor/transfer-editor-props
  ;
  ; @return (keyword)
  [extension-id item-namespace]
  (keyword (str (name extension-id)   "."
                (name item-namespace) "-editor")
           "transfer-editor-props"))
