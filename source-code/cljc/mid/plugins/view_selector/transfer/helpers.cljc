
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.plugins.view-selector.transfer.helpers)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (transfer.helpers/transfer-id :my-extension)
  ;  =>
  ;  :my-extension.view-selector/transfer-selector-props
  ;
  ; @return (keyword)
  [extension-id]
  (keyword (str (name extension-id) ".view-selector/transfer-selector-props")))
